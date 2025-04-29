import java.awt.Color;
import java.awt.Graphics2D;

public class Ball { 
 
    private Surface surface; 
    private int size; 
    private double x; 
    private double y; 
    private double vx; 
    private double vy; 
    private Color color; 
 
    public Ball(Surface surface, double x, double y, int size, double direction, 
                double speed, Color color) { 
        this.x = x; 
        this.y = y; 
        this.size = size; 
        vx = Math.cos(direction) * speed; 
        vy = Math.sin(direction) * speed; 
        this.surface = surface; 
        this.color = color; 
    } 
 
    public void paint(Graphics2D g) { 
        g.setColor(color); 
        g.fillOval((int) x, (int) y, (int) size, size); 
    } 
 
    public void move(long lapse) { 
        x += (lapse * vx) / 1000000000L; 
        y += (lapse * vy) / 1000000000L; 
        if (x + size >= surface.getWidth()) { 
            x = 2 * surface.getWidth() - x - 2 * size; 
            vx *= -1; 
        } else if (x < 0) { 
            x = -x; 
            vx *= -1; 
        } else if (y + size >= surface.getHeight()) { 
            y = 2 * surface.getHeight() - y - 2 * size; 
            vy *= -1; 
        } else if (y < 0) { 
            y = -y; 
            vy *= -1; 
        } 
    } 
} 