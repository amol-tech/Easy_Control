package dashboards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import controls.CompositeBuilder;

public abstract class CommonDashBoard implements DashBoards

{
    private Composite parent;
    private Composite cmpHeader, cmpDetail, cmpFooter;
    private Label lblMessage, lblVersion;

    public CommonDashBoard(Composite parent)
    {
        this.parent = parent;
    }

    @Override
    public void show()
    {
        createHeaderComposite();

        createDetailComposite();

        createFooterComposite();
    }

    private void createDetailComposite()
    {
        cmpDetail = CompositeBuilder.newBuilder(parent).thinLayout().build();
    }

    private void createFooterComposite()
    {
        cmpFooter = CompositeBuilder.newBuilder(parent).fillHorizontal().layout(2, false).thinLayout().build();
        lblMessage = new Label(cmpFooter, SWT.BORDER);
        lblMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        lblVersion = new Label(cmpFooter, SWT.BORDER);
        GridData gridData = new GridData();
        gridData.widthHint = 200;
        lblVersion.setLayoutData(gridData);
    }

    private void createHeaderComposite()
    {
        cmpHeader = CompositeBuilder.newBuilder(parent).fillHorizontal().thinLayout().build();
    }

    public Composite getHeaderComposite()
    {
        return cmpHeader;
    }

    public Composite getDetailComposite()
    {
        return cmpDetail;
    }

    public Composite getFooterComposite()
    {
        return cmpFooter;
    }
    
    public void setMessage(String message)
    {
        lblMessage.setText(message);
    }
    
    public void setVersion(String version)
    {
        lblVersion.setText(version);
    }
}
