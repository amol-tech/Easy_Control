package controls.viewcontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import resources.CommonImageProvider;
import controls.CompositeBuilder;

public abstract class TableControl extends ViewControl
{
    private static final String COL_CHECK = "-";
    private static final int KEY_DELETE = 127;
    private Composite parent;
    private int controlHeight;
    private TableViewer tableViewer;
    private Table table;
    private String defaultSortColumnName;
    private boolean checkAll = false;
    private Map<ColumnProperty, CellEditor> columnEditors = new HashMap<ColumnProperty, CellEditor>();

    /**
     * @param parent - Composite
     * @param controlHeight - Height of the control
     */
    public TableControl(Composite parent, int controlHeight)
    {
        this.parent = parent;
        this.controlHeight = controlHeight;
    }

    /**
     * @param parent - Composite
     */
    public TableControl(Composite parent)
    {
        this.parent = parent;
        this.controlHeight = 0;
    }

    public Composite create()
    {

        Composite composite = CompositeBuilder.newBuilder(parent).build();

        table = getTable(composite);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        if (controlHeight > 0)
        {
            gridData.heightHint = controlHeight;
        }
        table.setLayoutData(gridData);

        // Decorate Columns
        TableColumn tc;

        /**
         * If check box is enable for table control then need to add dummy column for adding listener to check/un-check
         * all rows
         */
        if (isEnableCheckBox())
        {
            columnProperties.add(0, new TextColumnProperty(COL_CHECK, 30, false));
        }

        for (ColumnProperty cProperty : columnProperties)
        {
            tc = new TableColumn(table, SWT.LEFT);
            tc.setText(cProperty.getName());
            tc.setWidth(cProperty.getWidth());
            tc.setToolTipText(cProperty.getToolTip());
            // Adding a listener for dummy column
            if (cProperty.getName().equals(COL_CHECK))
            {
                tc.addListener(SWT.Selection, createCheckListner());
                tc.setImage(CommonImageProvider.getImage(CommonImageProvider.ICON_UNCHECKED));
            }
        }

        tableViewer = getViewer();
        tableViewer.setContentProvider(getTableContentProvider());
        tableViewer.setLabelProvider(new ControlLabelProvider());
        tableViewer.setColumnProperties(getColumnNames());

        // Set Default Sorter
        if (defaultSortColumnName != null)
        {
            if (getColumnProperty(defaultSortColumnName) != null)
            {
                tableViewer.setSorter(new TableViewerSorter(defaultSortColumnName, SWT.UP));
            }
        }

        // Set Editors & Modifiers
        CellEditor[] editors = new CellEditor[columnProperties.size()];
        for (int i = 0; i < editors.length; i++)
        {
            ColumnProperty cp = columnProperties.get(i);
            if (cp instanceof ComboColumnProperty)
            {
                ComboColumnProperty ccp = (ComboColumnProperty) cp;
                editors[i] = new ComboBoxCellEditor(table, ccp.getComboValues(), SWT.READ_ONLY);
            }
            else if (cp instanceof CheckBoxColumnProperty)
            {
                editors[i] = new CheckboxCellEditor(table);
            }
            else if (cp instanceof DialogColumnProperty)
            {
                editors[i] = new StandardDialogCellEditor(table, (DialogColumnProperty) cp);
            }
            else
            {
                editors[i] = new TextCellEditor(table);
            }
            columnEditors.put(cp, editors[i]); // Update the map for column editor linkage
        }
        tableViewer.setCellEditors(editors);
        tableViewer.setCellModifier(new ControlCellModifier());

        // Adding double click event for single selection
        if (!isMultiSelection())
        {
            tableViewer.addDoubleClickListener(getDoubleClickListener());
        }

        // Adding delete key event
        table.addKeyListener(getDeleteKeyListener());

        /**
         * If check box is enable for table control then need to add check state change listener on viewer so that if
         * any row select/de-select then it will set the null image all rows
         */
        if (isEnableCheckBox() && tableViewer instanceof CheckboxTableViewer)
        {
            ((CheckboxTableViewer) tableViewer).addCheckStateListener(new ICheckStateListener()
            {
                @Override
                public void checkStateChanged(CheckStateChangedEvent event)
                {
                    table.getColumns()[0].setImage(null);
                    checkAll = true;
                }
            });
        }

        return composite;
    }

    /**
     * Parent composite for new addition
     * @return composite
     */
    public Composite getParentComposite()
    {
        return parent;
    }

    /**
     * This method is internally called from getValue() method of CellModifier where you can add update the combo values
     * at runtime
     * @param cProperty Column property
     * @param element to check the instance of
     */
    @Override
    public void updateComboColumnValues(ComboColumnProperty cProperty, Object element)
    {
        CellEditor cellEditor = columnEditors.get(cProperty);
        if (cellEditor != null && cellEditor instanceof ComboBoxCellEditor)
        {
            ((ComboBoxCellEditor) cellEditor).setItems(cProperty.getComboValues());
        }
    }

    private IDoubleClickListener getDoubleClickListener()
    {
        return new IDoubleClickListener()
        {
            @Override
            public void doubleClick(DoubleClickEvent arg0)
            {
                StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
                if (selection != null && selection.getFirstElement() != null)
                {
                    doDoubleClick(selection.getFirstElement());
                }
            }
        };
    }

    private KeyListener getDeleteKeyListener()
    {
        return new KeyListener()
        {
            public void keyReleased(KeyEvent arg0)
            {
            }

            @SuppressWarnings("rawtypes")
            public void keyPressed(KeyEvent event)
            {
                if (event.keyCode == KEY_DELETE)
                {
                    List<Object> objects = new ArrayList<Object>();
                    StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
                    if (selection != null)
                    {
                        for (Iterator iterator = selection.iterator(); iterator.hasNext();)
                        {
                            objects.add((Object) iterator.next());
                        }
                    }
                    doDeleteKeyPressed(objects);
                }
            }
        };
    }

    /**
     * Give a control to take an action when user press delete button on selection
     * @return viewer
     */
    public void doDeleteKeyPressed(List<Object> deleteElements)
    {

    }

    /**
     * Give a control to take an action when user press double click on selection provided single selction property has
     * been set
     * @return viewer
     */
    public void doDoubleClick(Object selection)
    {

    }

    /**
     * Give a control to overwrite default font for each column in table
     */
    @Override
    public Font getColumnValueFont(Object element, String columnName)
    {
        return null;
    }

    /**
     * Table viewer for customization example adding a listener
     * @return viewer
     */
    public TableViewer getViewer()
    {
        if (tableViewer == null && table != null)
        {
            if (isEnableCheckBox())
            {
                tableViewer = new CheckboxTableViewer(table);
            }
            else
            {
                tableViewer = new TableViewer(table);
            }
        }
        return tableViewer;
    }

    public void setInput(Object input)
    {
        if (tableViewer != null)
        {
            tableViewer.setInput(input);
        }
    }

    public Table getTable()
    {
        return table;
    }

    public void setDefaultSortColumnName(String columnName)
    {
        this.defaultSortColumnName = columnName;
    }

    private Table getTable(Composite composite)
    {
        int style = SWT.FULL_SELECTION | SWT.BORDER;
        if (isMultiSelection())
        {
            style = style | SWT.MULTI;
        }
        if (isEnableCheckBox())
        {
            style = style | SWT.CHECK;
        }
        return new Table(composite, style);
    }

    private Listener createCheckListner()
    {
        Listener checkListener = new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                TableColumn currentColumn = (TableColumn) event.widget;
                if (tableViewer instanceof CheckboxTableViewer)
                {
                    checkAll = !checkAll;
                    ((CheckboxTableViewer) tableViewer).setAllChecked(checkAll);
                    currentColumn.setImage(CommonImageProvider.getImage(checkAll ? CommonImageProvider.ICON_CHECKED
                            : CommonImageProvider.ICON_UNCHECKED));
                }
                checkColumns(event);
            }
        };
        return checkListener;
    }

    public void checkColumns(Event event)
    {
    }

    class TableViewerSorter extends ViewerSorter
    {
        private String columnName;
        private int direction;

        public TableViewerSorter(String columnName, int direction)
        {
            super();
            this.columnName = columnName;
            this.direction = direction;
        }

        public int compare(Viewer viewer, Object object1, Object object2)
        {
            int returnValue = 0;

            String value1 = getColumnValueText(object1, columnName);
            String value2 = getColumnValueText(object2, columnName);

            value1 = value1 != null ? value1.toLowerCase() : "";
            value2 = value2 != null ? value2.toLowerCase() : "";

            returnValue = value1.compareTo(value2);

            if (direction == SWT.DOWN)
            {
                returnValue = returnValue * -1;
            }
            return returnValue;
        }
    }

    @Override
    protected void viewerRefresh()
    {
        tableViewer.refresh();
    }

    protected StructuredSelection getSelection()
    {
        return tableViewer != null ? (StructuredSelection) tableViewer.getSelection() : null;
    }

    private IStructuredContentProvider getTableContentProvider()
    {
        return new IStructuredContentProvider()
        {
            public void inputChanged(Viewer arg0, Object arg1, Object arg2)
            {
            }

            public void dispose()
            {
            }

            @SuppressWarnings("rawtypes")
            public Object[] getElements(Object input)
            {
                if (input instanceof List)
                {
                    return ((List) input).toArray();
                }
                else if (input instanceof Set)
                {
                    return ((Set) input).toArray();
                }
                return new Object[0];
            }
        };
    }
}
