package controls.inputcontrol;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class IntegerTextInputControl extends TextInputControl
{
    private boolean allowNegative;

    public IntegerTextInputControl(Composite parent)
    {
        super(parent);
    }

    @Override
    public boolean verifInput()
    {
        try
        {
            Integer i = new Integer(getData());
            if (!allowNegative && i < 0)
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return super.verifInput();
    }

    @Override
    public boolean isComplete()
    {
        Integer i = new Integer(getData());
        return !(i == 0);
    }

    public void setAllowNegative(boolean allowNegative)
    {
        this.allowNegative = allowNegative;
    }

    @Override
    public void setData(Object value)
    {
        if (value instanceof Integer)
        {
            ((Text) getControl()).setText(((Integer) value).toString());
        }
    }

}
