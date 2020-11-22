package example;

import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import controls.viewcontrol.EditableObject;
import controls.viewcontrol.EditableTableControl;
import controls.viewcontrol.TableControl;
import example.pojo.Address;
import example.pojo.Employee;
import example.pojo.Model;

public class EmployeeControl extends EditableTableControl
{
    public static final String COL_ID = "Employee ID";
    public static final String COL_NAME = "Name";
    public static final String COL_DESIG = "Designation";
    public static final String COL_CONFIRM = "Confirm";
    public static final String COL_ADDRESS = "Address";
    public static final String COL_EOY = "Emp Of The Year";
    private Employee empOfTheYEar;

    public EmployeeControl(Composite parent, int controlHeight)
    {
        super(parent, controlHeight);
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
            else if (columnName.equals(COL_EOY))
            {
                return e.equals(empOfTheYEar) ? new Boolean(Boolean.TRUE) : new Boolean(Boolean.FALSE);
            }
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
    public Object openDialog(String columnName, Object value)
    {
        if (columnName.equals(COL_ADDRESS))
        {
            Address address = value != null ? (Address) value : new Address();
            AddressDialog dialog = new AddressDialog(Display.getDefault().getActiveShell(), address);
            if (dialog.open() == Window.OK)
            {
                return dialog.getAddress();
            }
        }
        return null;
    }

    @Override
    public void updateColumnValue(Object element, String columnName, Object value)
    {
        if (element instanceof Employee)
        {
            Employee e = (Employee) element;
            if (columnName.equals(COL_NAME))
            {
                if (value instanceof String)
                {
                    e.setName((String) value);
                }
            }
            else if (columnName.equals(COL_DESIG))
            {
                if (value instanceof String)
                {
                    e.setDesig((String) value);
                }
            }
            else if (columnName.equals(COL_CONFIRM))
            {
                if (value instanceof Boolean)
                {
                    e.setConfirm((Boolean) value);
                }
            }
            else if (columnName.equals(COL_ADDRESS))
            {
                if (value instanceof Address)
                {
                    e.setAddress((Address) value);
                }
            }
            else if (columnName.equals(COL_EOY))
            {
                if (value instanceof Boolean)
                {
                    empOfTheYEar = e;
                }
            }
        }
        refreshCompletnessErrorMessage();
    }

    @Override
    public void doDeleteKeyPressed(List<Object> deleteElements)
    {
        for (Object object : deleteElements)
        {
            System.out.println(((Employee) object).getName());
        }
    }

    @Override
    public EditableObject addEditableObject()
    {
        return Employee.getNewEmployee();
    }

}
