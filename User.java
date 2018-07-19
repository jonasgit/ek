import org.w3c.dom.Document;
import org.w3c.dom.Node;

final class User extends DataStorage {
    private long id;
    private String name;
    private String born;
    private String sex;
	
    User() {
	//System.out.println("User constructor");
    }
    
    /** 
     * This method.
     *
     * @param name some string
     * @param year some string
     * @param sex some string
     * @param id some long
     */
    User(final String name,
		final String year,
		final String sex,
		final long id) {
	setName(name);
	setBorn(year);
	setSex(sex);
	setId(id);
    }
    
    /** 
     * This method.
     *
     * @param node some Node
     */
    User(final Node node) {
	Node TextNode;
 
        for (Node child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if( child.getNodeType() == Node.ELEMENT_NODE) {
		TextNode = child.getFirstChild();
		if(TextNode.getNodeType() == Node.TEXT_NODE) {
		    if(child.getNodeName().compareToIgnoreCase("Id") == 0)
			id = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Name") == 0)
			name = TextNode.getNodeValue();
		    if(child.getNodeName().compareToIgnoreCase("Born") == 0)
			born = TextNode.getNodeValue();
		    if(child.getNodeName().compareToIgnoreCase("Sex") == 0)
			sex = TextNode.getNodeValue();
		}
	    }
        }
	//System.out.println("New User created:");
	//System.out.println("id = "+id);
	//System.out.println("name = "+name);
	//System.out.println("born = "+born);
	//System.out.println("sex = "+sex);
	//System.out.println("--");
    }

    public void debugDump() {
	System.out.println(id+":"+name+':'+born+':'+sex);
    }

    public void setId(final long _id) {
	id = _id;
    }

    public void setName(final String _name) {
	name = _name;
    }

    public void setBorn(final String _born) {
	born = _born;
    }

    public void setSex(final String _sex) {
	sex = _sex;
    }

    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

// --Recycle Bin START (2003-12-27 18:33):
//    public final String getBorn() {
//	return born;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

// --Recycle Bin START (2003-12-27 18:33):
//    public final String getSex() {
//	return sex;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    public void DOMexport(final Document document, final Node root) {
	final Node user;
	
	user = document.createElement("User");
	
	addNodeDOM(document, user, "Id", id);
	addNodeDOM(document, user, "Name", name);
	addNodeDOM(document, user, "Born", born);
	addNodeDOM(document, user, "Sex", sex);

	root.appendChild(user);
    }
}

