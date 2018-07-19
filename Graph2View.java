import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * Graph2View shows a graph
 */
final class Graph2View extends JPanel implements Printable, ActionListener {
    private static final long serialVersionUID = 1L;
    private final WorkingStorage ws;
    private final JComboBox yearsList;
    private JButton allb;
    private JButton printb;
    private JButton helpb;
    private BarChart bc;
    private int year;
    private ArrayList<String> selectedAccountNames;
    private final ArrayList<JRadioButton> buttons = new ArrayList<JRadioButton>();

    Graph2View(final WorkingStorage _ws) {
        ws = _ws;

        setLayout(new BorderLayout());

        final JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(0, 1));
        JRadioButton genericButton;

        final ArrayList AccountNames = ws.getAccountNames();

        int i = 0;
        while (i < AccountNames.size()) {
            genericButton = new JRadioButton((String) AccountNames.get(i));
            genericButton.addActionListener(this);
            jp.add(genericButton);
            buttons.add(genericButton);
            i++;
        }
        JScrollPane scrollPane = new JScrollPane(jp,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.WEST);

        bc = new BarChart();
	String[] names = 
	    { "Jan", "Feb", "Mar", "Apr", "Maj", "Jun",
	      "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};
	bc.setDataName(names);
	double[] data = new double[12];
	for(i=0; i<12; i++) {
	    data[i] = (double)i+1;
	}
	bc.setData(data);

        add(bc, BorderLayout.CENTER);

        final JPanel jp3 = new JPanel();
	final JLabel yearsLabel = new JLabel("År ");
	final Calendar rightNow = Calendar.getInstance();
	final int CurrentYear = rightNow.get(Calendar.YEAR);
	final String[] yearsStrings = new String[10];
	for(i=0; i<10; i++) {
	    yearsStrings[i] = java.lang.Integer.toString(CurrentYear - i);
	}
	yearsList = new JComboBox(yearsStrings);
	yearsList.addActionListener(this);
	jp3.add(yearsLabel);
	jp3.add(yearsList);

	allb = new JButton("select all");
	allb.addActionListener(this);
	jp3.add(allb);

        printb = new JButton("Print");
        printb.addActionListener(this);
        jp3.add(printb);
	
        helpb = new JButton("Help");
        helpb.addActionListener(this);
        jp3.add(helpb);
	
	add(jp3, BorderLayout.SOUTH);
    }

    private void updateView() {
	ArrayList sums = new ArrayList();
	if(selectedAccountNames != null) {
	    String[] AccountNameList = new String[selectedAccountNames.size()];
	    for(int i=0; i<selectedAccountNames.size(); i++) {
		AccountNameList[i]=selectedAccountNames.get(i);
	    }
	    if(AccountNameList.length>0) {
		sums = ws.dumpAccountBalance(AccountNameList, year);
		
		double[] data = new double[sums.size()];
		for(int i=0; i<sums.size(); i++) {
		    data[i] = ((Double)sums.get(i)).doubleValue();
		}
		bc.setData(data);
	    } else {
		System.out.println("No accounts selected");
	    }
	} else {
	    System.out.println("No accounts selected (2)");
	}
    }

    public int print(Graphics g, PageFormat pf, int pi)
	throws PrinterException {
	if (pi >= 1) {
	    return Printable.NO_SUCH_PAGE;
	}
	
	Rectangle clipr = g.getClipBounds();
 	bc.printComponent(g);
	return Printable.PAGE_EXISTS;
    }

    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(helpb)) {
	    MessageDialogue helpd = new MessageDialogue("Graph Likviditet\nShows a balance summary for an optional number of accounts for each day over a year. Depending on screensize and such each day will be a single line. Select one or more accounts and then which year. The graph is only updated when you select year not when you select or deselect accounts. If you have a lot of transactions recorded or a slow computer there will be a delay when you select year until the graph is updated. The current view can be printed by hitting the print-button. The first graph shown is just an example you have to select year to get a real graph. You do not have to select another year to see current, just select current.");
	    helpd.setVisible(true);
	} else if (buttons.contains(e.getSource())) {
	    selectedAccountNames = new ArrayList<String>();

	    //System.out.println("Selected buttons");
	    for(int i=0; i<buttons.size(); i++) {
		if((buttons.get(i)).isSelected()) {
		    //System.out.println(((JRadioButton)buttons.get(i)).getText());
		    selectedAccountNames.add((buttons.get(i)).getText());
		}
	    }
	    //System.out.println("--");
	} else if(e.getSource().equals(yearsList)) {
	    year = Integer.parseInt((String)yearsList.getSelectedItem());
	    updateView();
	} else if(e.getSource().equals(allb)) {
	    for(int i=0; i<buttons.size(); i++) {
		(buttons.get(i)).setSelected(true);
	    }
	} else if (e.getSource().equals(printb)) {
	    PrinterJob printJob = PrinterJob.getPrinterJob();
	    java.awt.print.PageFormat pf = printJob.defaultPage();
	    pf.setOrientation(java.awt.print.PageFormat.LANDSCAPE);
	    printJob.setPrintable(this, pf);
	    if (printJob.printDialog()) {
		try {
		    printJob.print();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	} else {
	    System.out.println("Unhandled item: " + e);
	}
    }
}
