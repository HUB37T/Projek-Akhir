package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class RoundedButton extends JButton {
    private Color originalBackgroundColor;
    private Color hoverBackgroundColor;

    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverBackgroundColor != null) {
                    RoundedButton.super.setBackground(hoverBackgroundColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                RoundedButton.super.setBackground(originalBackgroundColor);
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        this.originalBackgroundColor = bg;
        this.hoverBackgroundColor = bg.darker();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        super.paintComponent(g2);
        g2.dispose();
    }
}
