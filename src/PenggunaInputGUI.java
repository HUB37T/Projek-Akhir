import javax.swing.*;
import java.awt.*;

class PenggunaInputGUI extends JFrame {
    private JTextField nimField, namaField, prodiField;
    Perpustakaan perpustakaan = new Perpustakaan();

    public PenggunaInputGUI() {
        setTitle("Form Input Pengguna");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("NIM:"));
        nimField = new JTextField();
        add(nimField);

        add(new JLabel("Nama:"));
        namaField = new JTextField();
        add(namaField);

        add(new JLabel("Prodi:"));
        prodiField = new JTextField();
        add(prodiField);

        JButton DaftarButton = new JButton("Add");
        JButton SearchButton = new JButton("Search");
        JButton EditButton = new JButton("Edit");
        JButton DeleteButton = new JButton("Delete");
        JButton ClearButton = new JButton("Clear");
        add(DaftarButton);
        add(SearchButton);
        add(EditButton);
        add(DeleteButton);

        add(new JLabel());

        DaftarButton.addActionListener(e -> daftarPengguna());
        SearchButton.addActionListener(e -> searchPengguna());
        EditButton.addActionListener(e -> {
            try {
                editPengguna();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
        DeleteButton.addActionListener(e -> deletePengguna());
        ClearButton.addActionListener(e -> clearPengguna());

        setVisible(true);
    }
    private void daftarPengguna() {
        try {
            String nim = nimField.getText();
            String nama = namaField.getText();
            String prodi = prodiField.getText();
            perpustakaan.simpanPengguna(nim, nama, prodi);
            JOptionPane.showMessageDialog(null, "Pengguna berhasil didaftarkan!!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    private void searchPengguna() {
        try{
            String nim = nimField.getText();
            String hasil = perpustakaan.cariPengguna(nim);
            if(hasil != null){
                JOptionPane.showMessageDialog(null, hasil);
            }
            else{
                JOptionPane.showMessageDialog(null, "Pengguna tidak ditemukan!");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    private void editPengguna() throws Exception {
        String nim = nimField.getText().trim();
        String nama = namaField.getText().trim();
        String prodi = prodiField.getText().trim();

        if (nim.isEmpty() || nama.isEmpty() || prodi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            perpustakaan.editPengguna(nim, nama, prodi);
            JOptionPane.showMessageDialog(null, "Pengguna berhasil diedit!");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    private void deletePengguna() {
        try {
            String nim = nimField.getText().trim();
            perpustakaan.hapusPengguna(nim);
            JOptionPane.showMessageDialog(null, "User berhasil didelete!");
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null || message.isBlank()) {
                message = "Terjadi kesalahan saat menghapus User.";
            }
            JOptionPane.showMessageDialog(this, message);
        }
    }
    private void clearPengguna() {
        nimField.setText("");
        namaField.setText("");
        prodiField.setText("");
    }

    public static void main(String[] args) {
        PenggunaInputGUI frame = new PenggunaInputGUI();
    }
}
