import java.awt.Color;
import java.awt.Graphics2D;

public class Ball {
    public double x;
    public double y;
    public int size;
    public double vx;
    public double vy;
    private Color color;
    private Surface surface; // Añadido

    public Ball(Surface surface) {
        this.surface = surface; // Asignación
        int dim = Math.min(surface.getWidth(), surface.getHeight());
        int minSize = (int) (dim * 0.05);
        int maxSize = (int) (dim * 0.6);
        size = (int) (Math.random() * (maxSize - minSize) + minSize);
        x = (surface.getWidth() - size) / 2.0;
        y = (surface.getHeight() - size) / 2.0;
        double speed = Math.random() * 450 + 50;
        double direction = Math.random() * 2 * Math.PI;
        vx = Math.cos(direction) * speed;
        vy = Math.sin(direction) * speed;
        color = new Color(
                (float) Math.random(),
                (float) Math.random(),
                (float) Math.random(),
                1);
        
    }

    public Ball(Surface surface, double x, double y, int size, double direction, double speed, Color color) {
        this.surface = surface; // Añadido
        this.x = x;
        this.y = y;
        this.size = size;
        this.vx = Math.cos(direction) * speed;
        this.vy = Math.sin(direction) * speed;
        this.color = color;
    }

    public void paint(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int) x, (int) y, size, size);
    }

    public void move(long lapse) {
        double deltaSeconds = lapse / 1_000_000_000.0;
        x += deltaSeconds * vx;
        y += deltaSeconds * vy;

        // Colisiones con borde - eje X
        if (x + size >= surface.getWidth()) {
            x = surface.getWidth() - size;
            vx *= -1;
            surface.setScore1();
            this.x = surface.getWidth() / 2.0;
            this.y = surface.getHeight() / 2.0;
        } else if (x < 0) {
            x = 0;
            vx *= -1;
            surface.setScore2();
            this.x = surface.getWidth() / 2.0;
            this.y = surface.getHeight() / 2.0;
        }

        // eje Y
        if (y + size >= surface.getHeight()) {
            y = surface.getHeight() - size;
            vy *= -1;
        } else if (y < 0) {
            y = 0;
            vy *= -1;
        }
    }

}

