import org.w3c.dom.Document;
import org.w3c.dom.Node;

final class Place extends DataStorage {
    private long id=-1;
    private String name="";
    private String tAccount="";
    private String type="";
    private long rAccount=-1;

    Place() {
	//System.out.println("Place constructor");
    }
    
    /** 
     * This method.
     *
     * @param name some string
     * @param number some string
     * @param cred some boolean
     * @param id some long
     */
    Place(final String name,
		 final String number,
		 final boolean cred,
		 final long id) {
	setId(id);
	setName(name);
	setTransferAccount(number);
	if(cred) {
	    setType("cred");
	}
    }
    
    /** 
     * This method.
     *
     * @param node some node
     */
    Place(final Node node) {
	Node TextNode;
 
        for (Node child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if( child.getNodeType() == Node.ELEMENT_NODE) {
		TextNode = child.getFirstChild();
		if((TextNode != null) && (TextNode.getNodeType() == Node.TEXT_NODE)) {
		    if(child.getNodeName().compareToIgnoreCase("Id") == 0)
			id = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Name") == 0)
			name = TextNode.getNodeValue();
		    if(child.getNodeName().compareToIgnoreCase("TAccount") == 0)
			tAccount = TextNode.getNodeValue();
		    if(child.getNodeName().compareToIgnoreCase("Type") == 0)
			type = TextNode.getNodeValue();
		    if(child.getNodeName().compareToIgnoreCase("rAccount") == 0)
			rAccount = Long.parseLong(TextNode.getNodeValue());
		}
	    }
        }
	//System.out.println("New Place created:");
	//System.out.println("id = "+id);
	//System.out.println("Name = "+name);
	//System.out.println("TAccount = "+tAccount);
	//System.out.println("type = "+type);
	//System.out.println("RAccount = "+rAccount);
	//System.out.println("--");
    }

    public void debugDump() {
	System.out.println(id+":"+name+':'+tAccount+':'+type+':'+rAccount);
    }

    public void setId(final long _id) {
	id = _id;
    }

    public void setName(final String _name) {
	if(_name == null) {
	    name = "";
	} else {
	    name = _name;
	}
    }

    public void setTransferAccount(final String _tA) {
	if(_tA == null) {
	    tAccount = "";
	} else {
	    tAccount = _tA;
	}
    }
    public void setType(final String _type) {
	if(_type == null) {
	    type = "";
	} else {
	    type = _type;
	}
    }
    public void setReAccount(final long _rA) {
	rAccount = _rA;
    }


    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

// --Recycle Bin START (2003-12-27 18:33):
//    public final String getTransferAccount() {
//	return tAccount;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)
// --Recycle Bin START (2003-12-27 18:33):
//    public final String getType() {
//	return type;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)
// --Recycle Bin START (2003-12-27 18:33):
//    public final long getReAccount() {
//	return rAccount;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    public void DOMexport(final Document document, final Node root) {
	final Node place;
	
	place = document.createElement("Place");

	addNodeDOM(document, place, "Id", id);
	addNodeDOM(document, place, "Name", name);
	addNodeDOM(document, place, "TAccount", tAccount);
	addNodeDOM(document, place, "Type", type);
	addNodeDOM(document, place, "RAccount", rAccount);
	
	if(type == null) {
	    System.out.println("DOMexport Place:");
	    System.out.println("Id:"+id);
	    System.out.println("Name:"+name);
	    System.out.println("tAccount:"+tAccount);
	    System.out.println("Type:"+type);
	    System.out.println("rAccount:"+rAccount);
	}
	root.appendChild(place);
    }
}

