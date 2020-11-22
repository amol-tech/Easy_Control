package example.pojo;

import org.eclipse.swt.graphics.Image;


public abstract class Model
{
    private String id;
    private String name;
    private Model parent;
    private Image image;

    public Model(String id, String name, Model parent)
    {
        this.id = id;
        this.name = name;
        this.parent = parent;
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

    public boolean equals(Object object)
    {
        if (object instanceof Model)
            return ((Model) object).getId().equals(this.getId());
        return false;
    }

    public Model getParent()
    {
        return parent;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image)
    {
        this.image = image;
    }
}
