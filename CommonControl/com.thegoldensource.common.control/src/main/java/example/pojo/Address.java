package example.pojo;

public class Address
{

    private String building;
    private String street;
    private String city;

    public String getBuilding()
    {
        return building != null ? building : "";
    }

    public void setBuilding(String building)
    {
        this.building = building;
    }

    public String getStreet()
    {
        return street != null ? street : "";
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getCity()
    {
        return city != null ? city : "";
    }

    public void setCity(String city)
    {
        this.city = city;
    }
    
    @Override
    public String toString()
    {
        return getCity();
    }
}
