package example.pojo;

import java.util.ArrayList;
import java.util.List;

import resources.CommonImageProvider;

public class AllDepartments extends Model
{
    private List<Department> departments = new ArrayList<Department>();

    public AllDepartments(String id, String name)
    {
        super(id, name, null);
        setImage(CommonImageProvider.getImage(CommonImageProvider.ICON_ALL));
    }

    public List<Department> getDepartments()
    {
        return departments;
    }

    public void setDepartments(List<Department> departments)
    {
        this.departments = departments;
    }

}
