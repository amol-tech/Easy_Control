package controls.viewcontrol;

import org.eclipse.swt.graphics.Image;

import resources.CommonImageProvider;


public class DialogColumnProperty extends ColumnProperty
{
    private Image image;
    
    /**
     * @param name of the column
     * @param width of the column
     * @param editable boolean value to indicate editable column
     */

    public DialogColumnProperty(String name, int width, boolean editable)
    {
        super(name, width, editable);
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
