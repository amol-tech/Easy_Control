package resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Common Image handler class
 * 
 * @author amol
 */
public class CommonImageProvider
{
    public static String ICON_CHECKED = "resources/images/checked.gif";
    public static String ICON_UNCHECKED = "resources/images/unchecked.gif";
    public static String ICON_LOCATION = "resources/images/location.gif";
    public static String ICON_EMPLOYEE = "resources/images/employee.gif";
    public static String ICON_DEPARTMENT = "resources/images/department.gif";
    public static String ICON_ALL = "resources/images/all.gif";
    public static String ICON_BUTTON_SELECT = "resources/images/radio_buttion_select.png";
    public static String ICON_BUTTON_UNSELECT = "resources/images/radio_buttion_unselect.png";
    public static String ICON_ADD = "resources/images/addAction.gif";
    public static String ICON_DELETE = "resources/images/deleteAction.png";
    public static String IMG_SPLASH = "resources/images/splash.jpg";
    public static String ICON_ERROR_32 = "resources/images/error32.png";
    private static Map<String, Image> imageMap = new HashMap<String, Image>();

    public static Image getImage(String key)
    {
        Display display = Display.getCurrent() != null ? Display.getCurrent() : Display.getDefault();
        return loadImage(display, key);
    }

    public static ImageDescriptor getImageDescriptor(String key)
    {
        Image image = getImage(key);
        return ImageDescriptor.createFromImage(image);
    }

    public static Image loadImage(Display display, String name)
    {
        Image result = null;
        if (imageMap != null && !imageMap.containsKey(name))
        {
            InputStream stream = CommonImageProvider.class.getClassLoader().getResourceAsStream(name);
            if (stream != null)
            {
                try
                {
                    result = new Image(display, stream);
                    imageMap.put(name, result);
                }
                finally
                {
                    try
                    {
                        stream.close();
                    }
                    catch (IOException unexpected)
                    {
                        throw new RuntimeException("Failed to close image input stream", unexpected);
                    }
                }
            }
        }
        else
        {
            result = imageMap.get(name);
        }
        return result;
    }
}
