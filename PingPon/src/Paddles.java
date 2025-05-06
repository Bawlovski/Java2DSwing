import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Paddles {
    private Surface surface;
    private int width;
    private int height;
    private double x;
    private double y;
    private double speed; // píxeles por segundo
    private Color color;

    public Paddles(Surface surface, double x, double y, int width, int height, double speed, Color color) {
        this.surface = surface;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    public void moveUp(long lapse) {
        double deltaY = (lapse / 1_000_000_000.0) * speed;
        y -= deltaY;
        if (y < 0) y = 0;
    }

    public void moveDown(long lapse) {
        double deltaY = (lapse / 1_000_000_000.0) * speed;
        y += deltaY;
        if (y + height > surface.getHeight()) {
            y = surface.getHeight() - height;
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, width, height);
    }

    public void checkCollision(Ball ball) {
        Rectangle paddleRect = new Rectangle((int) x, (int) y, width, height);
        Rectangle ballRect = new Rectangle((int) ball.x, (int) ball.y, ball.size, ball.size);
        if (paddleRect.intersects(ballRect)) {
            ball.vx *= -1;
            // Opcional: ajustar dirección en función del punto de impacto
        }
    }

    // Getters si son necesarios
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

