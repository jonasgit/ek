import org.w3c.dom.Document;
import org.w3c.dom.Node;

final class Category extends DataStorage {
    private long id;
    private boolean income;
    private String name;
	
    Category() {
	//System.out.println("Category constructor");
    }

    /** 
     * This method.
     *
     * @param name some name
     * @param type some type
     * @param id some long id
     */
    Category(final String name, final String type, final long id) {
	setId(id);
	setName(name);
	if(type.equals("Inkomst")) {
	    setIncome("J");
	} else {
	    setIncome("N");
	}
    }

    /** 
     * This method.
     *
     * @param node some node
     */
    Category(final Node node) {
	Node TextNode;
 
        for (Node child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if( child.getNodeType() == Node.ELEMENT_NODE) {
		TextNode = child.getFirstChild();
		if(TextNode.getNodeType() == Node.TEXT_NODE) {
		    if(child.getNodeName().compareToIgnoreCase("Id") == 0)
			id = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Income") == 0)
			income = Boolean.valueOf(TextNode.getNodeValue()).booleanValue();
		    if(child.getNodeName().compareToIgnoreCase("Name") == 0)
			name = TextNode.getNodeValue();
		}
	    }
        }
	//System.out.println("New Category created:");
	//System.out.println("id = "+id);
	//System.out.println("income = "+income);
	//System.out.println("name = "+name);
	//System.out.println("--");
    }

    public void debugDump() {
	System.out.println(id+":"+
			   income+':'+
			   name+':'
			   );
    }
    public void setId(final long _id) {
	id = _id;
    }

    public void setName(final String _name) {
	name = _name;
    }

    public void setIncome(final String _income) {
	income = _income.equals("J");
    }

    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public boolean isIncome() {
	return income;
    }

    public void DOMexport(final Document document, final Node root) {
	final Node category;
	
	category = document.createElement("Category");

	addNodeDOM(document, category, "Id", id);
	addNodeDOM(document, category, "Income", income);
	addNodeDOM(document, category, "Name", name);

	root.appendChild(category);
    }
}

