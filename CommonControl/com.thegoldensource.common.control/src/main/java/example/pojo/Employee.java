package example.pojo;

import controls.viewcontrol.EditableObject;
import resources.CommonImageProvider;

public class Employee extends Model implements EditableObject
{
    private static final String ERR_MSG_COMPLETENESS = "Fill employee name and designation to completer the entry!";
    private static final String NEW_ID_PREFIX = "N";
    private static final String NEW_NAME = "<New Employee>";
    private String desig;
    private boolean confirm = true;
    private Address address;
    private boolean markForDeletion;

    public Employee(String id, String name, String desig, Model parent)
    {
        super(id, name, parent);
        this.desig = desig;
        setImage(CommonImageProvider.getImage(CommonImageProvider.ICON_EMPLOYEE));
    }

    public String getDesig()
    {
        return desig;
    }

    public void setDesig(String desig)
    {
        this.desig = desig;
    }

    public boolean isConfirm()
    {
        return confirm;
    }

    public void setConfirm(boolean confirm)
    {
        this.confirm = confirm;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    @Override
    public boolean isMarkForDeletion()
    {
        return markForDeletion;
    }

    @Override
    public void markForDeletion()
    {
        markForDeletion = true;
    }

    @Override
    public boolean isNew()
    {
        return getId().startsWith(NEW_ID_PREFIX);
    }

    @Override
    public boolean isComplete()
    {
        return !getName().equals(NEW_NAME) && getName().length() > 0 && desig != null && desig.length() > 0;
    }

    public static Employee getNewEmployee()
    {
        Employee employee = new Employee(NEW_ID_PREFIX + System.currentTimeMillis(), NEW_NAME, null, null);
        return employee;
    }

    @Override
    public String getCompletenessError()
    {
        return ERR_MSG_COMPLETENESS;
    }

}
