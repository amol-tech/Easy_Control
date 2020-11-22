package controls.inputcontrol;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class CheckBoxInputControl extends InputControl
{
    private Button btnCheckBox;

    public CheckBoxInputControl(Composite parent)
    {
        super(parent);
    }

    @Override
    public Boolean getData()
    {
        return (Boolean) btnCheckBox.getSelection();
    }

    @Override
    public void setData(Object value)
    {
        if (value instanceof Boolean)
        {
            btnCheckBox.setSelection((Boolean) value);
        }
    }

    @Override
    public Control getControl()
    {
        return btnCheckBox;
    }

    @Override
    public void createControl()
    {
        if (getLabel() != null)
        {
            Composite composite = getParent();
            Label label = new Label(composite, SWT.NONE);
            label.setText(getLabel());
            btnCheckBox = new Button(composite, getStyle());
        }
        else
        {
            btnCheckBox = new Button(getParent(), getStyle());
        }
        //btnCheckBox.setLayoutData(getGridData());

        if (getDefaultValue() instanceof Boolean)
        {
            btnCheckBox.setSelection((Boolean) getDefaultValue());
        }
    }

    @Override
    public boolean isComplete()
    {
        return true;
    }

}
