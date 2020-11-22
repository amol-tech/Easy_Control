package example;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import controls.CompositeBuilder;
import controls.viewcontrol.TextColumnProperty;

public class DepartmentDialog extends Dialog
{
    private boolean filterable = false;
    
    public DepartmentDialog(Shell parentShell)
    {
        super(parentShell);
    }
    
    public DepartmentDialog(Shell parentShell,boolean filterable)
    {
        super(parentShell);
        this.filterable = filterable;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Select Employee");
        Composite composite = CompositeBuilder.newBuilder(parent).build();
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 300;
        composite.setLayoutData(gridData);

        DepartmentControl deptControl = new DepartmentControl(composite, 300);
        deptControl.addColumnProperty(new TextColumnProperty(DepartmentControl.COL_NAME, 150, false));
        deptControl.addColumnProperty(new TextColumnProperty(DepartmentControl.COL_DESIG, 150, false));
        deptControl.setEnableCheckBox(false);
        deptControl.setFilterable(filterable);
        deptControl.create();
        deptControl.getTree().setLinesVisible(false);
        deptControl.addDefaultCheckBoxListener();
        deptControl.setTimeDebug(true);

        deptControl.setInput(TestDataProvider.getLocations());

        return composite;
    }

}
