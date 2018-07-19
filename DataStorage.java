import java.text.SimpleDateFormat;
import java.text.FieldPosition;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.Date;

class DataStorage {
    /** 
     * This method.
     *
     * @param document some docuemtn
     * @param root some node
     * @param name string
     * @param value some date
     */
    static void addNodeDOM(final Document document, final Node root, final String name, final String value) {
	final Node node;
	
	if(value != null) {
	    if(name != null) {
		node = document.createElement(name);
		node.appendChild(document.createTextNode(value));
		root.appendChild(node);
	    } else {
		System.err.println("AddNodeDOM called with nullname for value "+value);
		Thread.dumpStack();
	    }
	} else {
	    if(name == null) {
		System.err.println("AddNodeDOM called with nullvalue and nullname");
		Thread.dumpStack();
	    } else {
		System.err.println("AddNodeDOM called with nullvalue, for name: "+name);
		Thread.dumpStack();
	    }
	}	
    }

    /** 
     * This method.
     *
     * @param document some document
     * @param root some node
     * @param name some string
     * @param value some long
     */
    static void addNodeDOM(final Document document, final Node root, final String name, final long value) {
	addNodeDOM(document, root, name, ""+value);
    }

    /** 
     * This method.
     *
     * @param document some document
     * @param root some node
     * @param name some string
     * @param value some boolean
     */
    static void addNodeDOM(final Document document, final Node root, final String name, final boolean value) {
	addNodeDOM(document, root, name, ""+value);
    }

    /** 
     * This method.
     *
     * @param document some docuemtn
     * @param root some node
     * @param name some string
     * @param value some date
     */
    static void addNodeDOM(final Document document, final Node root, final String name, final Date value) {
	final StringBuffer dateBuffer;

	if(value != null) {
	    dateBuffer = new StringBuffer();
	    final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    df.format(value, dateBuffer, new FieldPosition(0));
	    
	    addNodeDOM(document, root, name, new String(dateBuffer));
	}
    }
}
