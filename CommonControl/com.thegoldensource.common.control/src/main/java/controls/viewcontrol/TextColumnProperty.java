package controls.viewcontrol;

public class TextColumnProperty extends ColumnProperty
{
    /**
     * 
     * @param name of the column
     * @param width of the column
     * @param editable boolean value to indicate editable column
     */
    public TextColumnProperty(String name, int width, boolean editable)
    {
        super(name, width, editable);
    }
    
    public boolean isValidInput(String value)
    {
        return true;
    }
}
