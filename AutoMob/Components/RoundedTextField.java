package Components;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class RoundedTextField extends JTextField {
    private static final int ARC_WIDTH = 20;
    private static final int ARC_HEIGHT = 20;

    public RoundedTextField() {
        super();
        setOpaque(false);
        setBorder(new RoundedBorder());
    }

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(new RoundedBorder());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
        super.paintComponent(g);
        g2.dispose();
    }

    static class RoundedBorder implements Border {
        //@Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(x, y, width - 1, height - 1, ARC_WIDTH, ARC_HEIGHT);
            g2.dispose();
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }

        public boolean isBorderOpaque() {
            return true;
        }
    }
}