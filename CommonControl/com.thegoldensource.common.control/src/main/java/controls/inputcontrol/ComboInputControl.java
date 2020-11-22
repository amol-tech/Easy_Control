package controls.inputcontrol;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ComboInputControl extends InputControl
{
    private Combo cmbControl;
    private Map<Object, String> sourceMap;
    private String[] source;

    public ComboInputControl(Composite parent)
    {
        super(parent);
    }

    @Override
    public Object getData()
    {
        if (cmbControl != null && !cmbControl.isDisposed())
        {
            return sourceMap != null ? getComboValueKey(cmbControl.getText()) : cmbControl.getText();
        }
        return null;
    }

    @Override
    public void setData(Object value)
    {
        if (sourceMap != null)
        {
            cmbControl.setText(sourceMap.get(value) != null ? sourceMap.get(value) : "");
        }
        else if (value instanceof String)
        {
            cmbControl.setText((String) value);
        }
    }

    @Override
    public Control getControl()
    {
        return cmbControl;
    }

    @Override
    public void createControl()
    {
        if (getLabel() != null)
        {
            Composite composite = getParent();
            Label label = new Label(composite, SWT.NONE);
            label.setText(getLabel());
            cmbControl = new Combo(composite, getStyle());
        }
        else
        {
            cmbControl = new Combo(getParent(), getStyle());
        }
        cmbControl.setLayoutData(getGridData());
        cmbControl.setItems(getComboValues());

        if (getDefaultValue() instanceof String)
        {
            cmbControl.setText((String) getDefaultValue());
        }
    }

    @Override
    public boolean isComplete()
    {
        return getData() != null;
    }

    public void setSourceMap(Map<Object, String> sourceMap)
    {
        this.sourceMap = sourceMap;
    }

    public void setSource(String[] source)
    {
        this.source = source;
    }

    public String[] getComboValues()
    {
        if (sourceMap != null)
        {
            String[] comboValues = new String[sourceMap.size()];
            int ctr = 0;
            for (Object key : sourceMap.keySet())
            {
                comboValues[ctr] = sourceMap.get(key);
                ctr++;
            }
            return comboValues;
        }
        else
        {
            return source;
        }
    }

    public Object getComboValueKey(String value)
    {
        for (Object key : sourceMap.keySet())
        {
            if (sourceMap.get(key).equals(value))
            {
                return key;
            }
        }
        return null;
    }

    @Override
    public Object getDefaultValue()
    {
        if (sourceMap != null && super.getDefaultValue() != null)
        {
            return sourceMap.get(super.getDefaultValue());
        }
        return super.getDefaultValue();
    }

}
