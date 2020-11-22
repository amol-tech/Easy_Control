package controls.inputcontrol;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DecimalTextInputControl extends TextInputControl
{
    private boolean allowNegative;

    public DecimalTextInputControl(Composite parent)
    {
        super(parent);
    }

    @Override
    public boolean verifInput()
    {
        try
        {
            Double d = new Double(getData());
            if (!allowNegative && d < 0)
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

    public void setAllowNegative(boolean allowNegative)
    {
        this.allowNegative = allowNegative;
    }

    @Override
    public boolean isComplete()
    {
        Double d = new Double(getData());
        return !(d == 0);
    }

    @Override
    public void setData(Object value)
    {
        if (value instanceof Double)
        {
            ((Text) getControl()).setText(((Double) value).toString());
        }
    }
}
