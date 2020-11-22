package controls.viewcontrol;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import controls.CompositeBuilder;

public abstract class FilterableTreeControl extends TreeControl
{
    private Text txtSearch;
    private String searchInput;
    private Set<Object> filterableObjects = new LinkedHashSet<Object>();
    private Set<Object> searchObjects = new LinkedHashSet<Object>();
    private FontRegistry registry = new FontRegistry();
    private SearchFilter searchFilter;
    private boolean timeDebug = false;
    private SearchType searchType = SearchType.STARTS_WITH;
    private boolean filterable = false;
    private boolean highLightSearch = true;

    public enum SearchType
    {
        STARTS_WITH, CONTAIN, EXACT_MATCH
    };

    /**
     * @param parent - Composite
     * @param controlHeight - Height of the control
     */
    public FilterableTreeControl(Composite parent, int controlHeight)
    {
        super(parent, controlHeight);
    }

    @Override
    public Composite create()
    {
        Composite main = getParentComposite();
        createSearchComposite(main);

        Composite composite = super.create();
        return composite;
    }

    /**
     * Helper method to create filter
     */
    private void createFilter()
    {
        if (searchFilter == null)
        {
            searchFilter = new SearchFilter();
            getViewer().addFilter(searchFilter);
        }
    }

    /**
     * Helper method to remove and nullify the filter
     */
    private void removeFilter()
    {
        if (searchFilter != null)
        {
            getViewer().removeFilter(searchFilter);
            searchFilter = null;
        }
    }

    /**
     * This method will provide default text box to enter input for filter, user can overwrite this method to apply the
     * filter for any other control or action
     * 
     * @param parent
     */
    public void createSearchComposite(Composite parent)
    {
        Composite composite = CompositeBuilder.newBuilder(parent).layout(2, false).thinLayout().build();
        txtSearch = new Text(composite, SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        composite.setLayoutData(gridData);
        txtSearch.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        txtSearch.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                applySearch(txtSearch.getText().trim());
            }
        });
        Button btnSearchSetting = new Button(composite, SWT.PUSH);
        btnSearchSetting.setText("...");
        btnSearchSetting.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                openSearchSettingsDialog();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {

            }
        });
    }

    /**
     * This method will open Search Setting Dialog and allow user to select the search options for next search
     */
    public void openSearchSettingsDialog()
    {
        Display display = Display.getCurrent() != null ? Display.getCurrent() : Display.getDefault();
        SearchSettingDialog dialog = new SearchSettingDialog(display.getActiveShell(), searchType, filterable);
        if (dialog.open() == Window.OK)
        {
            searchType = dialog.getSearchType();
            filterable = dialog.isFilterable();
        }
    }

    /**
     * To capture the filter/selection activity time in second for performance monitoring
     * @return boolean
     */
    public boolean isTimeDebug()
    {
        return timeDebug;
    }

    /**
     * To capture the filter/selection activity time in second for performance monitoring
     */
    public void setTimeDebug(boolean timeDebug)
    {
        this.timeDebug = timeDebug;
    }

    /**
     * Return Search Type set by user
     * @return
     */
    public SearchType getSearchType()
    {
        return searchType;
    }

    /**
     * Return whether filter option is selected or not
     * @return
     */
    public boolean isFilterable()
    {
        return filterable;
    }

    /**
     * To turn on filterable behavior
     * @return boolean value
     */
    public void setFilterable(boolean filterable)
    {
        this.filterable = filterable;
    }

    /**
     * To set Search Type example Begin With or Contains
     * @param searchType
     */
    public void setSearchType(SearchType searchType)
    {
        this.searchType = searchType;
    }

    /**
     * return search input to user for view
     * 
     * @return string
     */
    public String getSearchInput()
    {
        return searchInput != null ? searchInput.toLowerCase() : "";
    }

    /**
     * This method will remove the filter if filter mode is set and also clear the search & filter objects list refresh
     * the viewer and then collapse it.
     * 
     * @param searchInput
     */
    public void clearSearch()
    {
        if (filterable)
        {
            removeFilter();
        }
        searchObjects.clear();
        filterableObjects.clear();
        getViewer().refresh();
        getViewer().collapseAll();
    }

    /**
     * To construct the list of filter & search objects and then apply the search/filter on tree, also set the selection
     * for search objects. If search input is empty then it will call clear search implicitly
     * 
     * @param searchInput
     */
    public void applySearch(String searchInput)
    {
        this.searchInput = searchInput;
        if (getSearchInput().length() == 0)
        {
            clearSearch();
            return;
        }

        if (filterable)
        {
            createFilter();
        }
        else
        {
            removeFilter();
        }
        getViewer().collapseAll();

        searchObjects.clear();
        filterableObjects.clear();

        long starTime = System.currentTimeMillis();

        // Filter Activity
        Object[] rootElements = getRootElements();
        for (int i = 0; i < rootElements.length; i++)
        {
            updateFilterObjects(rootElements[i]);
        }
        long filterTime = System.currentTimeMillis();
        if (isTimeDebug())
        {
            System.out.println("*** Time Debug (Filteration): " + (filterTime - starTime) + " ms");
        }

        // Refresh Activity
        getViewer().refresh();
        long refreshTime = System.currentTimeMillis();
        if (isTimeDebug())
        {
            System.out.println("*** Time Debug (Refresh): " + (refreshTime - filterTime) + " ms");
        }

        // Selection Activity
        getViewer().setSelection(new StructuredSelection(searchObjects.toArray()), true);
        long selectionTime = System.currentTimeMillis();
        if (isTimeDebug())
        {
            System.out.println("*** Time Debug (Selection): " + (selectionTime - refreshTime) + " ms");
        }
    }

    /**
     * Helper method to update the filter objects recursively for children
     * 
     * @param object
     */
    private void updateFilterObjects(Object object)
    {
        String searchTarget = getFilterValue(object);
        Object parentObject = getTreeParent(object);
        boolean searchCandiate = isSearchCandidate(object, searchTarget);

        if (searchCandiate)
        {
            filterableObjects.add(object);
            searchObjects.add(object);
            updateParentToFilterObjects(parentObject);
        }
        else if (isParentSearchable(parentObject))
        {
            filterableObjects.add(object);
        }

        Object[] children = getTreeChildren(object);
        for (int i = 0; i < children.length; i++)
        {
            updateFilterObjects(children[i]);
        }
    }

    /**
     * Helped method to determine the search candidate based on searchTarget and searchType
     * @param object
     * @param searchTarget
     * @return
     */
    private boolean isSearchCandidate(Object object, String searchTarget)
    {
        if (searchTarget != null)
        {
            // If search is applicable on specific column value provided by user
            return search(searchTarget);
        }
        else
        {
            // If search is applicable for all column values
            for (String columnName : getColumnNames())
            {
                searchTarget = getColumnValueText(object, columnName);
                if (searchTarget != null && search(searchTarget))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method to perform search as per the searchType
     * @param value
     * @return
     */
    private boolean search(String value)
    {
        if (searchType.equals(SearchType.STARTS_WITH))
        {
            return value.toLowerCase().startsWith(getSearchInput());
        }
        else if (searchType.equals(SearchType.CONTAIN))
        {
            return value.toLowerCase().contains(getSearchInput());
        }
        else
        {
            return value.toLowerCase().equals(getSearchInput());
        }
    }

    /**
     * Helper method to update the filter objects recursively for parent
     * 
     * @param object
     */
    private boolean isParentSearchable(Object object)
    {
        if (object != null)
        {
            if (searchObjects.contains(object))
            {
                return true;
            }
            else
            {
                Object parentObject = getTreeParent(object);
                return isParentSearchable(parentObject);
            }
        }
        return false;
    }

    /**
     * Helper method to update the filter objects recursively for parent
     * 
     * @param object
     */
    private void updateParentToFilterObjects(Object object)
    {
        if (object != null && !filterableObjects.contains(object))
        {
            filterableObjects.add(object);
            updateParentToFilterObjects(getTreeParent(object));
        }
    }

    /**
     * Filter class for tree viewer
     * 
     * @author AKhandek
     */
    private class SearchFilter extends ViewerFilter
    {
        public boolean select(Viewer viewer, Object parentElement, Object element)
        {
            return getSearchInput().length() == 0 || filterableObjects.contains(element);
        }

    }

    /**
     * This will apply the BOLD font setting on elements which is in the search object list if High Light Search is true
     */
    @Override
    public Font getColumnValueFont(Object element, String columnName)
    {
        if (searchObjects.contains(element) && highLightSearch)
        {
            if (getFilterValue(element) == null)
            {
                String searchTarget = getColumnValueText(element, columnName);
                if (search(searchTarget))
                {
                    return registry.getBold(Display.getCurrent().getSystemFont().getFontData()[0].getName());
                }
                else
                {
                    return registry.get(Display.getCurrent().getSystemFont().getFontData()[0].getName());
                }
            }
            else
            {
                return registry.getBold(Display.getCurrent().getSystemFont().getFontData()[0].getName());
            }
        }
        return null;
    }

    /**
     * This method is internally called from select() method of ViewerFilter which will filter the elements in the
     * viewer on basic of string value return by this method, default operator is "starts with"
     * 
     * @param element to check the instance of
     * @return String value for filter clause
     */
    public abstract String getFilterValue(Object element);

    public int getSearchResultCount()
    {
        return searchObjects.size();
    }

    public void setHighLightSearch(boolean highLightSearch)
    {
        this.highLightSearch = highLightSearch;
    }
}
