package controls;

import model.ConnectionInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import controls.inputcontrol.ControlBuilder;
import controls.inputcontrol.IInputControl;

/**
 * This is standard Connection Dialog to support Connection Control
 * @author AKhandek
 */
public class ConnectionlDialog extends AbstractInputDilaog
{

    private static final String CONNECTION_DETAILS_TITLE = "Connection Details";
    private IInputControl hostControl, portControl, serviceControl, userControl, passwordControl;
    private ConnectionInfo connectionInfo;

    /**
     * @param parentShell - Shell
     * @param connectionDetail - Connection Information
     */
    public ConnectionlDialog(Shell parentShell, String title)
    {
        super(parentShell, CONNECTION_DETAILS_TITLE);
    }

    public ConnectionlDialog(Shell parentShell, String title, ConnectionInfo connectionInfo)
    {
        super(parentShell, CONNECTION_DETAILS_TITLE);
        this.connectionInfo = connectionInfo;
    }

    @Override
    public Control decorateDialogArea(Composite parent)
    {
        GridData gridData = new GridData(GridData.FILL_VERTICAL);
        gridData.widthHint = 300;
        Composite cmp = CompositeBuilder.newBuilder(parent).layout(2, false).build();
        cmp.setLayoutData(gridData);

        hostControl = builder(cmp).label("Host").required().buildTextControl();
        registerControl(hostControl);

        portControl = builder(cmp).label("port").required().buildTextControl();
        registerControl(portControl);

        serviceControl = builder(cmp).label("Service").required().buildTextControl();
        registerControl(serviceControl);

        userControl = builder(cmp).label("User").required().buildTextControl();
        registerControl(userControl);

        passwordControl = builder(cmp).label("Password").required().style(SWT.PASSWORD).buildTextControl();
        registerControl(passwordControl);

        return cmp;
    }

    private ControlBuilder builder(Composite cmp)
    {
        return ControlBuilder.newContolBuilder(cmp);
    }

    @Override
    public void read()
    {
        if (connectionInfo != null)
        {
            hostControl.setData(connectionInfo.getHost());
            portControl.setData(connectionInfo.getPort());
            serviceControl.setData(connectionInfo.getService());
            userControl.setData(connectionInfo.getUser());
            passwordControl.setData(connectionInfo.getPassword());
        }
    }

    @Override
    public boolean update()
    {
        if (connectionInfo == null)
        {
            connectionInfo = new ConnectionInfo();
        }
        connectionInfo.setHost((String) hostControl.getData());
        connectionInfo.setPort((String) portControl.getData());
        connectionInfo.setService((String) serviceControl.getData());
        connectionInfo.setUser((String) userControl.getData());
        connectionInfo.setPassword((String) passwordControl.getData());
        return true;
    }

    public ConnectionInfo getConnectionInfo()
    {
        return connectionInfo;
    }

}
