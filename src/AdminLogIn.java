import javax.swing.*;
import java.awt.*;

public class AdminLogIn extends JFrame {

    OperatorAdmin operatorAdmin = new OperatorAdmin();
    public AdminLogIn() {
        setTitle("Admin Log In");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel utama dengan GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x3B1A12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label & Field: Email
        JLabel email = new JLabel("Email");
        email.setBackground(new Color(255, 215, 0, 180));
        email.setForeground(Color.YELLOW);
        email.setFont(new Font("Lato", Font.BOLD, 10));
        email.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 153, 180), 2),
                BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(email, gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Label & Field: Nama
        JLabel nama = new JLabel("Nama");
        nama.setBackground(new Color(255, 215, 0, 180));
        nama.setForeground(Color.YELLOW);
        nama.setFont(new Font("Lato", Font.BOLD, 10));
        nama.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 153, 180), 2),
                BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nama, gbc);

        gbc.gridx = 1;
        JTextField namaField = new JTextField(20);
        panel.add(namaField, gbc);

        // Label & Field: Password
        JLabel password = new JLabel("Password");
        password.setBackground(new Color(255, 215, 0, 180));
        password.setForeground(Color.YELLOW);
        password.setFont(new Font("Lato", Font.BOLD, 10));
        password.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 153, 180), 2),
                BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(password, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Panel tombol
        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        tombolPanel.setBackground(new Color(0x3B1A12));

        JButton logInButton = new JButton("Log In");
        logInButton.setFont(new Font("Lato", Font.BOLD, 15));
        logInButton.setBackground(new Color(218, 165, 32));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Lato", Font.BOLD, 15));
        cancelButton.setBackground(new Color(218, 165, 32));
        tombolPanel.add(logInButton);
        tombolPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(tombolPanel, gbc);

        // Action
        cancelButton.addActionListener(e -> dispose());
        logInButton.addActionListener(e -> {
            String email1 = emailField.getText().trim();
            String nama1 = namaField.getText().trim();
            String password1 = new String(passwordField.getPassword());

            if (email1.isEmpty() || nama1.isEmpty() || password1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    if(operatorAdmin.cekAdmin(email1, nama1, password1)){
                        SwingUtilities.invokeLater(() -> new AdminHomePage().setVisible(true));
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Admin belum terdaftar", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLogIn::new);
    }
}
