package controls.inputcontrol;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import controls.CompositeBuilder;

public abstract class DialogInputControl extends InputControl
{
    private Text txtControl;
    private Button btnSelect;

    public DialogInputControl(Composite parent)
    {
        super(parent);
    }

    public abstract Object getData();

    public abstract void setData(Object object);

    @Override
    public Control getControl()
    {
        return txtControl;
    }

    @Override
    public void createControl()
    {
        Composite composite = getParent();
        if (getLabel() != null)
        {
            Label label = new Label(composite, SWT.NONE);
            label.setText(getLabel());
        }
        Composite cmpButton = CompositeBuilder.newBuilder(composite).layout(2, false).thinLayout().build();
        txtControl = new Text(cmpButton, getStyle());
        txtControl.setLayoutData(getGridData());
        txtControl.setEditable(false);

        btnSelect = new Button(cmpButton, SWT.PUSH);
        btnSelect.setText("...");
        btnSelect.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                openDialog();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent event)
            {

            }
        });
    }

    public void addModifyListener(ModifyListener modifyListener)
    {
        this.txtControl.addModifyListener(modifyListener);
    }

    @Override
    public boolean isComplete()
    {
        return true;
    }

    public abstract void openDialog();

}
