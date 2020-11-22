package controls.viewcontrol;

import org.eclipse.swt.graphics.Image;

import resources.CommonImageProvider;

public class RadioButtonColumnProperty extends CheckBoxColumnProperty
{

    public RadioButtonColumnProperty(String name, int width, boolean editable)
    {
        super(name, width, editable);
    }
    
    public Image getCheckedImage()
    {
        return CommonImageProvider.getImage(CommonImageProvider.ICON_BUTTON_SELECT);
    }

    public Image getUncheckedImage()
    {
        return CommonImageProvider.getImage(CommonImageProvider.ICON_BUTTON_UNSELECT);
    }
}
