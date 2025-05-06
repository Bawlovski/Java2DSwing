import java.awt.Color;
import java.awt.Graphics2D;

public class MidleLine {
	
	private Color color = new Color(255,255,255);
	
	private int x;
	private int y;
	private int height;
	private int width;
	
	private Surface surface;
	
	public MidleLine(Surface s,int y) {
		this.surface = s;
		
		this.y = y;
		this.height = surface.getHeight()/30;
		this.width = surface.getWidth()/40;
		this.x = (surface.getWidth() - width )/2  ;
		
	}
	
	public void paint(Graphics2D g) {
        g.setColor(color);
        g.fillRect(x,y,width,height);
        
    }
}
