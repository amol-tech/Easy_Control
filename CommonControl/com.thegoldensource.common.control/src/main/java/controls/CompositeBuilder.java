package controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class CompositeBuilder
{
    private Composite parent;
    private int style = SWT.NONE; // SWT Style
    private int layoutColumns = 1; // GridLayout columns
    private boolean makeColumnsEqualWidth = false; // GridLayout column equal width
    private int gridStyle = GridData.FILL_BOTH; // GridData fill style
    private String label = ""; // Group composite label
    private boolean thinLayout = false;
    private boolean toolBarLayout = false;
    private FormToolkit toolkit;

    public CompositeBuilder(Composite parent)
    {
        this.parent = parent;
        Display display = Display.getCurrent() != null ? Display.getCurrent() : Display.getDefault();
        toolkit = new FormToolkit(display);
    }

    public CompositeBuilder style(int style)
    {
        this.style = style;
        return this;
    }

    public CompositeBuilder layout(int layoutColumns, boolean makeColumnsEqualWidth)
    {
        this.layoutColumns = layoutColumns;
        this.makeColumnsEqualWidth = makeColumnsEqualWidth;
        return this;
    }

    public CompositeBuilder thinLayout()
    {
        this.thinLayout = true;
        return this;
    }

    public CompositeBuilder toolBarLayout()
    {
        this.toolBarLayout = true;
        return this;
    }

    public CompositeBuilder fillHorizontal()
    {
        gridStyle = GridData.FILL_HORIZONTAL;
        return this;
    }

    public CompositeBuilder fillVertical()
    {
        gridStyle = GridData.FILL_VERTICAL;
        return this;
    }

    public CompositeBuilder label(String label)
    {
        this.label = label;
        return this;
    }

    public Composite build()
    {
        Composite composite = new Composite(parent, style);
        setLayout(composite);
        return composite;
    }

    public ExpandableComposite buildExpandableComposite()
    {
        ExpandableComposite composite = new ExpandableComposite(parent, style);
        setLayout(composite);
        composite.setText(label);
        composite.addExpansionListener(new ExpansionAdapter()
        {
            public void expansionStateChanged(ExpansionEvent e)
            {
                parent.getShell().pack(true);
            }
        });
        return composite;
    }

    public Group buildGroup()
    {
        Group group = new Group(parent, style);
        setLayout(group);
        group.setText(label);
        return group;
    }

    public Section buildSection()
    {
        FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        Section section = toolkit.createSection(parent, Section.TITLE_BAR | style);
        setLayout(section);
        section.setText(label);
        return section;
    }

    public Composite buildSectionComposite()
    {
        Composite composite = toolkit.createComposite(getParent());
        setLayout(composite);
        if (getParent() instanceof Section)
        {
            ((Section) getParent()).setClient(composite);
        }
        return composite;
    }

    public SashForm buildSashForm()
    {
        SashForm sashForm = new SashForm(parent, style);
        sashForm.setLayout(new GridLayout());
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 80;
        gridData.heightHint = 80;
        sashForm.setLayoutData(gridData);
        toolkit.adapt(sashForm);
        return sashForm;
    }

    protected void setLayout(Composite composite)
    {
        GridLayout gridLayout = new GridLayout(layoutColumns, makeColumnsEqualWidth);
        if (toolBarLayout)
        {
            gridLayout.marginHeight = 0;
        }
        else if (thinLayout)
        {
            gridLayout.marginHeight = gridLayout.marginWidth = 0;
        }
        composite.setLayout(gridLayout);
        composite.setLayoutData(new GridData(gridStyle));
    }

    public static CompositeBuilder newBuilder(Composite parent)
    {
        return new CompositeBuilder(parent);
    }

    public Composite getParent()
    {
        return parent;
    }

}
