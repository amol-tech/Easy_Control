package example;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import controls.viewcontrol.FilterableTreeControl;
import example.pojo.Department;
import example.pojo.Employee;
import example.pojo.Location;
import example.pojo.Model;

public class DepartmentControl extends FilterableTreeControl
{
    public static final String COL_NAME = "Name";
    public static final String COL_DESIG = "Designation";

    public DepartmentControl(Composite parent, int controlHeight)
    {
        super(parent, controlHeight);
    }

    @Override
    public Object[] getTreeChildren(Object element)
    {
        if (element instanceof Location)
        {
            return ((Location) element).getDepartments().toArray();
        }
        else if (element instanceof Department)
        {
            return ((Department) element).getEmployees().toArray();
        }
        return new Object[0];
    }

    @Override
    public Object getTreeParent(Object element)
    {
        if (element instanceof Model)
        {
            return ((Model) element).getParent();
        }
        return null;
    }

    @Override
    public Image getRecordImage(Object element)
    {
        if (element instanceof Model)
        {
            return ((Model) element).getImage();
        }
        return null;
    }

    @Override
    public Object getColumnValue(Object element, String columnName)
    {
        if (element instanceof Model)
        {
            Model model = (Model) element;
            if (columnName.equals(COL_NAME))
            {
                return model.getName();
            }
            else if (columnName.equals(COL_DESIG))
            {
                if (model instanceof Employee)
                {
                    Employee e = (Employee) element;
                    return e.getDesig();
                }
            }
        }
        return null;
    }

    @Override
    public void updateColumnValue(Object element, String columnName, Object value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Object openDialog(String columnName, Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFilterValue(Object element)
    {
        return null;
    }

}
