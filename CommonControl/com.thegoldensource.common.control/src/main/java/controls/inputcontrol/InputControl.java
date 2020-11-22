package controls.inputcontrol;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public abstract class InputControl implements IInputControl
{
    protected String label;
    private Composite parent;
    private boolean required;
    private GridData gridData;
    private int style = SWT.NONE;
    private Object defaultValue;

    public InputControl(Composite parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean isRequired()
    {
        return required;
    }

    public GridData getGridData()
    {
        return gridData;
    }

    public Composite getParent()
    {
        return parent;
    }

    @Override
    public String getLabel()
    {
        return label;
    }

    @Override
    public void setLabel(String label)
    {
        this.label = label;
    }

    public void setGridData(GridData gridData)
    {
        this.gridData = gridData;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public void setStyle(int style)
    {
        this.style = style;
    }

    public int getStyle()
    {
        return style;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    
}
