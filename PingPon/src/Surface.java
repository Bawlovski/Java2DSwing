import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Surface extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    private Thread t;
    private boolean paused;
    public Ball ball;
    private BufferStrategy bufferStrategy;

    // Paletas
    private Paddles leftPaddle;
    private Paddles rightPaddle;
    private boolean leftPaddleMovingUp = false;
    private boolean leftPaddleMovingDown = false;
    private boolean rightPaddleMovingUp = false;
    private boolean rightPaddleMovingDown = false;
    
    //Middle Line
    private ArrayList<MidleLine> middleline = new ArrayList<MidleLine>();
    
    //TEXTS 
    private Text2D text1;
    private Text2D text2 ;
    private int score1 = 0;
    private int score2 = 0;
    
    public Surface(int w, int h) {
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.BLACK);

        int paddleWidth = 10;
        int paddleHeight = 100;
        double paddleSpeed = 250; // píxeles por segundo

        leftPaddle = new Paddles(this, 30, (h - paddleHeight) / 2, paddleWidth, paddleHeight, paddleSpeed, Color.WHITE);
        rightPaddle = new Paddles(this, w - 30 - paddleWidth, (h - paddleHeight) / 2, paddleWidth, paddleHeight, paddleSpeed, Color.WHITE);
        
    }

    public void run() {
        // Crear el balón después de que la ventana tenga tamaño válido
        ball = new Ball(this, getWidth() / 2.0, getHeight() / 2.0, 20, Math.PI / 4, 300, Color.WHITE);
        
        text1 =  new Text2D(this,this.getWidth() / 4);
        text2 =  new Text2D(this,this.getWidth() - this.getWidth() / 4);
        
        
        
        for(int i = 0; i<= 15; i++) {
            middleline.add(new MidleLine(this , i * 43));
        }

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

        // Handle continuous paddle movement
        if (leftPaddleMovingUp && !leftPaddleMovingDown) {
            leftPaddle.moveUp(lapse);
        } else if (leftPaddleMovingDown && !leftPaddleMovingUp) {
            leftPaddle.moveDown(lapse);
        }

        if (rightPaddleMovingUp && !rightPaddleMovingDown) {
            rightPaddle.moveUp(lapse);
        } else if (rightPaddleMovingDown && !rightPaddleMovingUp) {
            rightPaddle.moveDown(lapse);
        }

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
        
        text1.setDisplayText(Integer.toString(score1));
        text2.setDisplayText(Integer.toString(score2));
        
        text1.paint(g2d);
        text2.paint(g2d);
        
        middleline.forEach((middle) ->{
        	middle.paint(g2d);
        });
        
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
    
    public void setScore1() {
    	score1++;
    }
    public void setScore2() {
    	score2++;
    }
    
 // Add these setter methods
    public void setLeftPaddleMovingUp(boolean moving) {
        this.leftPaddleMovingUp = moving;
    }

    public void setLeftPaddleMovingDown(boolean moving) {
        this.leftPaddleMovingDown = moving;
    }

    public void setRightPaddleMovingUp(boolean moving) {
        this.rightPaddleMovingUp = moving;
    }

    public void setRightPaddleMovingDown(boolean moving) {
        this.rightPaddleMovingDown = moving;
    }
}

