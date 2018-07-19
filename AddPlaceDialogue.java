
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * dialog to add Person.
 * 
 * @author Jonas Svensson
 */
final class AddPlaceDialogue extends JDialog implements ActionListener {
    //TODO: submit to resource bundle for internationalization
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel = null;
    private final WorkingStorage ws;
    private JButton okb;
    private JButton cancelb;
    private JTextField namnField;
    private JTextField numberField;
    private JCheckBox creditField;

    /**
     * AddPlaceDialogue constructor comment.
     * 
     * @param _ws WS
     */
    AddPlaceDialogue(final WorkingStorage _ws) {
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
            System.out.println("Namn: " + namnField.getText());
            System.out.println("Nummer: " + numberField.getText());
            final boolean cred = creditField.getModel().isSelected();
            System.out.println("Credit: " + cred);
            ws.addPlace(namnField.getText(),
                    numberField.getText(),
                    cred);
            dispose();
        }
    }

    /**
     * set up the layout and look of the dialog
     * but don't do any data yet.
     */
    private void initialize() {
        setTitle("AddPlaceDialogue");
        /*JButton jb;*/
        mainPanel = new JPanel();
        final JPanel northPanel = new JPanel();
        final JPanel centerPanel = new JPanel();
        final JPanel southPanel = new JPanel();

        /* BUILD CENTERPANEL */
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        /* Namn */
        JLabel jff = new JLabel("Namn");

        namnField = new JTextField(40);

        JPanel jp = new JPanel();
        jp.add(jff);
        jp.add(namnField);
        centerPanel.add(jp);
        /* END KOMMENTAR */

        /* Number */
        jff = new JLabel("Gironummer");

        numberField = new JTextField(40);

        jp = new JPanel();
        jp.add(jff);
        jp.add(numberField);
        centerPanel.add(jp);
        /* END Number */

        /* Kreditkind */
        creditField = new JCheckBox("Kontokortsföretag");

        jp = new JPanel();
        jp.add(creditField);
        centerPanel.add(jp);
        /* END Number */

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
