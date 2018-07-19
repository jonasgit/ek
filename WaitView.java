import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * A view to display while working/waiting.
 */
final class WaitView extends JPanel {
    private static final long serialVersionUID = 1L;
    WaitView() {
	final JLabel label2 = new JLabel("Arbetar...");
	add(label2);
    }
}
