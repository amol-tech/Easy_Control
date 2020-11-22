package dialogs;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import resources.CommonImageProvider;
import controls.CompositeBuilder;

public class ErrorDialog extends Dialog
{
    private String title, message;
    private Throwable error;

    protected ErrorDialog(Shell parentShell)
    {
        super(parentShell);
    }

    private ErrorDialog(Shell parentShell, String title, String message, Throwable error)
    {
        super(parentShell);
        this.title = title;
        this.message = message;
        this.error = error;
    }

    public static void open(String title, Throwable error)
    {
        Display display = Display.getCurrent() != null ? Display.getCurrent() : Display.getDefault();
        display.asyncExec(new Runnable()
        {
            public void run()
            {
                (new ErrorDialog(display.getActiveShell(), title, null, error)).open();
            }
        });
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Error");
        Composite composite = CompositeBuilder.newBuilder(parent).thinLayout().build();

        Composite cmpMessage = CompositeBuilder.newBuilder(composite).fillHorizontal().layout(3, false).build();

        Label lblErrorImage = new Label(cmpMessage, SWT.LEFT);
        lblErrorImage.setImage(CommonImageProvider.getImage(CommonImageProvider.ICON_ERROR_32));

        Label lblDummy = new Label(cmpMessage, SWT.NONE);
        lblDummy.setLayoutData(new GridData(10, 32));

        Label lblMessage = new Label(cmpMessage, SWT.NONE);
        lblMessage.setLayoutData(new GridData(400, 32));
        lblMessage.setText(title);

        message = error != null && error.getMessage() != null ? error.getMessage() : message;
        if (message != null)
        {
            Composite cmpDesc = CompositeBuilder.newBuilder(composite).fillHorizontal().toolBarLayout().build();
            Text txtDescription = new Text(cmpDesc, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
            txtDescription.setLayoutData(new GridData(483, 35));
            txtDescription.setEditable(false);
            txtDescription.setText(message);
        }

        if (error != null)
        {
            ExpandableComposite cmpExpandable = CompositeBuilder.newBuilder(parent).label("Error Detail")
                    .buildExpandableComposite();
            Composite cmpStackTrace = CompositeBuilder.newBuilder(cmpExpandable).fillHorizontal().layout(1, false)
                    .build();
            Text txtStackTrace = new Text(cmpStackTrace, SWT.MULTI);
            txtStackTrace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            txtStackTrace.setText(error != null ? ExceptionUtils.getStackTrace(error) : "");
            txtStackTrace.setLayoutData(new GridData(500, 300));
            cmpExpandable.setClient(cmpStackTrace);
        }
        return parent;
    }
}
