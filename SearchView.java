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
 * SearchView shows 
 */
final class SearchView extends JPanel implements Printable, ActionListener {
    private static final long serialVersionUID = 1L;
    private final WorkingStorage ws;
    private JButton searchb, helpb;
    private final JTextField stringField;
    private final JTextArea tLogg;
    private final JScrollPane scrollPane;

    SearchView(final WorkingStorage _ws) {
        ws = _ws;

        setLayout(new BorderLayout());

        Box jp = new Box(BoxLayout.Y_AXIS);
	tLogg = new JTextArea("Sökresultat\n");
	tLogg.setLineWrap(true);
	tLogg.setEditable(false);
	scrollPane = new JScrollPane(tLogg);
	scrollPane.setPreferredSize(new Dimension(900, 70));
	jp.add(scrollPane);
	add(jp, BorderLayout.CENTER);

	JPanel jp3 = new JPanel();
	stringField = new JTextField(20);
	jp3.add(stringField);

        searchb = new JButton("Search");
        searchb.addActionListener(this);
        jp3.add(searchb);
	
        helpb = new JButton("Help");
        helpb.addActionListener(this);
        jp3.add(helpb);
	
	add(jp3, BorderLayout.SOUTH);
    }

    private void updateView() {
    }

    public int print(Graphics g, PageFormat pf, int pi)
	throws PrinterException {
// 	if (pi >= 1) {
// 	    return Printable.NO_SUCH_PAGE;
// 	}
	
// 	Rectangle clipr = g.getClipBounds();
//  	bc.printComponent(g);
// 	return Printable.PAGE_EXISTS;
	return Printable.NO_SUCH_PAGE;
    }

    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(helpb)) {
	    MessageDialogue helpd = new MessageDialogue("SearchView\nThis is a search panel. Just enter some text in the input box and hit the search button to search all of the database to find the text. Currently searches only the comment-field of transactions. Search uses simple plain string case-insensitive matching no regexp or such.");
	    helpd.setVisible(true);
	} else if (e.getSource().equals(searchb)) {
	    // 	    PrinterJob printJob = PrinterJob.getPrinterJob();
	    // 	    printJob.setPrintable(this);
	    // 	    if (printJob.printDialog()) {
	    // 		try {
	    // 		    printJob.print();
	    // 		} catch (Exception ex) {
	    // 		    ex.printStackTrace();
	    // 		}
	    // 	    }
	    //	    System.out.println("SearchView: Doing some stuff");
	    ArrayList2D data=ws.stringSearch(stringField.getText());
	    tLogg.setText(null);
	    tLogg.append("Sökte på "+stringField.getText()+"\n");
	    if(data.getSizeY()==0) {
		tLogg.append("Hittade inga transaktioner\n");
	    } else {
		//System.out.println("x:"+data.getSizeX());
		//System.out.println("x:"+data.getSizeY());
		for(int i=0; i<data.getSizeY(); i++) {
		    tLogg.append("#"+data.get(0,i)
				 +":"+data.get(1,i)
				 +":"+data.get(2,i)
				 +":"+data.get(3,i)
				 +"\n");
		}
	    }
	} else {
	    System.out.println("Unhandled item: " + e);
	}
    }
}
