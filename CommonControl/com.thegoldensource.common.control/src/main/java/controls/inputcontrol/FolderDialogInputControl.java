package controls.inputcontrol;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class FolderDialogInputControl extends FileDialogInputControl
{

    private static final String FOLDER_DIALOG_TITLE = "Folder Selection Dialog";

    public FolderDialogInputControl(Composite parent)
    {
        super(parent);
    }

    @Override
    public void openDialog()
    {
        DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
        dialog.setText(FOLDER_DIALOG_TITLE);
        String folderPath = dialog.open();
        if (folderPath != null && folderPath.length() > 0)
        {
            ((Text) getControl()).setText(folderPath);
            setData(new File(folderPath));
        }

    }

}
