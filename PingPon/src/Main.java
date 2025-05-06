import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends WindowAdapter {
    private final JFrame frame;
    private final Surface surface;

    public Main() {
        frame = new JFrame("Bola Rebotando");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);
        surface = new Surface(650, 450);
        frame.add(surface);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(new MyKeyAdapter());
        
        
    }

    public void iniciar() {
        frame.setVisible(true);
        surface.start();
    }

    @Override
    public void windowClosing(java.awt.event.WindowEvent e) {
        surface.stop();
        frame.dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().iniciar());
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            long lapse = 16_666_666; // aproximadamente 60 fps
            switch (e.getKeyCode()) {
                case KeyEvent.VK_P:
                    if (surface.isPaused()) {
                        surface.resume();
                    } else {
                        surface.pause();
                    }
                    break;
                // Controles para paletas
                case KeyEvent.VK_W:
                    surface.moveLeftPaddleUp(lapse);
                    break;
                case KeyEvent.VK_S:
                    surface.moveLeftPaddleDown(lapse);
                    break;
                case KeyEvent.VK_UP:
                    surface.moveRightPaddleUp(lapse);
                    break;
                case KeyEvent.VK_DOWN:
                    surface.moveRightPaddleDown(lapse);
                    break;
            }
        }
    }
}
