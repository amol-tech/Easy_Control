package controls.viewcontrol;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import controls.CompositeBuilder;

public abstract class NavigationTableControl extends TableControl
{
    private NavigationControl navigationControl;
    private Object navigationSelection;
    private int navigationColumnWidth;

    /**
     * @param parent - Composite
     * @param controlHeight - Height of the control
     * @param navigationColumnWidth - Width of the navigation column
     */
    public NavigationTableControl(Composite parent, int controlHeight, int navigationColumnWidth)
    {
        super(parent, controlHeight);
        this.navigationColumnWidth = navigationColumnWidth > 0 ? navigationColumnWidth : 150;
    }

    @Override
    public Composite create()
    {
        Composite main = getParentComposite();
        main.setLayout(new GridLayout(2, false));
        main.setLayoutData(new GridData(GridData.FILL_BOTH));

        createNavigationComposite(main);

        Composite composite = super.create();
        getViewer().addFilter(new SelectionFilter());
        return composite;
    }

    /**
     * This method will create Table control for detail section
     * @param parent
     */
    public void createNavigationComposite(Composite parent)
    {
        Composite composite = CompositeBuilder.newBuilder(parent).build();
        navigationControl = new NavigationControl(composite, 300);
        navigationControl.addColumnProperty(new TextColumnProperty(NavigationControl.COL_BLANK, navigationColumnWidth,
                false));
        navigationControl.create();
        navigationControl.getTree().setHeaderVisible(false);
        navigationControl.getTree().setLinesVisible(false);
        navigationControl.getTree().addSelectionListener(getNavigationTreeSelectionListener());
    }

    /**
     * Selection listener to call the selection filter for tree
     * @return
     */
    private SelectionListener getNavigationTreeSelectionListener()
    {
        return new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                StructuredSelection selection = (StructuredSelection) navigationControl.getViewer().getSelection();
                if (selection != null)
                {
                    navigationSelection = selection.getFirstElement();
                    getViewer().refresh();
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent event)
            {

            }
        };
    }

    public Object getNavigationSelection()
    {
        return navigationSelection;
    }

    /**
     * This method will provide the input to tree control
     * @param input
     */
    public void setTreeInput(Object input)
    {
        if (navigationControl != null)
        {
            navigationControl.setInput(input);
        }
        navigationControl.getViewer().expandAll();
    }

    /**
     * This method is internally called from selection filter of navigation tree which allow user to apply the filter
     * criteria
     * @param element to check the instance of
     * @return boolean value
     */
    public abstract boolean show(Object element);

    /**
     * This method is internally called from getColumnText() method of navigation tree label Provider to provide display
     * name to tree
     * @param element to check the instance of
     * @return array of children
     */
    public abstract String getNavigationValue(Object element);

    /**
     * This method is internally called from getColumnImage() method of navigation tree Label Provider and applicable
     * only for first column
     * @param element to check the instance of
     * @return image object for respective instance
     */
    public abstract Image getNavigationImage(Object element);

    /**
     * This method is internally called from getChildren() method of navigation tree content Provider to provide the
     * tree logic
     * @param element to check the instance of
     * @return array of children
     */
    public abstract Object[] getNavigationTreeChildren(Object element);

    /**
     * This method is internally called from getParent() method of navigation tree content Provider to provide the tree
     * logic
     * @param element to check the instance of
     * @return parent object
     */
    public abstract Object getNavigationTreeParent(Object element);

    /**
     * Filter class for tree viewer
     * 
     * @author AKhandek
     */
    private class SelectionFilter extends ViewerFilter
    {
        public boolean select(Viewer viewer, Object parentElement, Object element)
        {
            return show(element);
        }

    }

    private class NavigationControl extends TreeControl
    {
        private static final String COL_BLANK = "";

        public NavigationControl(Composite parent, int controlHeight)
        {
            super(parent, controlHeight);
        }

        @Override
        public Object[] getTreeChildren(Object element)
        {
            return getNavigationTreeChildren(element);
        }

        @Override
        public Object getTreeParent(Object element)
        {
            return getNavigationTreeParent(element);
        }

        @Override
        public Image getRecordImage(Object element)
        {
            return getNavigationImage(element);
        }

        @Override
        public Object getColumnValue(Object element, String columnName)
        {
            if (COL_BLANK.equals(columnName))
            {
                return getNavigationValue(element);
            }
            return "";
        }

        @Override
        public void updateColumnValue(Object element, String columnName, Object value)
        {

        }

        @Override
        public Object openDialog(String columnName, Object value)
        {
            return null;
        }

    }

}
