import java.awt.*;
import javax.swing.JPanel;
import java.awt.event.*;

public class BarChart extends JPanel
{
    private static final long serialVersionUID = 1L;
    /**Sample data, and data names*/
    private double[] data = {200, 140, -100, 60, 40};
    private String[] dataName = {"CS", "Math", "Chem", "Biol", "Phys"};
    private Color[] colors = {Color.red, Color.yellow, Color.green,
			      Color.blue, Color.cyan, Color.magenta, Color.orange, Color.pink,
			      Color.darkGray};
    
    public BarChart() {
	Dimension minimumSize = new Dimension(600,300);
	setMinimumSize(minimumSize);
	setPreferredSize(minimumSize);
    }
    
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	painter(g, 0, 0, getWidth(), getHeight());
    }
    
    public void printComponent(Graphics g) {
	Rectangle clipr = g.getClipBounds();
	painter(g, (int)clipr.getX(), (int)clipr.getY(), (int)clipr.getWidth(), (int)clipr.getHeight());
    }
    
    private void painter(Graphics g, int xpos, int ypos, int width, int height) {
	if (data == null) return;
	
	// Find the maximum value in the data
	double max = data[0];
	for (int i=1; i<data.length; i++)
	    max = Math.max(max, data[i]);
	if(max<0)
	    max=0;

	// Find the minimum value in the data
	double min = data[0];
	for (int i=1; i<data.length; i++)
	    min = Math.min(min, data[i]);
	if(min>0)
	    min=0;

	//int fontheight = g.getFontMetrics().getHeight();
	int fontheight = (int)g.getFontMetrics().getStringBounds("0123456789", g).getHeight();

	double barWidth = ((width - 20.0)/data.length - 10.0);
	int maxBarHeight = height - 30;
	
	if(barWidth<1.0) {
	    barWidth = ((width - 20.0)/data.length);
	    if(barWidth<1.0) {
		barWidth=1.0;
	    }
	}
	int zeroy=height-10-(int)(Math.abs(min)/(max-min)*maxBarHeight);
	g.drawLine(5+xpos, zeroy+ypos, width-5+xpos, zeroy+ypos);
	
	int x = 15;
	for (int i=0; i<data.length; i++)
	    {
		g.setColor(colors[i%colors.length]);
		int newHeight = (int)(maxBarHeight*
				      (Math.abs(data[i])/(max-min)));
		int y;
		if(data[i]<0) {
		    y = zeroy;
		} else {
		    y = zeroy-newHeight;
		}
		g.fillRect(x+xpos, y+ypos, (int)barWidth, newHeight);
		g.setColor(Color.black);
		// Display name if exist
		if(data.length<20) {
		    if ((dataName != null) && (i < dataName.length))
			g.drawString(dataName[i], x+xpos, height-fontheight/2+ypos);
		} else {
		    if ((dataName != null) && (i < dataName.length))
			g.drawString(dataName[i], xpos+15+i*(width/dataName.length), height+ypos);
		}
		if(data.length<20) {
		    g.drawString(Double.toString(data[i]), x+xpos, y - fontheight/2+ypos);
		}
		if(data.length<20) {
		    x += barWidth + 10;
		} else {
		    x = 15+(int)(((double)i)*barWidth);
		}
	    }
	g.drawString("0", 0+xpos, zeroy+ypos+fontheight/2);
	g.drawString(Double.toString(max), 0+xpos, height-10-maxBarHeight+ypos+fontheight/2);
	g.drawString(Double.toString(min), 0+xpos, height-10+ypos+fontheight/2);
    }
    
    /**Set data*/
    public void setData(double[] newData)
    {
	data = newData;
	repaint();
    }
    
    /**Set data names*/
    public void setDataName(String[] newDataName)
    {
	dataName = newDataName;
	repaint();
    }
}

