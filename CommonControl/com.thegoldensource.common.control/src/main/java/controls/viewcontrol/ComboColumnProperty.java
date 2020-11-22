package controls.viewcontrol;

import java.util.LinkedHashMap;
import java.util.Map;

public class ComboColumnProperty extends ColumnProperty
{
    private Map<Object, String> comboValueMap = new LinkedHashMap<Object, String>();

    /**
     * @param name of the column
     * @param width of the column
     * @param editable boolean value to indicate editable column
     * @param comboValueMap to store list of an actual object, display name for the drop down values
     */
    public ComboColumnProperty(String name, int width, boolean editable,
            Map<Object, String> comboValueMap)
    {
        super(name, width, editable);
        this.comboValueMap = comboValueMap;
    }

    public Map<Object, String> getComboValueMap()
    {
        return comboValueMap;
    }

    public String[] getComboValues()
    {
        String[] comboValues = new String[comboValueMap.size()];
        int ctr = 0;
        for (Object key : comboValueMap.keySet())
        {
            comboValues[ctr] = comboValueMap.get(key);
            ctr++;
        }
        return comboValues;
    }

    public Object getComboValueKey(int valueIndex)
    {
        if (valueIndex >= 0)
        {
            String[] comboValues = getComboValues();
            String value = comboValues[valueIndex];
            for (Object key : comboValueMap.keySet())
            {
                if (comboValueMap.get(key).equals(value))
                {
                    return key;
                }
            }
        }
        return null;
    }

    public int getComboValueIndex(Object key)
    {
        int index = 0;
        for (Object mapKey : comboValueMap.keySet())
        {
            if (mapKey.equals(key))
            {
                return index;
            }
            index++;
        }
        return index;
    }

    public void setComboValueMap(Map<Object, String> comboValueMap)
    {
        this.comboValueMap = comboValueMap;
    }

}
