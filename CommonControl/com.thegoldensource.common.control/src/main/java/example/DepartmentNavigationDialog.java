package example;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import controls.CompositeBuilder;
import controls.viewcontrol.CheckBoxColumnProperty;
import controls.viewcontrol.ComboColumnProperty;
import controls.viewcontrol.DialogColumnProperty;
import controls.viewcontrol.TextColumnProperty;

public class DepartmentNavigationDialog extends Dialog
{

    public DepartmentNavigationDialog(Shell parentShell)
    {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Select Employee");
        Composite composite = CompositeBuilder.newBuilder(parent).build();

        DepartmentNavigationControl deptControl = new DepartmentNavigationControl(composite, 300);
        deptControl.addColumnProperty(new TextColumnProperty(EmployeeControl.COL_ID, 100, false));
        deptControl.addColumnProperty(new TextColumnProperty(EmployeeControl.COL_NAME, 150, false));
        deptControl.addColumnProperty(new ComboColumnProperty(EmployeeControl.COL_DESIG, 150, false,
                getDesignationMap()));
        deptControl.addColumnProperty(new CheckBoxColumnProperty(EmployeeControl.COL_CONFIRM, 100, false));
        deptControl.addColumnProperty(new DialogColumnProperty(EmployeeControl.COL_ADDRESS, 100, false));
        deptControl.create();
        deptControl.setInput(TestDataProvider.getEmployees());
        deptControl.setTreeInput(TestDataProvider.getAllDepartments());
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

}
