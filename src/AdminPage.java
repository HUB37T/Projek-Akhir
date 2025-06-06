import javax.swing.*;
import java.awt.*;

public class AdminPage extends JFrame {

    public AdminPage() {
        setTitle("Halaman Admin");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(new Color(0x3B1A12));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x3B1A12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Baris 4 - Dua tombol (Simpan dan Kembali)
        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        tombolPanel.setBackground(new Color(0x3B1A12));
        JButton logButton = new JButton("Log In");
        logButton.setPreferredSize(new Dimension(140, 35));
        logButton.setFont(new Font("Lato", Font.BOLD, 15));
        logButton.setBackground(new Color(218, 165, 32));
        logButton.setForeground(Color.BLACK);
        logButton.setFocusPainted(false);
        logButton.setBorder(BorderFactory.createLineBorder(new Color(255, 223, 0), 2));
        JButton signButton = new JButton("Sign Up");
        signButton.setPreferredSize(new Dimension(140, 35));
        signButton.setFont(new Font("Lato", Font.BOLD, 15));
        signButton.setBackground(new Color(218, 165, 32));
        signButton.setForeground(Color.BLACK);
        signButton.setFocusPainted(false);
        signButton.setBorder(BorderFactory.createLineBorder(new Color(255, 223, 0), 2));
        tombolPanel.add(logButton);
        tombolPanel.add(signButton);

        signButton.addActionListener(e -> new AdminSignUp());
        logButton.addActionListener(e -> new AdminLogIn());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(tombolPanel, gbc);

        setContentPane(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPage::new);
    }
}

