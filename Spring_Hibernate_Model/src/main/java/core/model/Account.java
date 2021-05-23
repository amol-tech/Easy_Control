package core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account
{
    @Id
    @Column(name = "nAccountId")
    private String id;
    
    @Column(name = "sName")
    private String name;
    
    @Column(name = "sGroup")
    private String group;
    
    @Column(name = "nCreditPercent")
    private double creditPercent;
    
    @Column(name = "sLocation")
    private String location;
    
    public Account()
    {
        super();
    }

    public Account(String id, String name, String group, double creditPercent, String location)
    {
        super();
        this.id = id;
        this.name = name;
        this.group = group;
        this.creditPercent = creditPercent;
        this.location = location;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public double getCreditPercent()
    {
        return creditPercent;
    }

    public void setCreditPercent(double creditPercent)
    {
        this.creditPercent = creditPercent;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
    
}
