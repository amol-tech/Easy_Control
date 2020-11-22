package controls.viewcontrol;

import org.eclipse.swt.graphics.Image;

import resources.CommonImageProvider;

public class CheckBoxColumnProperty extends ColumnProperty
{

    /**
     * @param name of the column
     * @param width of the column
     * @param editable boolean value to indicate editable column
     */
    public CheckBoxColumnProperty(String name, int width, boolean editable)
    {
        super(name, width, editable);
    }

    public Image getCheckedImage()
    {
        return CommonImageProvider.getImage(CommonImageProvider.ICON_CHECKED);
    }

    public Image getUncheckedImage()
    {
        return CommonImageProvider.getImage(CommonImageProvider.ICON_UNCHECKED);
    }
}
