package view;

import javax.swing.*;
import java.awt.*;

public class AdminPage extends JFrame {

    private RoundedButton loginButton;
    private RoundedButton signupButton;

    public AdminPage() {
        initFrame();
        initComponents();
        setupLayout();
        registerEventListeners();

        setVisible(true);
    }

    private void initFrame() {
        setTitle("Panel Administrasi");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        loginButton = new RoundedButton("Log In");
        styleButton(loginButton);

        signupButton = new RoundedButton("Sign Up");
        styleButton(signupButton);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(0x3B1A12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        try {
            ImageIcon adminIcon = new ImageIcon(new ImageIcon("assets/icons/admin_icon.png").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(adminIcon);
            gbc.gridx = 0;
            gbc.gridy = 0;
            mainPanel.add(iconLabel, gbc);
        } catch (Exception e) {
            System.out.println("Ikon admin tidak ditemukan, melanjutkan tanpa ikon.");
        }

        JLabel titleLabel = new JLabel("Panel Administrasi");
        titleLabel.setFont(new Font("Lato", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(titleLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(buttonPanel, gbc);

        setContentPane(mainPanel);
    }

    private void registerEventListeners() {
        loginButton.addActionListener(e -> {
            new AdminLogIn();
            this.dispose();
        });

        signupButton.addActionListener(e -> {
            new AdminSignUp();
            this.dispose();
        });
    }

    private void styleButton(RoundedButton button) {
        button.setBackground(new Color(218, 165, 32));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Lato", Font.BOLD, 15));
        button.setPreferredSize(new Dimension(140, 40));
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPage::new);
    }
}