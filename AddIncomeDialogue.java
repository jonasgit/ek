
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * dialog to add insättning-transaction.
 * 
 * @author Jonas Svensson
 */
final class AddIncomeDialogue extends JDialog implements ActionListener {
    //TODO: submit to resource bundle for internationalization
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel = null;
    private final WorkingStorage ws;
    private JButton okb;
    private JButton cancelb;
    private JComboBox accountList;
    private JComboBox catList;
    private JComboBox userList;
    private DateButton dateb;
    private JFormattedTextField beloppFTF;
    private JTextField commentField;


    /**
     * AddIncomeDialogue constructor comment.
     * 
     * @param _ws WS
     */
    AddIncomeDialogue(final WorkingStorage _ws) {
        super();
        ws = _ws;
        setResizable(false);
        setModal(true);

        initialize();
    }

    /**
     * deal with all the buttons that may be pressed.
     * 
     * @param e java.awt.event.ActionEvent
     */
    public void actionPerformed(final ActionEvent e) {

        if (e.getSource().equals(cancelb)) {
            /* handle cancel-button */
            dispose();
        } else if (e.getSource().equals(okb)) {
            /* handle ok-button */
            System.out.println("OK-button pressed. WIll add transaction...maybe...");
            System.out.println("Konto: " + accountList.getSelectedItem());
            System.out.println("Typ: " + catList.getSelectedItem());
            System.out.println("Vem: " + userList.getSelectedItem());
            System.out.println("Belopp: " + beloppFTF.getValue());
            final String amount = "" + beloppFTF.getValue();
            System.out.println("Belopp: " + amount);
            System.out.println("Text: " + commentField.getText());
            System.out.println("Datum2: " + dateb.getText());
            ws.addTransaction("Insättning",
                    "", /* dummy */
                    (String) accountList.getSelectedItem(),
                    "",
                    dateb.getText(),
                    (String) catList.getSelectedItem(),
                    (String) userList.getSelectedItem(),
                    amount,
                    commentField.getText());
            dispose();
        }
    }

    /**
     * set up the layout and look of the dialog
     * but don't do any data yet.
     */
    private void initialize() {
        setTitle("AddIncomeDialogue");
        /*JButton jb;*/
        mainPanel = new JPanel();
        final JPanel northPanel = new JPanel();
        final JPanel centerPanel = new JPanel();
        final JPanel southPanel = new JPanel();

        /* BUILD CENTERPANEL */
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        /* FrånKonto-dropdown */
        final JLabel kontoLabel = new JLabel(" Till Konto ");
        final ArrayList accountNames = ws.getAccountNames();
        final String[] accountStrings = new String[accountNames.size()];

        int i = 0;
        while (i < accountNames.size()) {
            accountStrings[i] = (String) accountNames.get(i);
            i++;
        }

        accountList = new JComboBox(accountStrings);
        accountList.addActionListener(this);
        JPanel jp = new JPanel();
        jp.add(kontoLabel);
        jp.add(accountList);
        centerPanel.add(jp);

        /* VAD DROPDOWN */
        final JLabel typeLabel = new JLabel("Vad ");
        final ArrayList catNames = ws.getCategories();
        final String[] catStrings = new String[catNames.size()];

        i = 0;
        while (i < catNames.size()) {
            catStrings[i] = (String) catNames.get(i);
            i++;
        }

        catList = new JComboBox(catStrings);
        catList.addActionListener(this);
        jp = new JPanel();
        jp.add(typeLabel);
        jp.add(catList);
        centerPanel.add(jp);
        /* END VAD */

        /* VEM DROPDOWN */
        final JLabel whoLabel = new JLabel("Vem");
        final ArrayList userNames = ws.getUsers();
        final String[] userStrings = new String[userNames.size()];

        i = 0;
        while (i < userNames.size()) {
            userStrings[i] = (String) userNames.get(i);
            i++;
        }

        userList = new JComboBox(userStrings);
        userList.addActionListener(this);
        jp = new JPanel();
        jp.add(whoLabel);
        jp.add(userList);
        centerPanel.add(jp);
        /* END VEM KONTO */
	
        /* DATUM */
        JLabel jff = new JLabel(" Datum");
        dateb = new DateButton();
        jp = new JPanel();
        jp.add(jff);
        jp.add(dateb);
        centerPanel.add(jp);
        /* END DATUM */
	
        /* BELOPP */
        jff = new JLabel("Belopp");

        final NumberFormat currencyFormatter;
        final Locale currentLocale;
        currentLocale = java.util.Locale.getDefault();
        currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
        beloppFTF = new JFormattedTextField(currencyFormatter);
        beloppFTF.setValue(new Double(127.23));

        jp = new JPanel();
        jp.add(jff);
        jp.add(beloppFTF);
        centerPanel.add(jp);
        /* END BELOPP */

        /* KOMMENTAR */
        jff = new JLabel("Kommentar");

        commentField = new JTextField(20);

        jp = new JPanel();
        jp.add(jff);
        jp.add(commentField);
        centerPanel.add(jp);
        /* END KOMMENTAR */

        /*  BUILD SOUTHPANEL */
        okb = new JButton("OK");
        okb.addActionListener(this);
        southPanel.add(okb);

        cancelb = new JButton("Cancel");
        cancelb.addActionListener(this);
        southPanel.add(cancelb);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(northPanel, "North");
        mainPanel.add(centerPanel, "Center");
        mainPanel.add(southPanel, "South");

        this.getContentPane().add(mainPanel);
        setSize(500, 450);
    }
}
