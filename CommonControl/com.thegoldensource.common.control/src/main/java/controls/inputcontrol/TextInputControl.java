package controls.inputcontrol;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TextInputControl extends InputControl
{
    private Text txtControl;
    private String previousValue = "";

    public TextInputControl(Composite parent)
    {
        super(parent);
    }

    @Override
    public String getData()
    {
        return (txtControl != null && !txtControl.isDisposed()) ? txtControl.getText() : "";
    }

    @Override
    public Control getControl()
    {
        return txtControl;
    }

    @Override
    public void createControl()
    {
        if (getLabel() != null)
        {
            Composite composite = getParent();
            Label label = new Label(composite, SWT.NONE);
            label.setText(getLabel());
            txtControl = new Text(composite, getStyle());
        }
        else
        {
            txtControl = new Text(getParent(), getStyle());
        }
        txtControl.setLayoutData(getGridData());
        if (getDefaultValue() instanceof String)
        {
            txtControl.setText((String) getDefaultValue());
            previousValue = (String) getDefaultValue();
        }

        txtControl.addFocusListener(new FocusListener()
        {

            @Override
            public void focusLost(FocusEvent event)
            {
                if (!verifInput())
                {
                    txtControl.setText(previousValue);
                }
                previousValue = txtControl.getText();
            }

            @Override
            public void focusGained(FocusEvent event)
            {

            }
        });
    }

    @Override
    public void setData(Object value)
    {
        if(value instanceof String)
        {
            txtControl.setText((String) value);
        }
    }

    @Override
    public boolean isComplete()
    {
        return getData().length() > 0;
    }

    public boolean verifInput()
    {
        return true;
    }
}
