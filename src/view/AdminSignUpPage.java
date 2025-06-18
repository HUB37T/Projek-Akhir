package view;
import util.CustomTextField;
import controllers.OperatorAdmin;
import util.RoundedButton;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.regex.*;
import javax.swing.border.*;

public class AdminSignUpPage extends JFrame {

    private OperatorAdmin operatorAdmin;
    private CustomTextField emailField;
    private CustomTextField nameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private RoundedButton signupButton;
    private JLabel loginLabel;

    public AdminSignUpPage() {
        initFrame();
        initComponents();
        setupLayout();
        registerEventListeners();

        setVisible(true);
        setAlwaysOnTop(true);
    }

    private void initFrame() {
        setTitle("Admin Sign Up");
        setSize(450, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0x3B1A12));
    }

    private void initComponents() {
        operatorAdmin = new OperatorAdmin();

        nameField = new CustomTextField("assets/icons/user_icon.png");
        emailField = new CustomTextField("assets/icons/email_icon.png");

        passwordField = new JPasswordField();
        styleSimplePasswordField(passwordField);

        confirmPasswordField = new JPasswordField();
        styleSimplePasswordField(confirmPasswordField);

        signupButton = new RoundedButton("Create Account");

        loginLabel = new JLabel("<html>Sudah punya akun? <font color='#DAA520'>Login di sini</font></html>");
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- HEADER ---
        ImageIcon headerIcon = new ImageIcon(new ImageIcon("assets/icons/signup_icon.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
        JLabel iconLabel = new JLabel(headerIcon);
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(iconLabel, gbc);

        JLabel titleLabel = new JLabel("Create Admin Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lato", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 20, 20);
        mainPanel.add(titleLabel, gbc);

        // --- FORM FIELDS ---
        mainPanel.add(createLabel("Nama Lengkap"), gbc(0, 2));
        mainPanel.add(nameField, gbc(0, 3, 2, 1));

        mainPanel.add(createLabel("Email Address"), gbc(0, 4));
        mainPanel.add(emailField, gbc(0, 5, 2, 1));

        mainPanel.add(createLabel("Password"), gbc(0, 6));
        mainPanel.add(passwordField, gbc(0, 7, 2, 1));

        mainPanel.add(createLabel("Confirm Password"), gbc(0, 8));
        mainPanel.add(confirmPasswordField, gbc(0, 9, 2, 1));

        // --- Tombol Sign Up ---
        signupButton.setFont(new Font("Lato", Font.BOLD, 18));
        signupButton.setBackground(new Color(218, 165, 32));
        signupButton.setForeground(Color.BLACK);
        signupButton.setPreferredSize(new Dimension(100, 50));
        mainPanel.add(signupButton, gbc(0, 10, 2, 1, new Insets(20, 20, 10, 20)));

        // --- LINK LOGIN ---
        loginLabel.setFont(new Font("Lato", Font.PLAIN, 14));
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(loginLabel, gbc(0, 11, 2, 1));

        add(mainPanel);
    }

    private void registerEventListeners() {
        signupButton.addActionListener(e -> performSignUp());

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AdminSignInPage();
                dispose();
            }
        });
    }

    private void performSignUp() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Semua field wajib diisi.");
            return;
        }
        if (!isValidEmail(email)) {
            showError("Format email tidak valid.");
            return;
        }
        if (password.length() < 8) {
            showError("Password minimal harus 8 karakter.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Password dan konfirmasi password tidak cocok.");
            return;
        }

        try {
            operatorAdmin.daftarAdmin(email, name, password);
            JOptionPane.showMessageDialog(this, "Admin berhasil terdaftar! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            new AdminHomePage();
            dispose();
        } catch (Exception ex) {
            showError("Gagal mendaftar: " + ex.getMessage());
        }
    }

    // --- Helper Methods ---

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Lato", Font.BOLD, 14));
        label.setForeground(new Color(220, 220, 220));
        return label;
    }

    private void styleSimplePasswordField(JPasswordField pf) {
        pf.setFont(new Font("Lato", Font.PLAIN, 16));
        pf.setForeground(Color.BLACK);
        pf.setBackground(Color.WHITE);
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 120, 120)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        pf.setEchoChar('•');
    }

    private GridBagConstraints gbc(int gridx, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 20, 0, 20);
        return gbc;
    }

    private GridBagConstraints gbc(int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints gbc = gbc(gridx, gridy);
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 20, 10, 20);
        return gbc;
    }

    private GridBagConstraints gbc(int gridx, int gridy, int gridwidth, int gridheight, Insets insets) {
        GridBagConstraints gbc = gbc(gridx, gridy, gridwidth, gridheight);
        gbc.insets = insets;
        return gbc;
    }
}