import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MahasiswaSignUpPage extends JFrame {
    private OperatorMahasiswa operatorMahasiswa;
    private CustomTextField nimField;
    private CustomTextField nameField;
    private CustomTextField prodiField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private RoundedButton signupButton;
    private JLabel loginLabel;

    public MahasiswaSignUpPage() {
        initFrame();
        initComponents();
        setupLayout();
        registerEventListeners();

        setVisible(true);
    }

    private void initFrame() {
        setTitle("Mahasiswa - Sign Up");
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0x3B1A12));
    }

    private void initComponents() {
        operatorMahasiswa = new OperatorMahasiswa();
        nimField = new CustomTextField("nim_icon.png");
        nameField = new CustomTextField("user_icon.png");
        prodiField = new CustomTextField("prodi_icon.png");

        passwordField = new JPasswordField();
        stylePasswordField(passwordField);

        confirmPasswordField = new JPasswordField();
        stylePasswordField(confirmPasswordField);

        signupButton = new RoundedButton("Daftar Akun");

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
        ImageIcon headerIcon = new ImageIcon(new ImageIcon("signup_icon.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
        JLabel iconLabel = new JLabel(headerIcon);
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(iconLabel, gbc);

        JLabel titleLabel = new JLabel("Pendaftaran Mahasiswa", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lato", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 20, 20);
        mainPanel.add(titleLabel, gbc);

        // --- FORM FIELDS ---
        mainPanel.add(createLabel("NIM (Nomor Induk Mahasiswa)"), gbc(0, 2));
        mainPanel.add(nimField, gbc(0, 3, 2, 1));

        mainPanel.add(createLabel("Nama Lengkap"), gbc(0, 4));
        mainPanel.add(nameField, gbc(0, 5, 2, 1));

        mainPanel.add(createLabel("Program Studi"), gbc(0, 6));
        mainPanel.add(prodiField, gbc(0, 7, 2, 1));

        mainPanel.add(createLabel("Password"), gbc(0, 8));
        mainPanel.add(passwordField, gbc(0, 9, 2, 1));

        mainPanel.add(createLabel("Konfirmasi Password"), gbc(0, 10));
        mainPanel.add(confirmPasswordField, gbc(0, 11, 2, 1));

        // --- Tombol Daftar ---
        signupButton.setFont(new Font("Lato", Font.BOLD, 18));
        signupButton.setBackground(new Color(218, 165, 32));
        signupButton.setForeground(Color.BLACK);
        signupButton.setPreferredSize(new Dimension(100, 50));
        mainPanel.add(signupButton, gbc(0, 12, 2, 1, new Insets(25, 20, 15, 20)));

        // --- LINK LOGIN ---
        loginLabel.setFont(new Font("Lato", Font.PLAIN, 14));
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(loginLabel, gbc(0, 13, 2, 1));

        add(mainPanel);
    }

    private void registerEventListeners() {
        signupButton.addActionListener(e -> {
            String nim = nimField.getText().trim();
            String nama = nameField.getText().trim();
            String prodi = prodiField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (password.length() < 8) {
                showError("Password minimal harus 8 karakter.");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showError("Password dan konfirmasi password tidak cocok.");
                return;
            }

            if (nim.isEmpty() || nama.isEmpty() || prodi.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Password dan Konfirmasi Password tidak cocok.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                operatorMahasiswa.daftarMahasiswa(nim, nama, password, prodi);
                JOptionPane.showMessageDialog(this, "Mahasiswa berhasil terdaftar! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                new HalamanAktivitasMahasiswa();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mendaftar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MahasiswaSignInPage();
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
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MahasiswaSignUpPage::new);
    }
}