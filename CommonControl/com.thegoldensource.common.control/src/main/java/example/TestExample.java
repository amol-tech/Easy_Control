package example;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import controls.CompositeBuilder;
import controls.ConnectionlDialog;
import controls.SplashShell;
import resources.CommonImageProvider;

public class TestExample
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Display display = new Display();

        SplashShell splashShell = new SplashShell(display,
                CommonImageProvider.getImage(CommonImageProvider.IMG_SPLASH),5);
        splashShell.open();

        final Shell shell = new Shell(display);
        shell.setText("Common Control Examples");
        shell.setSize(300, 450);

        Composite cmpTest = CompositeBuilder.newBuilder(shell).build();
        // the layout manager handle the layout
        // of the widgets in the container

        Group grpTable = CompositeBuilder.newBuilder(cmpTest).fillHorizontal().buildGroup();
        grpTable.setText("Table Controls");

        // Editable Table Control
        Button btnEditableTableControl = getTestButton(grpTable, "Editable Table Control");
        btnEditableTableControl.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                EmployeeDialog dialog = new EmployeeDialog(shell);
                dialog.open();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });

        // Sortable Table Control
        Button btnSortableTableControl = getTestButton(grpTable, "Sortable Table Control");
        btnSortableTableControl.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                EmployeeDialog dialog = new EmployeeDialog(shell, false, true);
                dialog.open();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });

        // Sortable Table Control
        Button btnSortableCheckedTableControl = getTestButton(grpTable, "Sortable Multi-check Table Control");
        btnSortableCheckedTableControl.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                EmployeeDialog dialog = new EmployeeDialog(shell, true, true);
                dialog.open();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });

        Group grpTree = CompositeBuilder.newBuilder(cmpTest).fillHorizontal().buildGroup();
        grpTree.setText("Tree Controls");

        // Searchable Table Control
        Button btnSearchableTreeControl = getTestButton(grpTree, "Searchable Tree Control");
        btnSearchableTreeControl.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                DepartmentDialog dialog = new DepartmentDialog(shell);
                dialog.open();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });

        // Filterable Table Control
        Button btnFilterableTreeControl = getTestButton(grpTree, "Filterable Tree Control");
        btnFilterableTreeControl.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                DepartmentDialog dialog = new DepartmentDialog(shell, true);
                dialog.open();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });

        Group grpNavi = CompositeBuilder.newBuilder(cmpTest).fillHorizontal().buildGroup();
        grpNavi.setText("Navigation Controls");

        Button btnNavigationControl = getTestButton(grpNavi, "Navigation Control");
        btnNavigationControl.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                DepartmentNavigationDialog dialog = new DepartmentNavigationDialog(shell);
                dialog.open();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });

        // InputControl
        Group grpInputControl = CompositeBuilder.newBuilder(cmpTest).fillHorizontal().buildGroup();
        grpInputControl.setText("Input Controls");

        Button btnInputControl = getTestButton(grpInputControl, "Employee Input Control");
        btnInputControl.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                EmployeeDetailDialog dialog = new EmployeeDetailDialog(Display.getCurrent().getActiveShell(),
                        "Employee Dialog");
                dialog.open();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });

        Button btnConnectionDetail = getTestButton(grpInputControl, "Connection Detail Dialog");
        btnConnectionDetail.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                ConnectionlDialog dialog = new ConnectionlDialog(Display.getCurrent().getActiveShell(),
                        "Connection Detail Dialog");
                dialog.open();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });

        Button btnProgressBarTimer = getTestButton(grpInputControl, "Progress Bar Timer");
        btnProgressBarTimer.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                ProgressBarTimerDialog dialog = new ProgressBarTimerDialog(Display.getCurrent().getActiveShell());
                dialog.open();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });

        shell.setLayout(new FillLayout());
        shell.open();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    private static Button getTestButton(Composite cmpTest, String title)
    {
        Button btnTest = new Button(cmpTest, SWT.PUSH);
        btnTest.setText(title);
        btnTest.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return btnTest;
    }

}
