package controls.viewcontrol;

public abstract class ColumnProperty
{
    private String name;
    private int width;
    private boolean editable;
    private String toolTip;

    /**
     * @param name of the column
     * @param width of the column
     * @param editable boolean value to indicate editable column
     */
    public ColumnProperty(String name, int width, boolean editable)
    {
        this.name = name;
        this.width = width;
        this.editable = editable;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public String getToolTip()
    {
        return toolTip != null ? toolTip : "";
    }

    public void setToolTip(String toolTip)
    {
        this.toolTip = toolTip;
    }

}
