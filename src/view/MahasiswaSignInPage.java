package view;
import util.CustomTextField;
import controllers.OperatorMahasiswa;
import util.RoundedButton;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class MahasiswaSignInPage extends JFrame {
    private OperatorMahasiswa operator;
    private CustomTextField nimField;
    private CustomTextField nameField;
    private JPasswordField passwordField;
    private CustomTextField prodiField;
    private RoundedButton loginButton;
    private JLabel signupLabel;

    public MahasiswaSignInPage() {
        initFrame();
        initComponents();
        setupLayout();
        registerEventListeners();

        setVisible(true);
        setAlwaysOnTop(true);
    }

    private void initFrame() {
        setTitle("Mahasiswa - Sign In");
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0x3B1A12));
    }

    //Field untuk input
    private void initComponents() {
        operator = new OperatorMahasiswa();

        nimField = new CustomTextField("assets/icons/nim_icon.png");
        nameField = new CustomTextField("assets/icons/user_icon.png");
        prodiField = new CustomTextField("assets/icons/prodi_icon.png");

        passwordField = new JPasswordField();
        stylePasswordField(passwordField);

        loginButton = new RoundedButton("Login");

        signupLabel = new JLabel("<html>Belum punya akun? <font color='#DAA520'>Daftar di sini</font></html>");
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- HEADER ---
        ImageIcon headerIcon = new ImageIcon(new ImageIcon("assets/icons/student_login_icon.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
        JLabel iconLabel = new JLabel(headerIcon);
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(iconLabel, gbc);

        JLabel titleLabel = new JLabel("Mahasiswa Sign In", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lato", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 20, 20);
        mainPanel.add(titleLabel, gbc);

        // --- FORM FIELDS
        mainPanel.add(createLabel("NIM (Nomor Induk Mahasiswa)"), gbc(0, 2));
        mainPanel.add(nimField, gbc(0, 3, 2, 1));

        mainPanel.add(createLabel("Nama Lengkap"), gbc(0, 4));
        mainPanel.add(nameField, gbc(0, 5, 2, 1));

        mainPanel.add(createLabel("Password"), gbc(0, 6));
        mainPanel.add(passwordField, gbc(0, 7, 2, 1));

        mainPanel.add(createLabel("Program Studi"), gbc(0, 8));
        mainPanel.add(prodiField, gbc(0, 9, 2, 1));

        // --- Tombol Login ---
        loginButton.setFont(new Font("Lato", Font.BOLD, 18));
        loginButton.setBackground(new Color(218, 165, 32));
        loginButton.setForeground(Color.BLACK);
        loginButton.setPreferredSize(new Dimension(100, 50));
        mainPanel.add(loginButton, gbc(0, 10, 2, 1, new Insets(25, 20, 15, 20)));

        // --- LINK SIGN UP ---
        signupLabel.setFont(new Font("Lato", Font.PLAIN, 14));
        signupLabel.setForeground(Color.WHITE);
        signupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(signupLabel, gbc(0, 11, 2, 1));

        add(mainPanel);
    }

    private void registerEventListeners() {
        loginButton.addActionListener(e -> {
            String nim = nimField.getText().trim();
            String nama = nameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String prodi = prodiField.getText().trim();

            if (nim.isEmpty() || nama.isEmpty() || password.isEmpty() || prodi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    if (operator.cekMahasiswa(nim, nama, password, prodi)) {
                        SwingUtilities.invokeLater(() -> new MahasiswaHomePage());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Data tidak cocok atau mahasiswa belum terdaftar.", "Gagal Login", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Terjadi error saat login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MahasiswaSignUpPage();
                dispose();
            }
        });
    }

    // --- Helper Methods ---
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Lato", Font.BOLD, 14));
        label.setForeground(new Color(220, 220, 220));
        return label;
    }

    private void stylePasswordField(JPasswordField pf) {
        pf.setFont(new Font("Lato", Font.PLAIN, 16));
        pf.setForeground(Color.BLACK);
        pf.setBackground(Color.WHITE);
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 120, 120)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        pf.setEchoChar('•');
    }

    private GridBagConstraints gbc(int gridx, int gridy, int... gridwh) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = (gridwh.length > 0) ? gridwh[0] : 1;
        gbc.gridheight = (gridwh.length > 1) ? gridwh[1] : 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 20, 5, 20);
        return gbc;
    }

    private GridBagConstraints gbc(int gridx, int gridy, int gridwidth, int gridheight, Insets insets) {
        GridBagConstraints gbc = gbc(gridx, gridy, gridwidth, gridheight);
        gbc.insets = insets;
        return gbc;
    }
}