package controls.viewcontrol;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

public abstract class ViewControl
{
    private boolean multiSelection = false;
    private boolean enableCheckBox = false;

    public abstract Composite create();

    public abstract void setInput(Object input);

    /**
     * This method is internally called from getColumnImage() method of Label Provider and applicable only for first
     * column
     * @param element to check the instance of
     * @return image object for respective instance
     */
    public abstract Image getRecordImage(Object object);

    /**
     * This method is internally called from getColumnText() method of Label Provider, compare() method of ViewerSorter
     * and getValue() method of CellModifier
     * @param element to check the instance of
     * @param columnName name of the column
     * @return Object for the mentioned column
     */
    public abstract Object getColumnValue(Object element, String columnName);

    /**
     * This method is internally called from getFont method of Label Provider
     * @param element to check the instance of
     * @param columnName name of the column
     * @return Font for the mentioned column
     */
    public abstract Font getColumnValueFont(Object element, String columnName);

    /**
     * This method is internally called from modify() method of CellModifier
     * @param element to check the instance of
     * @param columnName name of the column
     * @param value which need to be updated for the given column to model object
     */
    public abstract void updateColumnValue(Object element, String columnName, Object value);

    /**
     * This method is internally called from openDialogBox() method of DialogCellEditor
     * @param columnName name of the column
     * @param value from table viewer to editor
     * @return value which return from dialog box
     */
    public abstract Object openDialog(String columnName, Object value);

    /**
     * This method is internally called from getValue() method of CellModifier where you can add update the combo values
     * at runtime
     * @param cProperty Column property
     * @param element to check the instance of
     */
    public abstract void updateComboColumnValues(ComboColumnProperty cProperty, Object element);

    /**
     * This method is internally called from canModify() method of CellModifier where you can add an additional logic to
     * restrict the modification on column
     * @param element
     * @param columnName
     * @return boolean value
     */
    public boolean canColumnModify(Object element, String columnName)
    {
        return true;
    }

    /**
     * Add a Column Property which will describe the column information
     * @param columnProperty
     */
    public void addColumnProperty(ColumnProperty columnProperty)
    {
        columnProperties.add(columnProperty);
    }

    /**
     * @return boolean value of a multi selction indicator
     */
    public boolean isMultiSelection()
    {
        return multiSelection;
    }

    /**
     * Set the boolean value which is an indicator to enable the multiple row selection of table.
     * @param multiSelection boolean value
     */
    public void setMultiSelection(boolean multiSelection)
    {
        this.multiSelection = multiSelection;
    }

    /**
     * @return boolean value which tell whether Check Box is enable on table.
     */
    public boolean isEnableCheckBox()
    {
        return enableCheckBox;
    }

    /**
     * Set boolean value which indicates whether check box is enable on table.
     * @param enableCheckBox
     */
    public void setEnableCheckBox(boolean enableCheckBox)
    {
        this.enableCheckBox = enableCheckBox;
    }

    /**
     * This method is internally called from getBackground() method of LabelProvider to customize the background
     * @param element
     * @param columnName
     * @return
     */
    public Color getControlBackground(Object element, String columnName)
    {
        return null;
    }

    /**
     * This method is internally called from getForeground() method of LabelProvider to customize the foreground
     * @param element
     * @param columnName
     * @return
     */
    public Color getControlForeground(Object element, String columnName)
    {
        return null;
    }

    protected List<ColumnProperty> columnProperties = new LinkedList<ColumnProperty>();

    protected String[] getColumnNames()
    {
        String[] names = new String[columnProperties.size()];
        for (int i = 0; i < columnProperties.size(); i++)
        {
            names[i] = columnProperties.get(i).getName();
        }
        return names;
    }

    protected ColumnProperty getColumnProperty(String columnName)
    {
        for (ColumnProperty cProperty : columnProperties)
        {
            if (cProperty.getName().equals(columnName))
            {
                return cProperty;
            }
        }
        return null;
    }

    protected String getColumnValueText(Object object, String columnName)
    {
        Object value = getColumnValue(object, columnName);
        ColumnProperty cProperty = getColumnProperty(columnName);
        if (value instanceof String && cProperty instanceof TextColumnProperty)
        {
            return (String) value;
        }
        else if (cProperty instanceof ComboColumnProperty)
        {
            ComboColumnProperty ccp = (ComboColumnProperty) cProperty;
            String strValue = ccp.getComboValueMap().get(value);
            return (strValue == null && value instanceof String) ? (String) value : strValue;
        }
        else if (cProperty instanceof CheckBoxColumnProperty)
        {
            if (value instanceof Boolean)
            {
                return ((Boolean) value) ? " " : "";
            }
        }
        else if (cProperty instanceof DialogColumnProperty)
        {
            if (value != null)
            {
                return value.toString();
            }
        }
        return "";
    }

    protected boolean isColumnEditable(String columnName)
    {
        for (ColumnProperty cProperty : columnProperties)
        {
            if (cProperty.getName().equals(columnName))
            {
                return cProperty.isEditable();
            }
        }
        return false;
    }

    protected class ControlLabelProvider implements ITableLabelProvider, ITableColorProvider, ITableFontProvider
    {

        public void addListener(ILabelProviderListener arg0)
        {
        }

        public void dispose()
        {
        }

        public boolean isLabelProperty(Object arg0, String arg1)
        {
            return false;
        }

        public void removeListener(ILabelProviderListener arg0)
        {
        }

        public Image getColumnImage(Object object, int columnIndex)
        {
            ColumnProperty cp = columnProperties.get(columnIndex);
            Object value = getColumnValue(object, columnProperties.get(columnIndex).getName());
            if (cp instanceof CheckBoxColumnProperty)
            {
                if (value instanceof Boolean)
                {
                    Boolean bValue = (Boolean) value;
                    if (bValue)
                    {
                        return ((CheckBoxColumnProperty) cp).getCheckedImage();
                    }
                    else
                    {
                        return ((CheckBoxColumnProperty) cp).getUncheckedImage();
                    }
                }
            }
            else if (cp instanceof DialogColumnProperty && value != null)
            {
                if (((DialogColumnProperty) cp).getImage() != null)
                {
                    return ((DialogColumnProperty) cp).getImage();
                }
            }
            if (columnIndex == 0)
            {
                return getRecordImage(object);
            }
            else
            {
                return getCellImage(object,  columnProperties.get(columnIndex).getName());
            }
        }
        
        @Override
        public String getColumnText(Object object, int columnIndex)
        {
            return getColumnValueText(object, columnProperties.get(columnIndex).getName());
        }

        @Override
        public Color getBackground(Object object, int columnIndex)
        {
            return getControlBackground(object, columnProperties.get(columnIndex).getName());
        }

        @Override
        public Color getForeground(Object object, int columnIndex)
        {
            return getControlForeground(object, columnProperties.get(columnIndex).getName());
        }

        @Override
        public Font getFont(Object object, int columnIndex)
        {
            return getColumnValueFont(object, columnProperties.get(columnIndex).getName());
        }

    }

    protected class ControlCellModifier implements ICellModifier
    {

        @Override
        public boolean canModify(Object element, String columnName)
        {
            return isColumnEditable(columnName) && canColumnModify(element, columnName);
        }

        @Override
        public Object getValue(Object element, String columnName)
        {
            Object value = getColumnValue(element, columnName);
            ColumnProperty cProperty = getColumnProperty(columnName);
            if (cProperty instanceof TextColumnProperty)
            {
                return value != null ? value : "";
            }
            else if (cProperty instanceof ComboColumnProperty)
            {
                updateComboColumnValues((ComboColumnProperty) cProperty, element);
                return ((ComboColumnProperty) cProperty).getComboValueIndex(value);
            }
            return value;
        }

        @Override
        public void modify(Object element, String columnName, Object value)
        {
            Object data = null;
            if (element instanceof TableItem)
            {
                data = ((TableItem) element).getData();
            }
            else if (element instanceof TreeItem)
            {
                data = ((TreeItem) element).getData();
            }

            ColumnProperty cProperty = getColumnProperty(columnName);
            Object updateValue = null;
            if (cProperty instanceof ComboColumnProperty && value instanceof Integer)
            {
                int valueIndex = (Integer) value;
                updateValue = ((ComboColumnProperty) cProperty).getComboValueKey(valueIndex);
            }
            else if (cProperty instanceof TextColumnProperty && value instanceof String)
            {
                if (((TextColumnProperty) cProperty).isValidInput((String) value))
                {
                    updateValue = value;
                }
            }
            else
            {
                updateValue = value;
            }
            if (updateValue != null)
            {
                updateColumnValue(data, columnName, updateValue);
                viewerRefresh();
            }
        }
    }
    
    public Image getCellImage(Object object, String columnName)
    {
        return null;
    }
    
    protected void viewerRefresh()
    {

    }

    protected class StandardDialogCellEditor extends DialogCellEditor
    {
        private DialogColumnProperty dcProperty;

        public StandardDialogCellEditor(Composite parent, DialogColumnProperty dcProperty)
        {
            super(parent, 0);
            this.dcProperty = dcProperty;
        }

        @Override
        protected Object doGetValue()
        {
            return super.doGetValue();
        }

        @Override
        protected Object openDialogBox(Control control)
        {
            return openDialog(dcProperty.getName(), getValue());
        }

    }
}
