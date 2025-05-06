import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Text2D {
	
	Color color = new Color(255,255,255);
	private Surface surface ;
	private int x;
	private String score;
	
	public Text2D(Surface s,int x) {
		this.surface =  s;
		this.x = x - 24;
	}
	
	
	public void paint(Graphics2D g) {
        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 72));
        g.drawString(score, x, surface.getHeight() / 4);
        
    }
	
    public void setDisplayText(String newText) {
        this.score = newText;
    }
}
