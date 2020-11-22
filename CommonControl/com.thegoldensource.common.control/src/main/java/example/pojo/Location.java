package example.pojo;

import java.util.ArrayList;
import java.util.List;

import resources.CommonImageProvider;

public class Location extends Model
{
    List<Department> departments = new ArrayList<Department>();
    
    public Location(String id, String name)
    {
        super(id, name,null);
        setImage(CommonImageProvider.getImage(CommonImageProvider.ICON_LOCATION));
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
