import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminLogIn extends JFrame {

    private OperatorAdmin operatorAdmin;
    private CustomTextField emailField;
    private CustomTextField nameField;
    private JPasswordField passwordField;
    private RoundedButton loginButton;
    private JLabel signupLabel;

    public AdminLogIn() {
        initFrame();
        initComponents();
        setupLayout();
        registerEventListeners();

        setVisible(true);
    }

    private void initFrame() {
        setTitle("Admin Log In");
        setSize(450, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0x3B1A12));
    }


    private void initComponents() {
        operatorAdmin = new OperatorAdmin();

        emailField = new CustomTextField("email_icon.png");
        nameField = new CustomTextField("user_icon.png");

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Lato", Font.PLAIN, 16));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 120, 120)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        passwordField.setEchoChar('•');

        loginButton = new RoundedButton("Log In");

        signupLabel = new JLabel("<html>Belum punya akun? <font color='#DAA520'>Daftar di sini</font></html>");
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon headerIcon = new ImageIcon(new ImageIcon("admin_login_icon.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
        JLabel iconLabel = new JLabel(headerIcon);
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(iconLabel, gbc);

        JLabel titleLabel = new JLabel("Admin Log In", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lato", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 20, 20);
        mainPanel.add(titleLabel, gbc);

        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridwidth = 2;

        mainPanel.add(createLabel("Email Address"), gbc(0, 2, 2, 1));
        mainPanel.add(emailField, gbc(0, 3, 2, 1));

        mainPanel.add(createLabel("Nama"), gbc(0, 4, 2, 1));
        mainPanel.add(nameField, gbc(0, 5, 2, 1));

        mainPanel.add(createLabel("Password"), gbc(0, 6, 2, 1));

        mainPanel.add(passwordField, gbc(0, 7, 2, 1));

        loginButton.setFont(new Font("Lato", Font.BOLD, 18));
        loginButton.setBackground(new Color(218, 165, 32));
        loginButton.setForeground(Color.BLACK);
        loginButton.setPreferredSize(new Dimension(100, 50));
        mainPanel.add(loginButton, gbc(0, 8, 2, 1, new Insets(20, 20, 10, 20)));

        signupLabel.setFont(new Font("Lato", Font.PLAIN, 14));
        signupLabel.setForeground(Color.WHITE);
        signupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(signupLabel, gbc(0, 9, 2, 1));

        add(mainPanel);
    }

    private void registerEventListeners() {
        loginButton.addActionListener(e -> performLogin());

        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AdminSignUp();
                dispose();
            }
        });

    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email, Nama, dan Password wajib diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (operatorAdmin.cekAdmin(email, name, password)) {
                JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Datang.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                new AdminHomePage();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak cocok atau admin tidak terdaftar.", "Gagal Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Helper Methods ---

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Lato", Font.BOLD, 14));
        label.setForeground(new Color(220, 220, 220));
        return label;
    }

    private GridBagConstraints gbc(int gridx, int gridy, int gridwidth, int gridheight, Insets... insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = (insets.length > 0) ? insets[0] : new Insets(5, 20, 10, 20);
        return gbc;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLogIn::new);
    }
}

