package example;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import controls.viewcontrol.NavigationTableControl;
import example.pojo.AllDepartments;
import example.pojo.Department;
import example.pojo.Employee;
import example.pojo.Model;

public class DepartmentNavigationControl extends NavigationTableControl
{
    public static final String COL_ID = "Employee ID";
    public static final String COL_NAME = "Name";
    public static final String COL_DESIG = "Designation";
    public static final String COL_CONFIRM = "Confirm";
    public static final String COL_ADDRESS = "Address";

    public DepartmentNavigationControl(Composite parent, int controlHeight)
    {
        super(parent, controlHeight, 140);
    }

    @Override
    public Object[] getNavigationTreeChildren(Object element)
    {
        if (element instanceof AllDepartments)
        {
            return ((AllDepartments) element).getDepartments().toArray();
        }
        return new Object[0];
    }

    @Override
    public Object getNavigationTreeParent(Object element)
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
        if (element instanceof Employee)
        {
            Employee e = (Employee) element;
            if (columnName.equals(COL_ID))
            {
                return e.getId();
            }
            else if (columnName.equals(COL_NAME))
            {
                return e.getName();
            }
            else if (columnName.equals(COL_DESIG))
            {
                return e.getDesig();
            }
            else if (columnName.equals(COL_CONFIRM))
            {
                return e.isConfirm();
            }
            else if (columnName.equals(COL_ADDRESS))
            {
                return e.getAddress();
            }
        }
        return null;
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

    @Override
    public String getNavigationValue(Object element)
    {
        if (element instanceof Model)
        {
            Model model = (Model) element;
            return model.getName();
        }
        return null;
    }

    @Override
    public boolean show(Object element)
    {
        Object parent = getNavigationSelection();
        if (parent instanceof AllDepartments || parent == null)
        {
            return true;
        }
        else if (parent instanceof Department && element instanceof Employee)
        {
            Department department = (Department) parent;
            Employee employee = (Employee) element;
            return employee.getParent().equals(department);
        }
        return false;
    }

    @Override
    public Image getNavigationImage(Object element)
    {
        if (element instanceof Model)
        {
            return ((Model) element).getImage();
        }
        return null;
    }

}
