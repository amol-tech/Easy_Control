package controls.viewcontrol;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public abstract class SortableTableControl extends TableControl
{

    public SortableTableControl(Composite parent)
    {
        super(parent);
    }

    public SortableTableControl(Composite parent, int controlHeight)
    {
        super(parent, controlHeight);
    }

    /**
     * Override method to add sort listener for table column
     */
    @Override
    public Composite create()
    {
        Composite composite = super.create();
        Listener sortListener = createSortListner();
        boolean firstColumn = true;
        for (TableColumn tableColumn : getTable().getColumns())
        {
            // If check box for table control is enabled then do not add sort listener for first column since check
            // listener is already added on first column
            if (!(firstColumn && isEnableCheckBox()))
            {
                tableColumn.addListener(SWT.Selection, sortListener);
            }
            firstColumn = false;
        }
        return composite;
    }

    /**
     * Helper method to create sort listener
     * @return Sort Listener
     */
    private Listener createSortListner()
    {
        Listener sortListener = new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                Table table = getViewer().getTable();
                TableColumn sortColumn = table.getSortColumn();
                TableColumn currentColumn = (TableColumn) event.widget;
                String columnName = currentColumn.getText();
                int direction = table.getSortDirection();

                if (currentColumn.equals(sortColumn))
                {
                    direction = direction == SWT.UP ? SWT.DOWN : SWT.UP;
                }
                else
                {
                    table.setSortColumn(currentColumn);
                    direction = SWT.UP;
                }

                table.setSortDirection(direction);
                getViewer().setSorter(new TableViewerSorter(columnName, direction));
            }
        };
        return sortListener;
    }
}
