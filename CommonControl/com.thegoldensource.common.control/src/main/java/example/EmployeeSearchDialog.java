package example;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import controls.SearchLookupDialog;
import example.pojo.Employee;

public class EmployeeSearchDialog extends SearchLookupDialog
{

    public EmployeeSearchDialog(Shell parentShell, String title, Map<String, Integer> columnHeaders, List input,
            boolean multi)
    {
        super(parentShell, title, columnHeaders, input, multi);
    }

    @Override
    public String getColumnValue(Object element, int columnIndex)
    {
        if (element instanceof Employee)
        {
            Employee e = (Employee) element;
            if (columnIndex == 0)
            {
                return e.getName();
            }
        }
        return "";
    }

    @Override
    public Image getRecordImage(Object element)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSearchValue(Object element)
    {
        if(element instanceof Employee)
        {
            Employee e = (Employee) element;
            return e.getName();
        }
        return "";
    }



    

}
