import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends WindowAdapter {
    private final JFrame frame;
    private final Surface surface;
    private final Set<Integer> pressedKeys = new HashSet<>(); // Track pressed keys

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
            pressedKeys.add(e.getKeyCode());
            updatePaddleStates();
            
            // Handle pause key separately
            if (e.getKeyCode() == KeyEvent.VK_P) {
                if (surface.isPaused()) {
                    surface.resume();
                } else {
                    surface.pause();
                }
                pressedKeys.remove(KeyEvent.VK_P); // Remove to prevent repeated toggling
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            pressedKeys.remove(e.getKeyCode());
            updatePaddleStates();
        }

        private void updatePaddleStates() {
            // Update left paddle states
            surface.setLeftPaddleMovingUp(pressedKeys.contains(KeyEvent.VK_W));
            surface.setLeftPaddleMovingDown(pressedKeys.contains(KeyEvent.VK_S));
            
            // Update right paddle states
            surface.setRightPaddleMovingUp(pressedKeys.contains(KeyEvent.VK_UP));
            surface.setRightPaddleMovingDown(pressedKeys.contains(KeyEvent.VK_DOWN));
        }
    }
}
