import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * dialog to add Konto.
 * @author Jonas Svensson
 */
final class AddAccountDialogue extends JDialog implements ActionListener {
    //TODO: submit to resource bundle for internationalization
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel = null;
    private final WorkingStorage ws;
    private JButton okb;
    private JButton cancelb;
    private JFormattedTextField beloppFTF;
    private JTextField namnField;
    private JTextField nummerField;
    
    /**
     * AddPlaceDialogue constructor comment..
     *
     * @param _ws WS
     */
    AddAccountDialogue(final WorkingStorage _ws) {
	super();
	ws = _ws;
	setResizable(false);
	setModal(true);
	
	initialize();
    }
    /**
     * deal with all the buttons that may be pressed.
     * @param e java.awt.event.ActionEvent
     */
    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(cancelb)){
	    /* handle cancel-button */
	    dispose();
	} else if(e.getSource().equals(okb)){
	    /* handle ok-button */
	    long belopp;
	    try {
		beloppFTF.commitEdit();
		Object foo = beloppFTF.getValue();
		if (foo!=null) {
		    //System.out.println("Trying to parse value: >"+foo+"<");
		    if(foo instanceof Double) {
			Double doubleVal = (Double)foo;
			double dVal = doubleVal.doubleValue();
			dVal *= 100;
			long longVal = (long)dVal;
			belopp = longVal;
		    } else {
			if (foo instanceof Long) {
			    Long lVal = (Long)foo;
			    long longVal = lVal.longValue();
			    belopp = longVal*(long)100;
			} else {
			    System.err.println("Fick en "+ foo.getClass().getName());
			    System.err.println("Kan inte hantera sådana så ingående belopp bli 0.");
			    belopp = 0;
			}
		    }
		} else {
		    belopp = 0;
		}
	    } catch (java.text.ParseException pe) {
		System.out.println("Trouble parsing input. Setting amount to zero.");
		System.out.println(pe);
		belopp = 0;
	    }

	    final int svar = JOptionPane.showConfirmDialog(null,
    			      "Bekräfta att du vill skapa konto enligt:\n"+
			      "Namn: "+namnField.getText()+"\n"+
			      "Kontonummer: "+nummerField.getText()+"\n"+
			      "Ingående saldo: "+belopp+" öre",
							   "Skapa konto?",
							   JOptionPane.OK_CANCEL_OPTION,
							   JOptionPane.QUESTION_MESSAGE);

	    if(svar == 0) {
		System.out.println("Skapar konto:");
		System.out.println("Ingående saldo: "+belopp+" öre");
		System.out.println("Namn: "+namnField.getText());
		System.out.println("Kontonummer: "+nummerField.getText());
		ws.addAccount(namnField.getText(),
			      nummerField.getText(),
			      belopp);
	    }
	    dispose();
	}
    }
    
    /**
     * set up the layout and look of the dialog
     * but don't do any data yet.
     */
    private void initialize() {
	setTitle("AddAccountDialogue");
	/*JButton jb;*/
	mainPanel = new JPanel();
	final JPanel northPanel = new JPanel();
	final JPanel centerPanel = new JPanel();
	final JPanel southPanel = new JPanel();

	/* BUILD CENTERPANEL */
	centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

	/* ACCOUNTNAME */
	JLabel jff = new JLabel("Kontonamn");

	namnField = new JTextField(40);
	
	JPanel jp = new JPanel();
	jp.add(jff);
	jp.add(namnField);
	centerPanel.add(jp);
	/* END ACCOUNTNAME */

	/* ACCOUNTNUMBER */
	jff = new JLabel("Kontonummer");

	nummerField = new JTextField(40);
	
	jp = new JPanel();
	jp.add(jff);
	jp.add(nummerField);
	centerPanel.add(jp);
	/* END ACCOUNTNUMBER */

	/* INITAL BALANCE */
	jff = new JLabel("Ingående saldo");

	final NumberFormat currencyFormatter;
	final Locale currentLocale;
	currentLocale = java.util.Locale.getDefault();
	currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
	beloppFTF = new JFormattedTextField(currencyFormatter);
	//beloppFTF = new JFormattedTextField();
	beloppFTF.setValue(new Double(127.23));
	
	jp = new JPanel();
	jp.add(jff);
	jp.add(beloppFTF);
	centerPanel.add(jp);
	/* END INITAL BALANCE */

	/*  BUILD SOUTHPANEL */
	okb = new JButton("OK");
	okb.addActionListener(this);
	southPanel.add(okb);

	cancelb = new JButton("Cancel");
	cancelb.addActionListener(this);
	southPanel.add(cancelb);
	
	mainPanel.setLayout(new BorderLayout());
	mainPanel.add(northPanel, "North");
	mainPanel.add(centerPanel,"Center");
	mainPanel.add(southPanel, "South");
	
	this.getContentPane().add(mainPanel);
	setSize(500,450);
    }
}
