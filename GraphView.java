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
 * GraphView shows a graph
 */
final class GraphView extends JPanel implements Printable, ActionListener {
    private static final long serialVersionUID = 1L;
    private final WorkingStorage ws;
    private final JComboBox yearsList;
    private final JComboBox typeList;
    private JButton printb;
    private JButton helpb;
    private BarChart bc;
    private int year;
    private int viewSet=1; /* 1=income, 2=expense, 3=result (ie income-expense) */

    GraphView(final WorkingStorage _ws) {
        ws = _ws;

        setLayout(new BorderLayout());

        bc = new BarChart();
	String[] names = 
	    { "Jan", "Feb", "Mar", "Apr", "Maj", "Jun",
	      "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};
	bc.setDataName(names);
	double[] data = new double[12];
	for(int i=0; i<12; i++) {
	    data[i] = (double)i+1;
	}
	bc.setData(data);

        add(bc, BorderLayout.CENTER);

        final JPanel jp3 = new JPanel();
	final JLabel yearsLabel = new JLabel("År ");
	final Calendar rightNow = Calendar.getInstance();
	final int CurrentYear = rightNow.get(Calendar.YEAR);
	final String[] yearsStrings = new String[10];
	for(int i=0; i<10; i++) {
	    yearsStrings[i] = java.lang.Integer.toString(CurrentYear - i);
	}
	yearsList = new JComboBox(yearsStrings);
	yearsList.addActionListener(this);
	jp3.add(yearsLabel);
	jp3.add(yearsList);

	year = CurrentYear;

	final JLabel typeLabel = new JLabel("Summeringssort ");
	String[] typeStrings = { "Inkomster", "Inköp", "Resultat" };
	typeList = new JComboBox(typeStrings);
	typeList.addActionListener(this);
	jp3.add(typeLabel);
	jp3.add(typeList);

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
	switch(viewSet) {
	case 1 : 
	    sums = ws.getIncomesByYear(year);
	    break;
	case 2:
	    sums = ws.getOutgoByYear(year);
	    break;
	case 3:
	    sums = ws.getResultByYear(year);
	    break;
	}
	/* System.out.println("Ritar om år: "+year);
	   for(int i=0; i<12; i++) {
	   System.out.println("Month#"+i+": "+sums.get(i));
	   }*/
	double[] data = new double[12];
	for(int i=0; i<12; i++) {
	    data[i] = ((Double)sums.get(i)).doubleValue();
	    }
	bc.setData(data);
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
	    MessageDialogue helpd = new MessageDialogue("Graph Result\nShows a summary for each month of a year. The summary can be of incomes, expenses or the net result of both. Just select year and/or type. The current view can be printed by hitting the print-button. The first graph shown is just an example you have to select year or type to get a real graph. You do not have to select another year to see current, just select current.");
	    helpd.setVisible(true);
	} else if(e.getSource().equals(yearsList)) {
	    year = Integer.parseInt((String)yearsList.getSelectedItem());
	    updateView();
	} else if(e.getSource().equals(typeList)) {
	    String choice = (String)typeList.getSelectedItem();
	    if(choice.equals("Inkomster")) {
		viewSet=1;
	    } else if (choice.equals("Inköp")) {
		viewSet=2;
	    } else if (choice.equals("Resultat")) {
		viewSet=3;
	    } else {
		System.out.println("Trasig dropdown: "+choice);
	    }
	    updateView();
	} else if (e.getSource().equals(printb)) {
	    PrinterJob printJob = PrinterJob.getPrinterJob();
	    printJob.setPrintable(this);
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
