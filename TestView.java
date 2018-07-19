
import javax.swing.*;
import java.awt.*;

/**
 * Surface is the base class for the 2d rendering demos.  Demos must
 * implement the render() method. Subclasses for Surface are
 * AnimatingSurface, ControlsSurface and AnimatingControlsSurface.
 */
public final class TestView extends JPanel {
    private static final long serialVersionUID = 1L;
    /**
     * default constructor.
     */
    public TestView() {
        setLayout(new BorderLayout());

        final JLabel label = new JLabel("Drag within the framed area.");
        add(label, BorderLayout.NORTH);

        final JLabel label2 = new JLabel("Another label.");
        add(label2, BorderLayout.EAST);

        final JPanel jp = new JPanel();


        final JTextField jff = new JTextField("The date field will hold the result.");
        final DateButton jb = new DateButton();

        jp.add(jff, BorderLayout.CENTER);
        jp.add(jb, BorderLayout.SOUTH);
        add(jp, BorderLayout.SOUTH);
    }
}
