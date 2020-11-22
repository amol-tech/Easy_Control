package controls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * Extending this abstract dialog developer can easily develop a dialog box which helps user to select a particular
 * object from a list in table where you can also view the extra information about that object in multiple columns. User
 * can also search them base on one of the column mostly on its name.
 * @author AKhandek
 * 
 */
public abstract class SearchLookupDialog extends Dialog
{
    private String title;
    private Map<String, Integer> columnHeaders;
    private List<Object> input = new ArrayList<Object>();
    private boolean multi = false;
    private Text txtSearch;
    private TableViewer tableViewer;
    private Table table;
    private List<Object> selectedObjects = new ArrayList<Object>();
    private int height = 300;
    private static final String COL_CHECK = "-";
    private boolean checkAll = false;

    /**
     * @param parentShell - Shell
     * @param title - String value for title of the dialog
     * @param columnHeaders - Map to hold the column name and column width
     * @param input - List of input elements
     * @param multi - Boolean value to enable multiple selection using check boxes
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected SearchLookupDialog(Shell parentShell, String title, Map<String, Integer> columnHeaders, List input,
            boolean multi)
    {
        super(parentShell);
        this.title = title;
        this.columnHeaders = columnHeaders;
        this.input = input;
        this.multi = multi;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        parent.getShell().setText(title);

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite compSearch = new Composite(composite, SWT.NONE);
        compSearch.setLayout(new GridLayout(2, false));
        compSearch.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label lblSearch = new Label(compSearch, SWT.LEFT);
        lblSearch.setText("Search Text ");
        txtSearch = new Text(compSearch, SWT.BORDER);
        txtSearch.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        txtSearch.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent arg0)
            {
                tableViewer.refresh();
            }
        });

        table = getTable(composite);
        table.setLayoutData(getTableGridData());
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn tc;
        /**
         * If check box is enable for table control then need to add dummy column for adding listener to check/un-check
         * all rows
         */
        if (multi)
        {
            tc = new TableColumn(table, SWT.LEFT);
            tc.setText(COL_CHECK);
            tc.setWidth(30);
            tc.addListener(SWT.Selection, createCheckListner());
            // tc.setImage(CommonImages.getImage(CommonImages.ICON_UNCHECKED));
        }

        for (Map.Entry<String, Integer> column : columnHeaders.entrySet())
        {
            tc = new TableColumn(table, SWT.LEFT);
            tc.setText(column.getKey());
            tc.setWidth(column.getValue());
        }

        tableViewer = createTableViewer(table);
        tableViewer.setContentProvider(getTableContentProvider());
        tableViewer.setLabelProvider(new SearchLabelProvider());
        tableViewer.addFilter(new SearchFilter());
        tableViewer.setInput(input);
        tableViewer.addSelectionChangedListener(getTableSelectionListner());

        // Code for pre-selections
        if (tableViewer != null)
        {
            if (tableViewer instanceof CheckboxTableViewer)
            {
                ((CheckboxTableViewer) tableViewer).setCheckedElements(selectedObjects.toArray());
            }
            else if (selectedObjects.size() > 0)
            {
                tableViewer.setSelection(new StructuredSelection(selectedObjects.get(0)));
            }
        }

        return composite;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        super.createButtonsForButtonBar(parent);
        updateButton();
    }

    public void updateButton()
    {
        Button ok = getButton(IDialogConstants.OK_ID);
        if (ok != null)
        {
            if (selectedObjects.size() > 0)
            {
                ok.setEnabled(true);
            }
            else
            {
                ok.setEnabled(false);
            }
        }
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
                return new Object[0];
            }
        };
    }

    private ISelectionChangedListener getTableSelectionListner()
    {
        return new ISelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent arg0)
            {
                if (!multi)
                {
                    StructuredSelection sel = (StructuredSelection) tableViewer.getSelection();
                    Object object = sel.getFirstElement();
                    if (!sel.isEmpty())
                    {
                        selectedObjects.clear();
                        selectedObjects.add(object);
                        updateButton();
                    }
                }
            }
        };
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
                    CheckboxTableViewer checkBoxtableViewer = (CheckboxTableViewer) tableViewer;
                    checkBoxtableViewer.setAllChecked(checkAll);
                    // currentColumn.setImage(CommonImages.getImage(checkAll ? CommonImages.ICON_CHECKED
                    // : CommonImages.ICON_UNCHECKED));
                    if (checkAll)
                    {
                        for (Object object : checkBoxtableViewer.getCheckedElements())
                        {
                            selectedObjects.add(object);
                        }
                    }
                    else
                    {
                        selectedObjects.clear();
                    }
                    SearchLookupDialog.this.tableViewer.refresh();
                    updateButton();
                }
            }
        };
        return checkListener;
    }

    private TableViewer createTableViewer(final Table table)
    {
        if (multi)
        {
            CheckboxTableViewer tableViewer = new CheckboxTableViewer(table);
            tableViewer.addCheckStateListener(new ICheckStateListener()
            {
                public void checkStateChanged(CheckStateChangedEvent event)
                {
                    if (event.getChecked())
                    {
                        selectedObjects.add(event.getElement());
                        SearchLookupDialog.this.tableViewer.refresh();
                    }
                    else
                    {
                        selectedObjects.remove(event.getElement());
                        SearchLookupDialog.this.tableViewer.refresh();
                    }
                    table.getColumns()[0].setImage(null);
                    checkAll = true;
                    updateButton();
                }
            });
            return tableViewer;
        }
        else
        {
            TableViewer tableViewer = new TableViewer(table);
            return tableViewer;
        }
    }

    private Table getTable(Composite composite)
    {
        Table table;
        if (multi)
        {
            table = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER | SWT.CHECK);
        }
        else
        {
            table = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
        }
        return table;
    }

    private GridData getTableGridData()
    {
        // Calculating the table width according to columns sizes
        int tableWidth = 0;
        for (Map.Entry<String, Integer> column : columnHeaders.entrySet())
        {
            tableWidth = tableWidth + column.getValue();
        }

        GridData gridTable = new GridData(GridData.FILL_BOTH);
        gridTable.widthHint = tableWidth;
        gridTable.heightHint = height;
        return gridTable;
    }

    private class SearchFilter extends ViewerFilter
    {
        public boolean select(Viewer viewer, Object parentElement, Object element)
        {
            String searchValue = getSearchValue(element);
            if (searchValue != null)
            {
                return searchValue.toLowerCase().startsWith(txtSearch.getText().toLowerCase())
                        || (multi && selectedObjects.contains(element)) || txtSearch.getText().length() == 0;
            }
            return true;
        }

    }

    private Color getLightYellowColor()
    {
        ColorRegistry colorRegistry = JFaceResources.getColorRegistry();
        if (!colorRegistry.hasValueFor("veryLightYellow"))
        {
            colorRegistry.put("veryLightYellow", new RGB(255, 255, 220));
        }
        return colorRegistry.get("veryLightYellow");
    }

    private class SearchLabelProvider implements ITableLabelProvider, ITableColorProvider
    {
        public void addListener(ILabelProviderListener arg0)
        {
        }

        public void dispose()
        {
        }

        public boolean isLabelProperty(Object arg0, String arg1)
        {
            return false;
        }

        public void removeListener(ILabelProviderListener arg0)
        {
        }

        public Image getColumnImage(Object object, int columnIndex)
        {
            if (columnIndex == 0)
            {
                return getRecordImage(object);
            }
            return null;
        }

        @Override
        public String getColumnText(Object object, int columnIndex)
        {
            return getColumnValue(object, multi ? columnIndex - 1 : columnIndex);
        }

        @Override
        public Color getBackground(Object object, int arg1)
        {
            if (selectedObjects.contains(object) && multi)
            {
                return getLightYellowColor();
            }
            return null;
        }

        @Override
        public Color getForeground(Object arg0, int arg1)
        {
            return null;
        }

    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public TableViewer getTableViewer()
    {
        return tableViewer;
    }

    /**
     * Implement this method to get the image icon for each record of search list. This method is internally called from
     * getColumnImage() method of Label Provider and applicable only for first column
     * @param element to check the instance of
     * @return image object for respective instance
     */
    public abstract Image getRecordImage(Object element);

    /**
     * Implement this method to get the text value for each object in list. This method is internally called from
     * getColumnText() method of Label Provider
     * @param element to check the instance of
     * @param columnIndex the zero-based index of the column
     * @return String for the column index
     */
    public abstract String getColumnValue(Object element, int columnIndex);

    /**
     * This method is internally called from select() method of ViewerFilter which will filter the elements in the
     * viewer on basic of string value return by this method, default operator is "starts with"
     * @param element to check the instance of
     * @return String value for filter clause
     */
    public abstract String getSearchValue(Object element);

    /**
     * This method returns the selected object list
     * @return
     */
    public List<Object> getSelectedObjects()
    {
        return selectedObjects;
    }

    /**
     * This method set the selection on table viewer for given selected object list
     * @return
     */
    public void setSelection(List<Object> selectedObjects)
    {
        this.selectedObjects = selectedObjects;
    }

}
