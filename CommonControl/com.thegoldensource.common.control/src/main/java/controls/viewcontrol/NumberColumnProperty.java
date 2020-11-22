package controls.viewcontrol;

public class NumberColumnProperty extends TextColumnProperty
{

    public NumberColumnProperty(String name, int width, boolean editable)
    {
        super(name, width, editable);
    }

    @Override
    public boolean isValidInput(String value)
    {
        if (!value.matches("[0-9]*"))
        {
            return false;
        }
        return true;
    }

}
