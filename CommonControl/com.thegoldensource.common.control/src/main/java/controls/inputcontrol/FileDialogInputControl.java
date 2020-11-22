package controls.inputcontrol;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

public class FileDialogInputControl extends DialogInputControl
{
    private String[] filterExtensions;
    private File file;
    private static final String DIALOG_TITLE = "File Selection Dialog";

    public FileDialogInputControl(Composite parent)
    {
        super(parent);
    }

    @Override
    public Object getData()
    {
        return file;
    }

    @Override
    public void setData(Object object)
    {
        if (object instanceof File)
        {
            file = (File) object;
            ((Text) getControl()).setText(file.getAbsolutePath());
        }
        else if (object == null)
        {
            file = null;
            ((Text) getControl()).setText("");
        }
    }

    @Override
    public void openDialog()
    {
        FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
        dialog.setText(DIALOG_TITLE);
        if (filterExtensions != null)
        {
            dialog.setFilterExtensions(filterExtensions);
        }
        String filePath = dialog.open();
        if (filePath != null && filePath.length() > 0)
        {
            file = new File(filePath);
            ((Text) getControl()).setText(filePath);
        }
    }

    public void setFilterExtension(String[] filterExtensions)
    {
        this.filterExtensions = filterExtensions;
    }

}
