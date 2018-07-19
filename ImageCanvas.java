import java.awt.*;

/** 
 * Class that contains an image and allows it to be cached and added to
 * dialogs as a component.
 */
final class ImageCanvas extends Component {
    private static final long serialVersionUID = 1L;
    private Image image;

// --Recycle Bin START (2003-12-27 18:33):
//	public ImageCanvas() {
//	}
// --Recycle Bin STOP (2003-12-27 18:33)

    ImageCanvas(final Image image) {
		setImage(image);
    }
    
    public void paint(final Graphics g) {
		if(image != null) {
        	g.drawImage(image, 0, 0, this);
		}
    }
    
    public void update(final Graphics g) {
        paint(g);
    }
    
	private void setImage(final Image image) {
        waitForImage(this, image);
		this.image = image;

        setSize(image.getWidth(this), image.getHeight(this));

		if(isShowing()) {
			repaint();
		}
	}
	
	public Dimension getMinimumSize() {
		if(image != null) {
			return new Dimension(image.getWidth(this),
		                     	image.getHeight(this));
		}
		else 
			return new Dimension(0,0);
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

    public static void waitForImage(final Component component,
                                    final Image image) {
        final MediaTracker tracker = new MediaTracker(component);
        try {
            tracker.addImage(image, 0);
            tracker.waitForID(0);
        }
        catch(InterruptedException e) { e.printStackTrace(); }
    }
}
