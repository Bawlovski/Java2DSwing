import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

public class Surface extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    private Thread t;
    private boolean paused;
    public Ball ball;
    private BufferStrategy bufferStrategy;

    // Paletas
    private Paddles leftPaddle;
    private Paddles rightPaddle;

    public Surface(int w, int h) {
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.BLACK);

        int paddleWidth = 10;
        int paddleHeight = 100;
        double paddleSpeed = 500; // píxeles por segundo

        leftPaddle = new Paddles(this, 30, (h - paddleHeight) / 2, paddleWidth, paddleHeight, paddleSpeed, Color.WHITE);
        rightPaddle = new Paddles(this, w - 30 - paddleWidth, (h - paddleHeight) / 2, paddleWidth, paddleHeight, paddleSpeed, Color.WHITE);
    }

    public void run() {
        // Crear el balón después de que la ventana tenga tamaño válido
        ball = new Ball(this, getWidth() / 2.0, getHeight() / 2.0, 20, Math.PI / 4, 300, Color.WHITE);
        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();

        long t0 = System.nanoTime();

        while (!Thread.currentThread().isInterrupted()) {
            long t1 = System.nanoTime();
            long lapse = t1 - t0;

            synchronized (this) {
                while (paused) {
                    try {
                        wait(); // Espera a que se llame a resume()
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            // Ejecutar lógica y dibujo
            next(lapse);
            drawFrame();

            t0 = t1;

            // Controlar la tasa de frames (opcional)
            long frameTime = System.nanoTime() - t1;
            long sleepTimeNs = Math.max(0, (16_666_666 - frameTime));
            try {
                // Convertir a milisegundos
                Thread.sleep(sleepTimeNs / 1_000_000, (int)(sleepTimeNs % 1_000_000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }



    public void start() {
        t = new Thread(this);
        t.start();
    }

    public void stop() {
        t.interrupt();
        try {
            t.join();
        } catch (InterruptedException e) {
            // Manejar si es necesario
        }
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        if (paused) {
            paused = false;
            notifyAll(); // o notify()
        }
    }


    public synchronized boolean isPaused() {
        return paused;
    }

    private void next(long lapse) {
        ball.move(lapse);

        // Actualizar movimiento de las paletas si se desea movimiento automático.
        // Aquí se controlan mediante teclado en Main.

        // Colisiones
        leftPaddle.checkCollision(ball);
        rightPaddle.checkCollision(ball);
    }

    private void drawFrame() {
        Graphics2D g = null;
        try {
            g = (Graphics2D) bufferStrategy.getDrawGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paint(g);
            if (!bufferStrategy.contentsLost())
                bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync();
        } finally {
            if (g != null)
                g.dispose();
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (ball != null) {
            ball.paint(g2d);
        }

        if (leftPaddle != null) {
            leftPaddle.paint(g2d);
        }
        if (rightPaddle != null) {
            rightPaddle.paint(g2d);
        }
    }

    // Métodos para mover paletas desde Main
    public void moveLeftPaddleUp(long lapse) {
        leftPaddle.moveUp(lapse);
    }

    public void moveLeftPaddleDown(long lapse) {
        leftPaddle.moveDown(lapse);
    }

    public void moveRightPaddleUp(long lapse) {
        rightPaddle.moveUp(lapse);
    }

    public void moveRightPaddleDown(long lapse) {
        rightPaddle.moveDown(lapse);
    }
}

