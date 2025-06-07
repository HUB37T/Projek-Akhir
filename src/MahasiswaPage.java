
import javax.swing.*;
import java.awt.*;

public class MahasiswaPage extends JFrame {

    public MahasiswaPage() {
        setTitle("Halaman Mahasiswa");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel background dengan warna solid maroon kayu gelap
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(0x4A1C1A)); // warna dari tone gambar
        backgroundPanel.setLayout(new GridBagLayout());

        // Panel konten
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(255, 255, 255, 210)); // putih semi-transparan
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        JLabel title = new JLabel("Mahasiswa");
        title.setFont(new Font("Lato", Font.BOLD, 32));
        title.setForeground(new Color(111, 0, 44)); // maroon tua untuk judul
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton signInBtn = createMaroonButton("Sign In");
        JButton signUpBtn = createMaroonButton("Sign Up");

        signInBtn.addActionListener(e -> {
            new MahasiswaSignInPage().setVisible(true);
            this.dispose();});
        signUpBtn.addActionListener(e -> {
            new MahasiswaSignUpPage().setVisible(true);
            this.dispose();});

        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(signInBtn);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(signUpBtn);

        backgroundPanel.add(contentPanel);
        add(backgroundPanel);
        setVisible(true);
    }

    private JButton createMaroonButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Lato", Font.BOLD, 18));
        button.setBackground(new Color(111, 0, 44)); // maroon gelap
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 50));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MahasiswaPage::new);
    }
}
