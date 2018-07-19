import javax.swing.*;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A summary view.
 */
final class SummaryView extends JPanel {
    private static final long serialVersionUID = 1L;
    private final WorkingStorage ws;
    SummaryView(final WorkingStorage _ws) {
	String message;
	StringBuffer dateBuffer;
	final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Date date;
	JLabel label2;

	ws = _ws;

	final JPanel jp2 = new JPanel();
	jp2.setLayout(new BoxLayout(jp2, BoxLayout.Y_AXIS));
	
	label2 = new JLabel("Antal transaktioner: "+ws.getNumberOfTransactions());
	jp2.add(label2);
	label2 = new JLabel("Antal platser      : "+ws.getNumberOfPlaces());
	jp2.add(label2);
	label2 = new JLabel("Antal användare    : "+ws.getNumberOfUsers());
	jp2.add(label2);
	label2 = new JLabel("Antal kategorier   : "+ws.getNumberOfCategories());
	jp2.add(label2);
	label2 = new JLabel("Antal konton       : "+ws.getNumberOfAccounts());
	jp2.add(label2);
	label2 = new JLabel("Antal typer        : "+ws.getNumberOfTypes());
	jp2.add(label2);
	label2 = new JLabel("Antal överföringar : "+ws.getNumberOfTransfers());
	jp2.add(label2);
	label2 = new JLabel("Antal budgetrader  : "+ws.getNumberOfBudgetLines());
	jp2.add(label2);

	message = "Äldsta transaktionen: ";
	dateBuffer = new StringBuffer();
	date=ws.getDateOfOldestTransaction();
	if(date != null) {
	    df.format(date, dateBuffer, new FieldPosition(0));
	    message+=dateBuffer;
	}
	label2 = new JLabel(message);
	jp2.add(label2);

	message="Sista transaktionen: ";
	dateBuffer = new StringBuffer();
	date=ws.getDateOfNewestTransaction();
	if(date != null) {
	    df.format(date, dateBuffer, new FieldPosition(0));
	    message+=dateBuffer;
	}
	label2 = new JLabel(message);
	jp2.add(label2);
  
	add(jp2);
    }
}
