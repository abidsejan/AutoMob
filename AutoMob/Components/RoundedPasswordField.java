package Components;

import javax.swing.*;
import java.awt.*;

public class RoundedPasswordField extends JPasswordField {
    private static final int ARC_WIDTH = 20;
    private static final int ARC_HEIGHT = 20;

    public RoundedPasswordField() {
        super();
        setOpaque(false);
        setBorder(new RoundedTextField.RoundedBorder());
    }

    public RoundedPasswordField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(new RoundedTextField.RoundedBorder());
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
}