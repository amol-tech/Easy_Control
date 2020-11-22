package example;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import controls.CompositeBuilder;
import example.pojo.Address;

public class AddressDialog extends Dialog
{
    private Address address;
    private Text txtBuilding;
    private Text txtStreet;
    private Text txtCity;

    public AddressDialog(Shell parentShell, Address address)
    {
        super(parentShell);
        this.address = address;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        parent.getShell().setText("Address");
        Composite composite = CompositeBuilder.newBuilder(parent).layout(2, false).build();

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.widthHint = 150;

        Label lblBuilding = new Label(composite, SWT.LEFT);
        lblBuilding.setText("Building");
        txtBuilding = new Text(composite, SWT.BORDER);
        txtBuilding.setLayoutData(gridData);

        Label lblStreet = new Label(composite, SWT.LEFT);
        lblStreet.setText("Street");
        txtStreet = new Text(composite, SWT.BORDER);
        txtStreet.setLayoutData(gridData);

        Label lblCity = new Label(composite, SWT.LEFT);
        lblCity.setText("City");
        txtCity = new Text(composite, SWT.BORDER);
        txtCity.setLayoutData(gridData);

        readProperty();

        return composite;
    }

    private void readProperty()
    {
        txtBuilding.setText(address.getBuilding());
        txtStreet.setText(address.getStreet());
        txtCity.setText(address.getCity());
    }

    @Override
    protected void okPressed()
    {
        address.setBuilding(txtBuilding.getText());
        address.setStreet(txtStreet.getText());
        address.setCity(txtCity.getText());
        super.okPressed();
    }

    public Address getAddress()
    {
        return address;
    }
    
}
