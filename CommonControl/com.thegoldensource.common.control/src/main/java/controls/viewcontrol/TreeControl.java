package controls.viewcontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public abstract class TreeControl extends ViewControl
{
    private Composite parent;
    private int controlHeight;
    private TreeViewer treeViewer;
    private Tree tree;
    private Map<ColumnProperty, CellEditor> columnEditors = new HashMap<ColumnProperty, CellEditor>();

    /**
     * @param parent - Composite
     * @param controlHeight - Height of the control
     */
    public TreeControl(Composite parent, int controlHeight)
    {
        this.parent = parent;
        this.controlHeight = controlHeight;
    }

    /**
     * @param parent - Composite
     */
    public TreeControl(Composite parent)
    {
        this.parent = parent;
        this.controlHeight = 0;
    }

    public Composite create()
    {

        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = gridLayout.marginWidth = 0;
        composite.setLayout(gridLayout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        tree = getTree(composite);
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        if (controlHeight > 0)
        {
            gridData.heightHint = controlHeight;
        }
        tree.setLayoutData(gridData);

        // Decorate Columns
        TreeColumn tc;
        for (ColumnProperty cProperty : columnProperties)
        {
            int style = (cProperty instanceof NumberColumnProperty) ? SWT.RIGHT : SWT.LEFT;
            tc = new TreeColumn(tree, style);
            tc.setText(cProperty.getName());
            tc.setWidth(cProperty.getWidth());
        }

        treeViewer = getViewer();
        treeViewer.setContentProvider(new ControlContentProvider());
        treeViewer.setLabelProvider(new ControlLabelProvider());
        treeViewer.setColumnProperties(getColumnNames());

        // Set Editors & Modifiers
        CellEditor[] editors = new CellEditor[columnProperties.size()];
        for (int i = 0; i < editors.length; i++)
        {
            ColumnProperty cp = columnProperties.get(i);
            if (cp instanceof ComboColumnProperty)
            {
                ComboColumnProperty ccp = (ComboColumnProperty) cp;
                editors[i] = new ComboBoxCellEditor(tree, ccp.getComboValues(), SWT.READ_ONLY);
            }
            else if (cp instanceof CheckBoxColumnProperty)
            {
                editors[i] = new CheckboxCellEditor(tree);
            }
            else if (cp instanceof DialogColumnProperty)
            {
                editors[i] = new StandardDialogCellEditor(tree, (DialogColumnProperty) cp);
            }
            else
            {
                editors[i] = new TextCellEditor(tree);
            }
            columnEditors.put(cp, editors[i]); // Update the map for column editor linkage
        }
        treeViewer.setCellEditors(editors);
        treeViewer.setCellModifier(new ControlCellModifier());

        return composite;
    }

    /**
     * Parent composite for new addition
     * 
     * @return composite
     */
    public Composite getParentComposite()
    {
        return parent;
    }

    /**
     * This will add default check box listener for tree which will provide standard behavior like checked children when
     * parent get checked, gray out parent when child get checked
     */
    public void addDefaultCheckBoxListener()
    {
        if (isEnableCheckBox() && treeViewer instanceof CheckboxTreeViewer)
        {
            final CheckboxTreeViewer checkboxTreeViewer = (CheckboxTreeViewer) treeViewer;
            checkboxTreeViewer.addCheckStateListener(new ICheckStateListener()
            {
                @Override
                public void checkStateChanged(CheckStateChangedEvent event)
                {
                    setCheckedElement(event.getElement(), event.getChecked());
                }

            });
        }
    }

    /**
     * This method will checked the given tree element and also provide standard behavior like checked children when
     * parent get checked, gray out parent when child get checked
     * 
     * @param element - tree element
     * @param state - checked state
     */
    public void setCheckedElement(Object element, boolean state)
    {
        CheckboxTreeViewer checkboxTreeViewer = (CheckboxTreeViewer) treeViewer;

        // Performance point view there are two algorithm to check/uncheck the
        // tree elements
        Object parent = getTreeParent(element);
        List<Object> allElements = new ArrayList<>();

        // if root element(if parent is null) then just calculate all the
        // children and checked
        if (parent == null)
        {
            updateAllTreeElements(element, allElements);
            if (state)
            {
                // Existing elements + new all elements 
                for (Object object : checkboxTreeViewer.getCheckedElements())
                {
                    allElements.add(object);
                }
                checkboxTreeViewer.setCheckedElements(allElements.toArray());
            }
            else
            {
                for (Object object : allElements)
                {
                    checkboxTreeViewer.setChecked(object, false);
                }
            }
        }
        else
        // if not root element then update parent grayed and checked all
        // children
        {
            setTreeChildrenChecked(element, checkboxTreeViewer, state);
            setTreeParentGrayed(element, checkboxTreeViewer);
        }
    }

    /**
     * Protected method for child classes to get the root elements
     * 
     * @return array of root tree elements
     */
    protected Object[] getRootElements()
    {
        if (treeViewer != null && treeViewer.getInput() != null && treeViewer.getInput() instanceof TreeControlInput)
        {
            return ((TreeControlInput) treeViewer.getInput()).getElements();
        }
        return new Object[0];
    }

    /**
     * Private Helper method which will check/un-check all the children of given element recursively.
     * 
     * @param element
     * @param viewer
     * @param state
     */
    private void setTreeChildrenChecked(Object element, CheckboxTreeViewer viewer, boolean state)
    {
        viewer.setParentsGrayed(element, false);
        viewer.setChecked(element, state);
        for (Object child : getTreeChildren(element))
        {
            setTreeChildrenChecked(child, viewer, state);
        }
    }

    /**
     * /** Private Helper method to all tree elements in single list
     * 
     * @param element - to iterate
     * @param allElements - single array list
     */
    private void updateAllTreeElements(Object element, List<Object> allElements)
    {
        allElements.add(element);
        for (Object child : getTreeChildren(element))
        {
            updateAllTreeElements(child, allElements);
        }
    }

    /**
     * Private Helper method which will grayed/check/un-check parent of given element recursively.
     * 
     * @param element
     * @param viewer
     */
    private void setTreeParentGrayed(Object element, CheckboxTreeViewer viewer)
    {
        Object parent = getTreeParent(element);
        if (parent != null)
        {
            boolean allChildChecked = true;
            boolean allChildUnchecked = true;
            for (Object child : getTreeChildren(parent))
            {
                if (isElementChecked(child, viewer))
                {
                    allChildUnchecked = false;
                    break;
                }
            }
            for (Object child : getTreeChildren(parent))
            {
                if (!isElementChecked(child, viewer))
                {
                    allChildChecked = false;
                }
            }
            if (allChildChecked)
            {
                viewer.setChecked(parent, true);
            }
            else if (allChildUnchecked)
            {
                viewer.setChecked(parent, false);
            }
            else
            {
                viewer.setParentsGrayed(parent, true);
                viewer.setChecked(parent, true);
            }
            setTreeParentGrayed(parent, viewer);
        }
    }

    private boolean isElementChecked(Object element, CheckboxTreeViewer viewer)
    {
        for (Object elm : viewer.getCheckedElements())
        {
            if (elm.equals(element))
            {
                return true;
            }
        }
        return false;
    }

    private Tree getTree(Composite composite)
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
        return new Tree(composite, style);
    }

    private class ControlContentProvider implements ITreeContentProvider
    {

        @Override
        public void dispose()
        {
        }

        @Override
        public void inputChanged(Viewer arg0, Object arg1, Object arg2)
        {
        }

        @Override
        public Object[] getChildren(Object element)
        {
            return getTreeChildren(element);
        }

        @Override
        public Object[] getElements(Object element)
        {
            if (element instanceof TreeControlInput)
            {
                return ((TreeControlInput) element).getElements();
            }
            return new Object[0];
        }

        @Override
        public Object getParent(Object element)
        {
            return getTreeParent(element);
        }

        @Override
        public boolean hasChildren(Object element)
        {
            if (getTreeChildren(element) != null)
            {
                return getTreeChildren(element).length > 0;
            }
            return false;
        }

    }

    /**
     * Table viewer for customization example adding a listener
     * 
     * @return viewer
     */
    public TreeViewer getViewer()
    {
        if (treeViewer == null && tree != null)
        {
            if (isEnableCheckBox())
            {
                treeViewer = new CheckboxTreeViewer(tree);
            }
            else
            {
                treeViewer = new TreeViewer(tree);
            }
        }
        return treeViewer;
    }

    public void setInput(Object input)
    {
        if (treeViewer != null)
        {
            if (input instanceof TreeControlInput)
            {
                treeViewer.setInput((TreeControlInput) input);
            }
            else
            {
                treeViewer.setInput(new TreeControlInput(input));
            }
        }
    }

    /**
     * This method is internally called from getValue() method of CellModifier where you can add update the combo values
     * at runtime
     * 
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

    /**
     * Give a control to overwrite default font for each column in table
     */
    @Override
    public Font getColumnValueFont(Object element, String columnName)
    {
        return null;
    }

    public Tree getTree()
    {
        return tree;
    }

    @Override
    protected void viewerRefresh()
    {
        treeViewer.refresh();
    }

    protected StructuredSelection getSelection()
    {
        return treeViewer != null ? (StructuredSelection) treeViewer.getSelection() : null;
    }

    /**
     * This method is internally called from getChildren() method of Content Provider to provide the tree logic
     * 
     * @param element to check the instance of
     * @return array of children
     */
    public abstract Object[] getTreeChildren(Object element);

    /**
     * This method is internally called from getParent() method of Content Provider to provide the tree logic
     * 
     * @param element to check the instance of
     * @return parent object
     */
    public abstract Object getTreeParent(Object element);
}
