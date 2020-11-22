package controls.inputcontrol;

import model.ConnectionInfo;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import controls.ConnectionlDialog;

public class ConnectionInputControl extends DialogInputControl
{
    private ConnectionInfo connectionInfo;

    public ConnectionInputControl(Composite parent)
    {
        super(parent);
    }

    @Override
    public Object getData()
    {
        return connectionInfo;
    }

    @Override
    public void setData(Object object)
    {
        if (object instanceof ConnectionInfo)
        {
            connectionInfo = (ConnectionInfo) object;
        }
    }

    @Override
    public boolean isComplete()
    {
        return connectionInfo != null;
    }

    @Override
    public void openDialog()
    {
        ConnectionlDialog dialog = null;
        if (connectionInfo != null)
        {
            dialog = new ConnectionlDialog(Display.getDefault().getActiveShell(), "Connection Details", connectionInfo);
        }
        else
        {
            dialog = new ConnectionlDialog(Display.getDefault().getActiveShell(), "Connection Details");
        }
        if (dialog.open() == Window.OK)
        {
            connectionInfo = dialog.getConnectionInfo();
            ((Text) getControl()).setText(connectionInfo.getIdentifier());
        }
    }

}
