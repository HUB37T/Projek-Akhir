import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class CustomTextField extends JPanel {
    private JTextField textField;

    public CustomTextField(String iconPath) {
        setLayout(new BorderLayout());
        setOpaque(false);

        try {
            ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setBorder(new EmptyBorder(0, 10, 0, 10)); // Beri padding di sekitar ikon
            add(iconLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.err.println("Ikon tidak ditemukan: " + iconPath);
            add(Box.createHorizontalStrut(30), BorderLayout.WEST);
        }

        textField = new JTextField();
        textField.setFont(new Font("Lato", Font.PLAIN, 16));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.BLACK);
        textField.setOpaque(false);
        textField.setBorder(null);

        add(textField, BorderLayout.CENTER);

        setBorder(BorderFactory.createLineBorder(new Color(218, 165, 32), 1));
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }
}
