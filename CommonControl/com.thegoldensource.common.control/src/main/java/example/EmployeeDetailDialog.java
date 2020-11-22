package example;

import java.util.HashMap;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import controls.AbstractInputDilaog;
import controls.CompositeBuilder;
import controls.inputcontrol.ControlBuilder;
import controls.inputcontrol.IInputControl;

public class EmployeeDetailDialog extends AbstractInputDilaog
{

    private IInputControl idControl, nameControl, designationControl, salaryControl, creditPercentageControl,
            catgControl, maritalStatusControl, connInputControl, fileInputControl, folderInputDialog;

    protected EmployeeDetailDialog(Shell parentShell, String title)
    {
        super(parentShell, title);
    }

    @Override
    public Control decorateDialogArea(Composite parent)
    {
        GridData gridData = new GridData(GridData.FILL_VERTICAL);
        gridData.widthHint = 400;
        Composite cmp = CompositeBuilder.newBuilder(parent).layout(2, false).build();
        cmp.setLayoutData(gridData);

        idControl = builder(cmp).label("Identifier").required().buildTextControl();
        registerControl(idControl);

        nameControl = builder(cmp).label("Name").required().buildTextControl();
        registerControl(nameControl);

        designationControl = builder(cmp).label("Designation").required().defaultValue("SE")
                .buildComboControl(getDesignationMap());
        registerControl(designationControl);

        salaryControl = builder(cmp).label("Salary").required().buildDecimalTextControl(false);
        registerControl(salaryControl);

        creditPercentageControl = builder(cmp).label("Credit Per.").required().buildIntegerTextControl(true);
        registerControl(creditPercentageControl);

        catgControl = builder(cmp).label("Category").defaultValue("Permenant")
                .buildComboControl(new String[] { "Permenant", "Temporary" });

        maritalStatusControl = builder(cmp).label("Marital Status").buildCheckBoxControl();
        registerControl(maritalStatusControl);

        connInputControl = builder(cmp).label("Connection Details").required().buildConnectionInputControl();
        registerControl(connInputControl);

        fileInputControl = builder(cmp).label("Select Text File").buildFileDialogInputControl(new String[] { "*.txt" });
        registerControl(fileInputControl);

        folderInputDialog = builder(cmp).label("Select Directory").buildFolderDialogInputControl();
        registerControl(fileInputControl);

        return cmp;
    }

    private ControlBuilder builder(Composite composite)
    {
        return ControlBuilder.newContolBuilder(composite);
    }

    @Override
    public void read()
    {
        idControl.setData("I01");
        nameControl.setData("Swara Khandekar");
        designationControl.setData("PM");
        salaryControl.setData(2000000d);
        creditPercentageControl.setData(75);
    }

    @Override
    public boolean update()
    {
        System.out.println(idControl.getData());
        System.out.println(nameControl.getData());
        System.out.println(designationControl.getData());
        System.out.println(salaryControl.getData());
        System.out.println(creditPercentageControl.getData());
        System.out.println(catgControl.getData());
        System.out.println(maritalStatusControl.getData());
        return true;
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
