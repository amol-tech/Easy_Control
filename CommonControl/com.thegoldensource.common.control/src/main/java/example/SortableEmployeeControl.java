package example;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import controls.viewcontrol.SortableTableControl;
import example.pojo.Employee;
import example.pojo.Model;

public class SortableEmployeeControl extends SortableTableControl
{
    public static final String COL_ID = "Employee ID";
    public static final String COL_NAME = "Name";
    public static final String COL_DESIG = "Designation";
    public static final String COL_CONFIRM = "Confirm";
    public static final String COL_ADDRESS = "Address";

    public SortableEmployeeControl(Composite parent, int controlHeight)
    {
        super(parent, controlHeight);
    }

    @Override
    public Image getRecordImage(Object element)
    {
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

}
