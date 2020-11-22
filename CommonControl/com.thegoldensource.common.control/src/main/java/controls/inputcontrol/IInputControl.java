package controls.inputcontrol;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;

public interface IInputControl
{
    /**
     * @return boolean value for required control
     * @Data
     */
    public boolean isRequired();

    public void setRequired(boolean value);

    public Object getData();

    public void setData(Object value);

    public String getLabel();

    public void setLabel(String label);

    public Control getControl();

    public void createControl();

    public boolean isComplete();

    public GridData getGridData();

    public void setGridData(GridData gridData);

    public void setStyle(int style);

    public int getStyle();

    public Object getDefaultValue();

    public void setDefaultValue(Object defaultValue);
}
