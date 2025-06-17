package view;

import javax.swing.*;
import java.awt.*;

public class MahasiswaPage extends JFrame {
    private RoundedButton signInButton;
    private RoundedButton signUpButton;

    public MahasiswaPage() {
        initFrame();
        initComponents();
        setupLayout();
        registerEventListeners();

        setVisible(true);
    }

    private void initFrame() {
        setTitle("Portal Mahasiswa");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0x3B1A12));
    }

    private void initComponents() {
        signInButton = new RoundedButton("Sign In");
        styleButton(signInButton, "assets/icons/login_icon.png");

        signUpButton = new RoundedButton("Sign Up");
        styleButton(signUpButton, "assets/icons/signup_icon.png");
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // --- HEADER ---
        try {
            ImageIcon portalIcon = new ImageIcon(new ImageIcon("assets/icons/student_portal_icon.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(portalIcon);
            gbc.gridy = 0;
            mainPanel.add(iconLabel, gbc);
        } catch (Exception e) {
            System.err.println("Ikon mahasiswa tidak ditemukan.");
        }

        // Judul
        JLabel titleLabel = new JLabel("Portal Mahasiswa", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lato", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 20, 10);
        mainPanel.add(titleLabel, gbc);

        // --- Tombol-tombol ---
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(signInButton, gbc);

        gbc.gridy = 3;
        mainPanel.add(signUpButton, gbc);

        add(mainPanel);
    }

    private void registerEventListeners() {
        signInButton.addActionListener(e -> {
            new MahasiswaSignInPage().setVisible(true);
            this.dispose();
        });

        signUpButton.addActionListener(e -> {
            new MahasiswaSignUpPage().setVisible(true);
            this.dispose();
        });
    }

    // Helper method untuk styling tombol agar konsisten
    private void styleButton(RoundedButton button, String iconPath) {
        button.setBackground(new Color(218, 165, 32));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Lato", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(220, 50));

        try {
            ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            button.setIcon(icon);
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setIconTextGap(15);
        } catch (Exception e) {
            System.err.println("Ikon tidak ditemukan: " + iconPath);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MahasiswaPage::new);
    }
}