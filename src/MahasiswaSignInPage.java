

import javax.swing.*;
import java.awt.*;

public class MahasiswaSignInPage extends JFrame {

    OperatorMahasiswa operator = new OperatorMahasiswa();
    JTextField nimField, prodiField, namaField;
    JPasswordField passwordField;
    public MahasiswaSignInPage() {
        setTitle("Mahasiswa - Sign In");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel latar dengan warna solid (cokelat gelap dari gambar)
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(0x3B1A12)); // tone dari tanah/latar
        backgroundPanel.setLayout(new GridBagLayout());

        // Panel isi form
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(255, 255, 255, 210)); // putih semi-transparan
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("Sign In Mahasiswa");
        title.setFont(new Font("Lato", Font.BOLD, 28));
        title.setForeground(new Color(111, 0, 44));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        nimField = createTextField("NIM");
        namaField = createTextField("Nama");
        passwordField = createPasswordField("Password");
        prodiField = createTextField("Prodi");

        JButton loginBtn = createMaroonButton("Login");
        loginBtn.addActionListener(e -> {
            String nim1 = nimField.getText().trim();
            String nama1 = namaField.getText().trim();
            String password1 = new String(passwordField.getPassword());
            String prodi1 = prodiField.getText().trim();

            if (nim1.isEmpty() || password1.isEmpty() || prodi1.isEmpty() || nama1.isEmpty() ){
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    if(operator.cekMahasiswa(nim1, nama1, password1, prodi1)){
                        SwingUtilities.invokeLater(() -> new HalamanAktivitasMahasiswa().setVisible(true));
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Mahasiswa belum terdaftar", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        JButton btnBack = new JButton("Back");
        btnBack.setBounds(680, 520, 80, 30); // Atur koordinat agar pojok kanan bawah
        btnBack.setBackground(new Color(0x800000));
        btnBack.setForeground(Color.WHITE);
        add(btnBack); // Tambahkan ke FRAME, bukan panel

        btnBack.addActionListener(e -> {
            new MahasiswaPage().setVisible(true);
            dispose();
        });


        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(nimField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(namaField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(prodiField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(loginBtn);
        // formPanel.add(backBtn);

        backgroundPanel.add(formPanel);
        add(backgroundPanel);
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(300, 40));
        textField.setFont(new Font("Lato", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createTitledBorder(placeholder));
        return textField;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setFont(new Font("Lato", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createTitledBorder(placeholder));
        return passwordField;
    }

    private JButton createMaroonButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Lato", Font.BOLD, 18));
        button.setBackground(new Color(111, 0, 44));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MahasiswaSignInPage().setVisible(true));
    }
}
