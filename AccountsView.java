
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * AccountsView, shows a list of accounts and for each a summary.
 */
final class AccountsView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final ArrayList<JRadioButton> buttons = new ArrayList<JRadioButton>();
    private JButton helpb;
    private final JTextArea textArea;
    private final WorkingStorage ws;

    AccountsView(final WorkingStorage _ws) {
        ws = _ws;

        setLayout(new BorderLayout());

        final String message;

        final JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(0, 1));

        JRadioButton genericButton;
        // Group the radio buttons.
        final ButtonGroup group = new ButtonGroup();

        final ArrayList AccountNames = ws.getAccountNames();

        int i = 0;
        while (i < AccountNames.size()) {
            genericButton = new JRadioButton((String) AccountNames.get(i));
            genericButton.addActionListener(this);
            jp.add(genericButton);
            group.add(genericButton);
            buttons.add(genericButton);
            i++;
        }
        JScrollPane scrollPane = new JScrollPane(jp,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel leftPane = new JPanel();
	leftPane.setLayout(new BorderLayout());
        leftPane.add(scrollPane, BorderLayout.CENTER);
        helpb = new JButton("Help");
        helpb.addActionListener(this);
        leftPane.add(helpb, BorderLayout.SOUTH);

        add(leftPane, BorderLayout.WEST);

        message = "Select account";

        textArea = new JTextArea(message, 4, 20);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(helpb)) {
	    MessageDialogue helpd = new MessageDialogue("AccountsView\nShows a listing of all accounts. Press the button at each account name to see more information about that account.");
	    helpd.setVisible(true);
	} else if (buttons.contains(e.getSource())) {
            final String accountName = ((JRadioButton) e.getSource()).getText();
            String message;
            StringBuffer dateBuffer = new StringBuffer();
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date;

            message = "Valt konto: " + accountName + "\n\n";
            message += "Startsaldo: " + ws.getInitialBalance(accountName) + '\n';
            message += "Dagens saldo: " + ws.getTodayBalance(accountName) + '\n';
            message += "Slutsaldo: " + ws.getTotalBalance(accountName) + "\n\n";
            message += "Antal transaktioner: " + ws.getNumberOfTransactions(accountName) + '\n';
            date = ws.getDateOfOldestTransaction(accountName);
            if (date != null) {
                df.format(date, dateBuffer, new FieldPosition(0));
                message += "Äldsta transaktionen: " + dateBuffer + '\n';
            }
            dateBuffer = new StringBuffer();
            date = ws.getDateOfNewestTransaction(accountName);
            if (date != null) {
                df.format(date, dateBuffer, new FieldPosition(0));
                message += "Sista transaktionen: " + dateBuffer + '\n';
            }

            textArea.setText(message);
        } else {
            System.out.println("Unhandled action: " + e);
        }
    }
}
