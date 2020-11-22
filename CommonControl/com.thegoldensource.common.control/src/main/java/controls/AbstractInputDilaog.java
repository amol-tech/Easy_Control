package controls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import controls.inputcontrol.IInputControl;

public abstract class AbstractInputDilaog extends Dialog
{
    private static final String ERR_COMPLETENESS_GENERAL = "All mandatory fields value must be entered";
    private static final String ERR_COMPLETENESS = " value must be entered!";
    private List<IInputControl> controlRegistery = new ArrayList<IInputControl>();
    private String title;
    
    protected AbstractInputDilaog(Shell parentShell, String title)
    {
        super(parentShell);
        this.title = title;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText(title);
        
        decorateDialogArea(parent);
        read();
        return parent;
    }

    public void registerControl(IInputControl control)
    {
        controlRegistery.add(control);
    }

    public List<IInputControl> getControlRegistery()
    {
        return controlRegistery;
    }

    @Override
    protected void okPressed()
    {
        if (checkCompleteness() != null)
        {
            MessageDialog.openError(Display.getCurrent().getActiveShell(), "Completeness Error", checkCompleteness());
            return;
        }
        if (update())
        {
            super.okPressed();
        }
    }

    public String checkCompleteness()
    {
        for (IInputControl iInputControl : controlRegistery)
        {
            if (iInputControl.isRequired() && !iInputControl.isComplete())
            {
                iInputControl.getControl().setFocus();
                return iInputControl.getLabel() != null ? iInputControl.getLabel() + ERR_COMPLETENESS
                        : ERR_COMPLETENESS_GENERAL;
            }
        }
        return null;
    }

    public abstract Control decorateDialogArea(Composite parent);

    public abstract void read();

    public abstract boolean update();

}
