package controls.viewcontrol;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import controls.CompositeBuilder;
import controls.viewcontrol.FilterableTreeControl.SearchType;

public class SearchSettingDialog extends Dialog
{
    private static final String LBL_SEARCH_SETTING = "Search Setting";
    private static final String LBL_FILTER_ALL_ITEMS_FOUND = "Filter All items Found";
    private static final String LBL_HIGHLIGHT_ALL_ITEMS_FOUND = "Highlight All Items Found";
    private static final String LBL_EXACT_MATCH = "Exact Match";
    private static final String LBL_CONTAIN = "Contain";
    private static final String LBL_START_WITH = "Start With";
    private SearchType searchType;
    private boolean filterable;
    private Button btnStartWith, btnContain, btnExactMatch, btnHiglight, btnFilter;

    public SearchSettingDialog(Shell parentShell, SearchType searchType, boolean filterable)
    {
        super(parentShell);
        this.searchType = searchType;
        this.filterable = filterable;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText(LBL_SEARCH_SETTING);
        Composite composite = CompositeBuilder.newBuilder(parent).layout(2, false).build();

        Group grpSearhSetting = CompositeBuilder.newBuilder(composite).buildGroup();
        btnStartWith = new Button(grpSearhSetting, SWT.RADIO);
        btnStartWith.setText(LBL_START_WITH);
        btnContain = new Button(grpSearhSetting, SWT.RADIO);
        btnContain.setText(LBL_CONTAIN);
        btnExactMatch = new Button(grpSearhSetting, SWT.RADIO);
        btnExactMatch.setText(LBL_EXACT_MATCH);

        Group grpSearhOption = CompositeBuilder.newBuilder(composite).buildGroup();
        btnHiglight = new Button(grpSearhOption, SWT.RADIO);
        btnHiglight.setText(LBL_HIGHLIGHT_ALL_ITEMS_FOUND);
        btnFilter = new Button(grpSearhOption, SWT.RADIO);
        btnFilter.setText(LBL_FILTER_ALL_ITEMS_FOUND);

        setValues();

        return composite;
    }

    private void setValues()
    {
        if (SearchType.STARTS_WITH.equals(searchType))
        {
            btnStartWith.setSelection(true);
        }
        else if (SearchType.CONTAIN.equals(searchType))
        {
            btnContain.setSelection(true);
        }
        else if (SearchType.EXACT_MATCH.equals(searchType))
        {
            btnExactMatch.setSelection(true);
        }

        if (filterable)
        {
            btnFilter.setSelection(true);
        }
        else
        {
            btnHiglight.setSelection(true);
        }
    }

    private void updateValues()
    {
        if (btnStartWith.getSelection())
        {
            searchType = SearchType.STARTS_WITH;
        }
        else if (btnContain.getSelection())
        {
            searchType = SearchType.CONTAIN;
        }
        else if (btnExactMatch.getSelection())
        {
            searchType = SearchType.EXACT_MATCH;
        }
        filterable = btnFilter.getSelection();
    }

    @Override
    protected void okPressed()
    {
        updateValues();
        super.okPressed();
    }

    public SearchType getSearchType()
    {
        return searchType;
    }

    public boolean isFilterable()
    {
        return filterable;
    }

}
