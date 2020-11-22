package example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import resources.CommonImageProvider;
import controls.CompositeBuilder;
import controls.viewcontrol.CheckBoxColumnProperty;
import controls.viewcontrol.ComboColumnProperty;
import controls.viewcontrol.DialogColumnProperty;
import controls.viewcontrol.EditableTableControl;
import controls.viewcontrol.RadioButtonColumnProperty;
import controls.viewcontrol.TableControl;
import controls.viewcontrol.TextColumnProperty;
import example.pojo.Employee;

public class EmployeeDialog extends Dialog
{
    private boolean enableCheckBox = false;
    private boolean sortable = false;
    private List<Employee> employees = new ArrayList<Employee>();

    public EmployeeDialog(Shell parentShell)
    {
        super(parentShell);
    }

    public EmployeeDialog(Shell parentShell, boolean enableCheckBox)
    {
        super(parentShell);
        this.enableCheckBox = enableCheckBox;
    }

    public EmployeeDialog(Shell parentShell, boolean enableCheckBox, boolean sortable)
    {
        super(parentShell);
        this.enableCheckBox = enableCheckBox;
        this.sortable = sortable;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Employee Details");
        Composite composite = CompositeBuilder.newBuilder(parent).build();

        TableControl control = null;
        if (sortable)
        {
            control = new SortableEmployeeControl(parent, 300);
            control.addColumnProperty(new TextColumnProperty(EmployeeControl.COL_ID, 100, false));
            control.addColumnProperty(new TextColumnProperty(EmployeeControl.COL_NAME, 150, false));
            control.addColumnProperty(new ComboColumnProperty(EmployeeControl.COL_DESIG, 150, false,
                    getDesignationMap()));
            control.addColumnProperty(new CheckBoxColumnProperty(EmployeeControl.COL_CONFIRM, 100, false));
        }
        else
        {
            control = new EmployeeControl(parent, 300);
            control.addColumnProperty(new TextColumnProperty(EmployeeControl.COL_ID, 120, false));
            control.addColumnProperty(new TextColumnProperty(EmployeeControl.COL_NAME, 150, true));
            control.addColumnProperty(new ComboColumnProperty(EmployeeControl.COL_DESIG, 150, true, getDesignationMap()));
            control.addColumnProperty(new CheckBoxColumnProperty(EmployeeControl.COL_CONFIRM, 100, true));
            DialogColumnProperty dialogColumnProperty = new DialogColumnProperty(EmployeeControl.COL_ADDRESS, 100, true);
            dialogColumnProperty.setImage(CommonImageProvider.getImage(CommonImageProvider.ICON_ALL));
            control.addColumnProperty(dialogColumnProperty);
            control.addColumnProperty(new RadioButtonColumnProperty(EmployeeControl.COL_EOY, 110, true));
            //((EditableTableControl)control).setEnableActions(false);
        }
        control.setEnableCheckBox(enableCheckBox);
        control.create();
        
        employees = TestDataProvider.getEmployees();
        control.setInput(employees);

        return composite;
    }

    private HashMap<Object, String> getDesignationMap()
    {
        HashMap<Object, String> valueMap = new HashMap<Object, String>();

        valueMap.put("SE", "Software Engineer");
        valueMap.put("TE", "Test Engineer");
        valueMap.put("CN", "Consultant");
        valueMap.put("PM", "Project Manager");
        valueMap.put("SM", "Support Manager");
        valueMap.put("QM", "Quality Manager");

        return valueMap;
    }
    
    @Override
    protected void okPressed()
    {
        super.okPressed();
    }
}
