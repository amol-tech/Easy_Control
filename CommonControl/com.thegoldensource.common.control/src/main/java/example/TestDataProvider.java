package example;

import java.util.ArrayList;
import java.util.List;

import example.pojo.AllDepartments;
import example.pojo.Department;
import example.pojo.Employee;
import example.pojo.Location;

public class TestDataProvider
{
    public static List<Location> getLocations()
    {
        List<Location> locations = new ArrayList<Location>();

        Location loc1 = new Location("LOC01", "Mumbai");
        Location loc2 = new Location("LOC02", "Pune");

        Department dept1 = new Department("D01", "Development", loc1);
        Department dept2 = new Department("D02", "Quality Analyst", loc1);
        Department dept3 = new Department("D03", "Support", loc2);

        loc1.getDepartments().add(dept1);
        loc1.getDepartments().add(dept2);
        loc2.getDepartments().add(dept3);

        dept1.getEmployees().add(new Employee("E01", "Allen", "SE", dept1));
        dept1.getEmployees().add(new Employee("E02", "Smith", "SE", dept1));
        dept1.getEmployees().add(new Employee("E03", "Rajan", "PM", dept1));
        Employee eConfirm = new Employee("E04", "John", "CN", dept1);
        eConfirm.setConfirm(false);
        dept1.getEmployees().add(eConfirm);

        dept2.getEmployees().add(new Employee("E05", "Smruti", "TE", dept2));
        dept2.getEmployees().add(new Employee("E06", "Tushar", "TE", dept2));
        dept2.getEmployees().add(new Employee("E07", "Kailash", "QM", dept2));

        dept3.getEmployees().add(new Employee("E08", "Krunal", "SE", dept3));
        dept3.getEmployees().add(new Employee("E09", "Vindod", "SE", dept3));
        Employee eConfirm2 = new Employee("E10", "Prakash", "SM", dept3);
        eConfirm2.setConfirm(false);
        dept3.getEmployees().add(eConfirm2);

        locations.add(loc1);
        locations.add(loc2);

        return locations;
    }

    public static List<Employee> getEmployees()
    {
        List<Employee> employees = new ArrayList<Employee>();

        for (Location loc : getLocations())
        {
            for (Department dept : loc.getDepartments())
            {
                employees.addAll(dept.getEmployees());
            }
        }

        return employees;
    }

    public static List<Department> getDepartments()
    {
        List<Department> departments = new ArrayList<Department>();

        for (Location loc : getLocations())
        {
            departments.addAll(loc.getDepartments());
        }

        return departments;
    }
    
    public static AllDepartments getAllDepartments()
    {
        List<Department> departments = new ArrayList<Department>();

        for (Location loc : getLocations())
        {
            departments.addAll(loc.getDepartments());
        }
        AllDepartments allDepartments = new AllDepartments("all", "All");
        allDepartments.setDepartments(departments);
        return allDepartments;
    }
}
