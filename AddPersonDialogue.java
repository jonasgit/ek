
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * dialog to add Person.
 * 
 * @author Jonas Svensson
 */
final class AddPersonDialogue extends JDialog implements ActionListener {
    //TODO: submit to resource bundle for internationalization
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel = null;
    private final WorkingStorage ws;
    private JButton okb;
    private JButton cancelb;
    private JComboBox sexList1;
    private JFormattedTextField yearFTF;
    private JTextField namnField;

    /**
     * AddPersonDialogue constructor comment.
     * 
     * @param _ws WS
     */
    AddPersonDialogue(final WorkingStorage _ws) {
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
            final String sex = "" + sexList1.getSelectedItem();
            System.out.println("Kön: " + sex);
            final String birthyear = "" + yearFTF.getValue();
            System.out.println("Födelseår: " + birthyear);
            System.out.println("Namn: " + namnField.getText());
            ws.addPerson(namnField.getText(),
                    birthyear,
                    sex);
            dispose();
        }
    }

    /**
     * set up the layout and look of the dialog
     * but don't do any data yet.
     */
    private void initialize() {
        setTitle("AddPersonDialogue");
        /*JButton jb;*/
        mainPanel = new JPanel();
        final JPanel northPanel = new JPanel();
        final JPanel centerPanel = new JPanel();
        final JPanel southPanel = new JPanel();

        /* BUILD CENTERPANEL */
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        /* Sex-dropdown */
        final JLabel sexLabel = new JLabel(" Kön ");
        final String[] sexStrings = {"Man", "Kvinna"};

        sexList1 = new JComboBox(sexStrings);
        sexList1.addActionListener(this);
        JPanel jp = new JPanel();
        jp.add(sexLabel);
        jp.add(sexList1);
        centerPanel.add(jp);

        /* FÖDELSEÅR */
        JLabel jff = new JLabel("Födelseår");

        /* TODO: Limit input to 4 digits ranging 1900-currentyear */
        final DecimalFormat decimalFormat = new DecimalFormat("####");
        final NumberFormatter textFormatter = new NumberFormatter(decimalFormat);
        yearFTF = new JFormattedTextField(textFormatter);
        yearFTF.setValue(new Integer(1970));

        jp = new JPanel();
        jp.add(jff);
        jp.add(yearFTF);
        centerPanel.add(jp);
        /* END FÖDELSEÅR */

        /* KOMMENTAR */
        jff = new JLabel("Namn");

        namnField = new JTextField(40);

        jp = new JPanel();
        jp.add(jff);
        jp.add(namnField);
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
