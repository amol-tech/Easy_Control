package controls.viewcontrol;

import java.util.List;
import java.util.Set;

public class TreeControlInput
{
    private Object input;

    public TreeControlInput(Object input)
    {
        this.input = input;
    }

    @SuppressWarnings("rawtypes")
    public Object[] getElements()
    {
        if (input instanceof List)
        {
            return ((List) input).toArray();
        }
        else if (input instanceof Set)
        {
            return ((Set) input).toArray();
        }
        else if (input instanceof Object)
        {
            return new Object[] { input };
        }
        return new Object[0];
    }

    public Object getInput()
    {
        return input;
    }
    
}
