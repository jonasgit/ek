import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ArrayList;

/**
 * AddGeneralTransactionsView add any kind of transaction and show
 * last few added transactions.
 */
final class AddGeneralTransactionsView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final DateButton dateSelect;
    private final JComboBox srcAccountList;
    private final JComboBox dstAccountList;
    private final JComboBox tList;
    private final JComboBox placeList;
    private final JComboBox catList;
    private final JComboBox userList;
    private final JFormattedTextField amountFTF;
    private final JTextField commentField;
    private final JButton okb;
    private final JTextArea tLogg;
    private final WorkingStorage ws;
    private final JScrollPane scrollPane;
    
    AddGeneralTransactionsView(final WorkingStorage _ws) {
	ws = _ws;
	
 	setLayout(new BorderLayout());

	// Bygg titeldelen
	JPanel jp = new JPanel();
	final JLabel titleLabel = new JLabel("Add a Transaction");
	jp.add(titleLabel);
	add(jp, BorderLayout.NORTH);

	// Bygg mellandelen
	final JPanel jp2 = new JPanel();
	jp2.setLayout(new BoxLayout(jp2, BoxLayout.X_AXIS));

	/////////////////////////////////////////////////////////
	jp = new JPanel();
	jp.setLayout(new BorderLayout());

	final JLabel tTypLabel = new JLabel("Typ");
	final ArrayList tNames = ws.getTypes();
	final String[] tStrings = new String[tNames.size()];
	
	int i=0;
	while (i < tNames.size()) {
	    tStrings[i] = (String)tNames.get(i);
	    i++;
	}
	
	tList = new JComboBox(tStrings);
	tList.addActionListener(this);
	tList.setPreferredSize(new Dimension(40, 20));
	jp.add(tTypLabel, BorderLayout.NORTH);
	jp.add(tList, BorderLayout.SOUTH);
	jp2.add(jp);
	/////////////////////////////////////////////////////////
	jp = new JPanel();
	jp.setLayout(new BorderLayout());

	final JLabel srcKontoLabel = new JLabel("Från konto ");
	final ArrayList accountNames = ws.getAccountNames();
	final String[] accountStrings = new String[accountNames.size()];
	
	i=0;
	while (i < accountNames.size()) {
	    accountStrings[i] = (String)accountNames.get(i);
	    i++;
	}
	
	srcAccountList = new JComboBox(accountStrings);
	srcAccountList.addActionListener(this);
	srcAccountList.setPreferredSize(new Dimension(50, 20));
	jp.add(srcKontoLabel, BorderLayout.NORTH);
	jp.add(srcAccountList, BorderLayout.SOUTH);
	jp2.add(jp);
	/////////////////////////////////////////////////////////
	jp = new JPanel();
	jp.setLayout(new BorderLayout());

	final JLabel dstKontoLabel = new JLabel("Till konto ");
	dstAccountList = new JComboBox(accountStrings);
	dstAccountList.addActionListener(this);
	dstAccountList.setPreferredSize(new Dimension(50, 20));
	jp.add(dstKontoLabel, BorderLayout.NORTH);
	jp.add(dstAccountList, BorderLayout.SOUTH);
	jp2.add(jp);
	/////////////////////////////////////////////////////////
	jp = new JPanel();
	jp.setLayout(new BorderLayout());
	final JLabel platsLabel = new JLabel("Plats");
	final ArrayList placeNames = ws.getPlaces();
	final String[] placeStrings = new String[placeNames.size()];
	
	i=0;
	while (i < placeNames.size()) {
	    placeStrings[i] = (String)placeNames.get(i);
	    i++;
	}

	placeList = new JComboBox(placeStrings);
	placeList.addActionListener(this);
	placeList.setPreferredSize(new Dimension(50, 20));
	jp.add(platsLabel, BorderLayout.NORTH);
	jp.add(placeList, BorderLayout.SOUTH);
	jp2.add(jp);
	/////////////////////////////////////////////////////////

	jp = new JPanel();
	jp.setLayout(new BorderLayout());

	final JLabel jff = new JLabel("Datum");
    dateSelect = new DateButton();

	jp.add(jff, BorderLayout.NORTH);
	jp.add(dateSelect, BorderLayout.SOUTH);
	jp2.add(jp);

	/////////////////////////////////////////////////////////
	jp = new JPanel();
	jp.setLayout(new BorderLayout());
	final JLabel catLabel = new JLabel("Vad");
	final ArrayList catNames = ws.getCategories();
	final String[] catStrings = new String[catNames.size()];
	
	i=0;
	while (i < catNames.size()) {
	    catStrings[i] = (String)catNames.get(i);
	    i++;
	}

	catList = new JComboBox(catStrings);
	catList.addActionListener(this);
	catList.setPreferredSize(new Dimension(50, 20));

	jp.add(catLabel, BorderLayout.NORTH);
	jp.add(catList, BorderLayout.SOUTH);
	jp2.add(jp);
	/////////////////////////////////////////////////////////
	jp = new JPanel();
	jp.setLayout(new BorderLayout());
	final JLabel userLabel = new JLabel("Vem");

	final ArrayList userNames = ws.getUsers();
	final String[] userStrings = new String[userNames.size()];
	
	i=0;
	while (i < userNames.size()) {
	    userStrings[i] = (String)userNames.get(i);
	    i++;
	}



	userList = new JComboBox(userStrings);
	userList.addActionListener(this);
	userList.setPreferredSize(new Dimension(50, 20));

	jp.add(userLabel, BorderLayout.NORTH);
	jp.add(userList, BorderLayout.SOUTH);
	jp2.add(jp);
	/////////////////////////////////////////////////////////
	final JLabel amountLabel = new JLabel("Belopp");

	final NumberFormat currencyFormatter;
	final Locale currentLocale;
	currentLocale = java.util.Locale.getDefault();
	currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
	amountFTF = new JFormattedTextField(currencyFormatter);
	amountFTF.setValue(new Double(127.23));

	jp = new JPanel();
	jp.setLayout(new BorderLayout());
	amountFTF.setPreferredSize(new Dimension(50, 20));
	jp.add(amountLabel, BorderLayout.NORTH);
	jp.add(amountFTF, BorderLayout.SOUTH);
	jp2.add(jp);
	/////////////////////////////////////////////////////////
	final JLabel commentLabel = new JLabel("Kommentar");
	commentField = new JTextField(20);
	jp = new JPanel();
	jp.setLayout(new BorderLayout());
	commentField.setPreferredSize(new Dimension(50, 20));
	jp.add(commentLabel, BorderLayout.NORTH);
	jp.add(commentField, BorderLayout.SOUTH);
	jp2.add(jp);
	/////////////////////////////////////////////////////////
	jp2.setPreferredSize(new Dimension(1000, 70));
	add(jp2, BorderLayout.CENTER);

	/*  BUILD SOUTHPANEL */
	jp = new JPanel();
	tLogg = new JTextArea("Transaktionslogg\n");
	tLogg.setLineWrap(true);
	tLogg.setEditable(false);
	//tLogg.setPreferredSize(new Dimension(900, 70));
	scrollPane = new JScrollPane(tLogg);
	scrollPane.setPreferredSize(new Dimension(900, 70));
	jp.add(scrollPane);

	okb = new JButton("Add");
	okb.addActionListener(this);
	jp.add(okb);

	add(jp, BorderLayout.SOUTH);
	changeSetup();
    }

    /** A function.
     */
    private void changeSetup() {
	// Borde ersätta strängarna nedan med ws.getType(n) kanske?
	ArrayList catNames=null;

	if(tList.getSelectedItem().equals("Insättning")) {
	    srcAccountList.setEnabled(false);
	    dstAccountList.setEnabled(true);
	    placeList.setEnabled(false);
	    catList.setEnabled(true);
	    catNames = ws.getIncomeCategories();
	}
	if(tList.getSelectedItem().equals("Inköp")) {
	    srcAccountList.setEnabled(true);
	    dstAccountList.setEnabled(false);
	    placeList.setEnabled(true);
	    catList.setEnabled(true);
	    catNames = ws.getOutgoCategories();
	}
	if(tList.getSelectedItem().equals("Uttag")) {
	    srcAccountList.setEnabled(true);
	    dstAccountList.setEnabled(false);
	    placeList.setEnabled(false);
	    catList.setEnabled(false);
	}
	if(tList.getSelectedItem().equals("Överföring")) {
	    srcAccountList.setEnabled(true);
	    dstAccountList.setEnabled(true);
	    placeList.setEnabled(false);
	    catList.setEnabled(false);
	}

	if(catNames != null) {
	    catList.removeAllItems();
	    
	    int i=0;
	    while (i < catNames.size()) {
		catList.addItem(catNames.get(i));
		i++;
	    }
	}
    }

    /** A function.
     * @param e ActionEvent
     */
    public void actionPerformed(final ActionEvent e) {
    if(e.getSource().equals(srcAccountList)) {
	    System.out.println("src account: "+ srcAccountList.getSelectedItem());
	} else if(e.getSource().equals(dstAccountList)) {
	    System.out.println("dst account: "+ dstAccountList.getSelectedItem());
	} else if(e.getSource().equals(tList)) {
	    System.out.println("Typ: "+ tList.getSelectedItem());
	    changeSetup();
	} else if(e.getSource().equals(placeList)) {
	    System.out.println("Plats: "+ placeList.getSelectedItem());
	} else if(e.getSource().equals(catList)) {
	    System.out.println("Vad: "+ catList.getSelectedItem());
	} else if(e.getSource().equals(userList)) {
	    System.out.println("Vem: "+ userList.getSelectedItem());
	} else if(e.getSource().equals(amountFTF)) {
	    System.out.println("Belopp: "+ amountFTF.getValue());
	} else if(e.getSource().equals(commentField)) {
	    System.out.println("Kommentar: "+ commentField.getText());
	} else if(e.getSource().equals(okb)){
	    /* handle ok-button */
	    System.out.println("OK-button pressed...");
	    System.out.println("Typ: "+tList.getSelectedItem());
	    System.out.println("srcKonto: "+srcAccountList.getSelectedItem());
	    System.out.println("dstKonto: "+dstAccountList.getSelectedItem());
	    System.out.println("Plats: "+placeList.getSelectedItem());
	    System.out.println("Date: "+dateSelect.getText());
	    System.out.println("Typ: "+catList.getSelectedItem());
	    System.out.println("Vem: "+userList.getSelectedItem());
	    final String amount = amountFTF.getValue().toString();
	    System.out.println("Belopp: "+amount);
	    System.out.println("Text: "+commentField.getText());
	    ws.addTransaction((String)tList.getSelectedItem(),
			      (String)srcAccountList.getSelectedItem(),
			      (String)dstAccountList.getSelectedItem(),
			      (String)placeList.getSelectedItem(),
			      dateSelect.getText(),
			      (String)catList.getSelectedItem(),
			      (String)userList.getSelectedItem(),
			      amount,
			      commentField.getText());
	    tLogg.append(
			 (String)tList.getSelectedItem()+';'+
			 (String)srcAccountList.getSelectedItem()+';'+
			 (String)dstAccountList.getSelectedItem()+';'+
			 (String)placeList.getSelectedItem()+';'+
			 dateSelect.getText()+';'+
			 (String)catList.getSelectedItem()+';'+
			 (String)userList.getSelectedItem()+';'+
			 amount+';'+
			 commentField.getText()+'\n');

	} else {
	    System.out.println("Unhandled item: "+e);
	}
    }
}
