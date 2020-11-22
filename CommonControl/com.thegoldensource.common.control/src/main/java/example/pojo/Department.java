package example.pojo;

import java.util.ArrayList;
import java.util.List;

import resources.CommonImageProvider;

public class Department extends Model
{
    private List<Employee> employees = new ArrayList<Employee>();

    public Department(String id, String name, Model parent)
    {
        super(id, name, parent);
        setImage(CommonImageProvider.getImage(CommonImageProvider.ICON_DEPARTMENT));
    }

    public List<Employee> getEmployees()
    {
        return employees;
    }

    public void setEmployees(List<Employee> empployees)
    {
        this.employees = empployees;
    }

}
