import javax.swing.*;
import java.awt.*;

class RoundedButton extends JButton {
    private Color originalBackgroundColor;
    private Color hoverBackgroundColor;

    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {

                if (hoverBackgroundColor != null) {
                    originalBackgroundColor = getBackground();
                    setBackground(hoverBackgroundColor);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {

                setBackground(originalBackgroundColor);
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        originalBackgroundColor = bg;
        hoverBackgroundColor = (bg != null) ? bg.brighter() : null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground()); // Gunakan warna background saat ini
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25); // Radius lengkungan

        super.paintComponent(g2);
        g2.dispose();
    }
}
