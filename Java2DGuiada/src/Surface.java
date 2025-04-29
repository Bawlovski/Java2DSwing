import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class Surface extends JComponent { 
    private static final long serialVersionUID = 1L; 
    private Thread t; 
    private boolean paused; 
    private Ball ball; 
    private BufferedImage buffer; 
    private Graphics2D g; 
 
    public Surface(int w, int h) { 
        setPreferredSize(new Dimension(w, h)); 
        setBackground(Color.BLACK); 
        ball = new Ball(this, (w - 15) / 2d, (h - 15) / 2d, 15, 45d, 300d, Color.WHITE); 
        buffer = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_ARGB); 
        g = (Graphics2D) buffer.getGraphics(); 
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
    } 
 
    private void run() { 
        long t0 = System.nanoTime(), t1; 
        while (!Thread.currentThread().isInterrupted()) { 
            synchronized (this) { 
                if (paused) { 
                    try { 
                        wait(); 
                    } catch (InterruptedException e) { 
                        Thread.currentThread().interrupt(); 
                    } 
                    t0 = System.nanoTime(); 
                } 
            } 
            next((t1 = System.nanoTime()) - t0); 
            drawFrame(); 
            t0 = t1; 
        } 
    } 
 
    public void start() { 
        t = new Thread(this::run); 
        t.start(); 
    } 
 
    public void stop() { 
        t.interrupt(); 
        try { 
            t.join(); 
        } catch (InterruptedException e) { 
        } 
    } 
 
    public synchronized void pause() { 
        paused = true; 
    } 
 
    public synchronized void resume() { 
        if (paused) { 
            paused = false; 
            notify(); 
        } 
    } 
 
    public synchronized boolean isPaused() { 
        return paused; 
    } 
 
    private void next(long lapse) { 
        ball.move(lapse); 
    } 
 
    private void drawFrame() { 
        Graphics g = getGraphics(); 
        paintComponent(g); 
        g.dispose(); 
    } 
 
    @Override 
    protected void paintComponent(Graphics g) { 
        // dibujar el frame en el buffer 
        this.g.setColor(getBackground()); 
        this.g.fillRect(0, 0, getWidth(), getHeight()); 
        ball.paint(this.g); 
        // dibujar el buffer en la pantalla 
        g.drawImage(buffer, 0, 0, this); 
    } 
}