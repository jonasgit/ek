import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import java.util.Collections;

/**
 * TransactionsView show transactions.
 */
final class TransactionsView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final DateButton jb;
    private final DateButton jb2;
    private JButton printb;
    private JButton updateb;
    private JButton helpb;
    private final WorkingStorage ws;
    private JTable table;
    private TableModel dataModel;
    private final JList accountList;
    private final JCheckBox AllAccountsCB;
    private final JList catList;
    private final JCheckBox AllCatCB;
    private final JList userList;
    private final JCheckBox AllUsersCB;
    private final JList placeList;
    private final JCheckBox AllPlacesCB;

    private JScrollPane datascrollPane;

    TransactionsView(final WorkingStorage _ws) {
        ws = _ws;
	
        setLayout(new BorderLayout());
	
	//Panelen med kontroller
        final JPanel jp = new JPanel();
	jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
	
	//panel för kontolistan
        final JPanel accountpanel = new JPanel();
	accountpanel.setLayout(new BoxLayout(accountpanel, BoxLayout.PAGE_AXIS));
        final ArrayList<String> AccountNames = ws.getAccountNames();
        Collections.sort(AccountNames);

        final JLabel kontoLabel = new JLabel("Konto ");
        accountpanel.add(kontoLabel);

	accountList = new JList(AccountNames.toArray());
	JScrollPane scrollPane = new JScrollPane(accountList);
	accountpanel.add(scrollPane);
        AllAccountsCB = new JCheckBox("Alla");
        accountpanel.add(AllAccountsCB);
	jp.add(accountpanel);

	//panel för Vad
        final JPanel catpanel = new JPanel();
	catpanel.setLayout(new BoxLayout(catpanel, BoxLayout.PAGE_AXIS));
        final ArrayList<String> CatNames = ws.getCategories();
	Collections.sort(CatNames);

        final JLabel CatLabel = new JLabel("Vad ");
        catpanel.add(CatLabel);

	catList = new JList(CatNames.toArray());
	scrollPane = new JScrollPane(catList);
	catpanel.add(scrollPane);
	AllCatCB = new JCheckBox("Alla");
        catpanel.add(AllCatCB);
	jp.add(catpanel);

	//panel för vem
        final JPanel whopanel = new JPanel();
	whopanel.setLayout(new BoxLayout(whopanel, BoxLayout.PAGE_AXIS));
        final ArrayList<String> WhoNames = ws.getUsers();
        Collections.sort(WhoNames);

        final JLabel WhoLabel = new JLabel("Vem ");
        whopanel.add(WhoLabel);

	userList = new JList(WhoNames.toArray());
	scrollPane = new JScrollPane(userList);
	whopanel.add(scrollPane);
        AllUsersCB = new JCheckBox("Alla");
        whopanel.add(AllUsersCB);
	jp.add(whopanel);

	//panel för Plats
        final JPanel placepanel = new JPanel();
	placepanel.setLayout(new BoxLayout(placepanel, BoxLayout.PAGE_AXIS));
        final ArrayList<String> PlaceNames = ws.getPlaces();
        Collections.sort(PlaceNames);

        final JLabel PlaceLabel = new JLabel("Plats ");
        placepanel.add(PlaceLabel);

	placeList = new JList(PlaceNames.toArray());
	scrollPane = new JScrollPane(placeList);
	placepanel.add(scrollPane);
        AllPlacesCB = new JCheckBox("Alla");
        placepanel.add(AllPlacesCB);
	jp.add(placepanel);

	//panel för datumknappar
        final JPanel datepanel = new JPanel();
	datepanel.setLayout(new BoxLayout(datepanel, BoxLayout.PAGE_AXIS));

        final JLabel jff = new JLabel("  Från ");
        jb = new DateButton("Press to select date");
        datepanel.add(jff);
        datepanel.add(jb);

        final JLabel jff2 = new JLabel("  Till ");
        jb2 = new DateButton("Press to select date");
        datepanel.add(jff2);
        datepanel.add(jb2);
	jp.add(datepanel);

	//panel för övriga knappar
        final JPanel miscpanel = new JPanel();
	miscpanel.setLayout(new BoxLayout(miscpanel, BoxLayout.PAGE_AXIS));

        updateb = new JButton("Uppdatera");
        updateb.addActionListener(this);
        miscpanel.add(updateb);
	
        printb = new JButton("Print");
        printb.addActionListener(this);
        miscpanel.add(printb);
	
        helpb = new JButton("Help");
        helpb.addActionListener(this);
        miscpanel.add(helpb);
	jp.add(miscpanel);
        add(jp, BorderLayout.PAGE_END);

        dataModel = new AbstractTableModel() {
	    private static final long serialVersionUID = 1L;
            final ArrayList2D tableData = new ArrayList2D();

            public int getColumnCount() {
                return tableData.getSizeX();
            }

            public int getRowCount() {
                return tableData.getSizeY();
            }

            public boolean isCellEditable(final int row, final int col) {
                return false;
            }

            public Object getValueAt(final int row, final int col) {
                return tableData.get(col, row);
            }
            //public void setValueAt(Object value, int row, int col) {
            //  rowData[row][col] = value;
            //  fireTableCellUpdated(row, col);
            //}
        };
        table = new JTable(dataModel);
	datascrollPane = new JScrollPane(table);
        add(datascrollPane, BorderLayout.CENTER);
    }

    private void doUpdate() {
	//System.out.println("Valda konton:");
	//Object[] foo = accountList.getSelectedValues();
	//for(int i=0; i<foo.length; i++) {
	//    System.out.println(foo[i]);
	//}
	
	final ArrayList2D tData;
	
	tData = ws.getTransactions(
				   accountList.getSelectedValues(),
				   AllAccountsCB.isSelected(),
				   catList.getSelectedValues(),
				   AllCatCB.isSelected(),
				   userList.getSelectedValues(),
				   AllUsersCB.isSelected(),
				   placeList.getSelectedValues(),
				   AllPlacesCB.isSelected(),
				   jb.getDate(),
				   jb2.getDate()
				   );
	if (tData != null) {
	    remove(datascrollPane);
	    
	    dataModel = new AbstractTableModel() {
		    private static final long serialVersionUID = 1L;
		    final String[] columnNames = {"Från",
						  "Till",
						  "Typ",
						  "Plats",
						  "Datum",
						  "Kategori",
						  "Person",
						  "Summa",
						  "Kommentar"};
		    final ArrayList2D tableData = tData;
		    
		    public int getColumnCount() {
			return tableData.getSizeX();
		    }
		    
		    public int getRowCount() {
			return tableData.getSizeY();
		    }
		    
		    public boolean isCellEditable(final int row, final int col) {
			return false;
		    }
		    
		    public Object getValueAt(final int row, final int col) {
			return tableData.get(col, row);
		    }
		    
		    public String getColumnName(final int col) {
			return columnNames[col];
		    }
		};
	    table = new JTable(dataModel);
	    datascrollPane = new JScrollPane(table);
	    add(datascrollPane, BorderLayout.CENTER);
	    revalidate();
	}
    }
    
    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(helpb)) {
	    MessageDialogue helpd = new MessageDialogue("Transaktioner\nThis view will produce a log of transactions. Press buttons to select start and/or end date. If you do not select a date then today is assumed. After selecting date, select account.");
	    helpd.setVisible(true);
	} else if (e.getSource().equals(updateb)) {
	    doUpdate();
	} else if (e.getSource().equals(printb)) {
	    MessageFormat headerFormat = new MessageFormat("Page {0}");
	    MessageFormat footerFormat = new MessageFormat("- {0} -");
	    try {
		table.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
	    } catch (java.awt.print.PrinterException ex) {
		//		    ex.printStackTrace();
		System.err.println("TransactionsView printer exception:"+ex);
	    }
        } else {
            System.out.println("Unhandled item: " + e);
        }
    }
}
