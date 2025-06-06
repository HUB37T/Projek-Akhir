import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.TreeSet;

class BukuInputGUI extends JFrame {
    private JTextField kodeField, judulField, jumlahField;
    private JTable table;
    private DefaultTableModel tableModel;
    private TreeSet<String> pengarangSet = new TreeSet<>();
    private int jumlahPengarang;
    private int pengarangKe = 0;
    Perpustakaan perpustakaan = new Perpustakaan();

    public BukuInputGUI() {
        setTitle("Form Input Buku");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Kode Buku:"));
        kodeField = new JTextField();
        add(kodeField);

        add(new JLabel("Judul Buku:"));
        judulField = new JTextField();
        add(judulField);

        add(new JLabel("Jumlah Pengarang:"));
        jumlahField = new JTextField();
        add(jumlahField);

        JButton LanjutButton = new JButton("Add");
        JButton SearchButton = new JButton("Search");
        JButton EditButton = new JButton("Edit");
        JButton DeleteButton = new JButton("Delete");
        add(LanjutButton);
        add(SearchButton);
        add(EditButton);
        add(DeleteButton);
        add(new JLabel());

        String[] kolom = {"Kode", "Judul", "Pengarang"};
        tableModel = new DefaultTableModel(kolom, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table));
        tampilkanTabelBuku();


        LanjutButton.addActionListener(e -> daftarBuku());
        SearchButton.addActionListener(e -> searchBuku());
        EditButton.addActionListener(e -> {
            try {
                editBuku();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
        DeleteButton.addActionListener(e -> deleteBuku());
        setVisible(true);
    }

    private void daftarBuku() {
        String kode = kodeField.getText().trim();
        String judul = judulField.getText().trim();
        String jumlahText = jumlahField.getText().trim();

        if (kode.isEmpty() || judul.isEmpty() || jumlahText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            jumlahPengarang = Integer.parseInt(jumlahText);
            if (jumlahPengarang <= 0) throw new NumberFormatException();

            pengarangSet.clear();
            pengarangKe = 1;
            inputNamaPengarang(kode, judul);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah pengarang harus angka > 0!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        try {
            perpustakaan.simpanBuku(kode, judul, pengarangSet);
            tampilkanTabelBuku();
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void inputNamaPengarang(String kode, String judul) {
        if (pengarangKe > jumlahPengarang) {
            JOptionPane.showMessageDialog(null, "Buku berhasil disimpan!");
            return;
        }

        String nama = JOptionPane.showInputDialog(null, "Masukkan nama pengarang ke-" + pengarangKe + ":");
        if (nama != null && !nama.isBlank()) {
            pengarangSet.add(nama.trim());
            pengarangKe++;
            inputNamaPengarang(kode, judul);
        } else {
            JOptionPane.showMessageDialog(null, "Nama pengarang tidak boleh kosong.");
            inputNamaPengarang(kode, judul);
        }
    }

    public void searchBuku() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        JLabel labelKriteria = new JLabel("Cari berdasarkan:");
        String[] kriteria = {"Kode", "Judul", "Pengarang"};
        JComboBox<String> comboBox = new JComboBox<>(kriteria);

        JLabel labelInput = new JLabel("Masukkan nilai:");
        JTextField inputField = new JTextField();

        panel.add(labelKriteria);
        panel.add(comboBox);
        panel.add(labelInput);
        panel.add(inputField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Cari Buku", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String dipilih = (String) comboBox.getSelectedItem();
            String input = inputField.getText().trim();

            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Input tidak boleh kosong!");
                return;
            }

            String hasil = null;
            try {
                if (dipilih.equals("Kode")) {
                    hasil = perpustakaan.cariBukuKode(input);
                } else if (dipilih.equals("Judul")) {
                    hasil = perpustakaan.cariBukuJudul(input);
                } else if (dipilih.equals("Pengarang")) {
                    hasil = perpustakaan.cariBukuPengarang(input);
                }

                if (hasil != null) {
                    JOptionPane.showMessageDialog(null, hasil, "Hasil Pencarian", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Buku tidak ditemukan!", "Hasil Pencarian", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void editBuku() throws Exception {
        String kode = kodeField.getText().trim();
        String judul = judulField.getText().trim();
        String jumlahText = jumlahField.getText().trim();

        if (kode.isEmpty() || judul.isEmpty() || jumlahText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            jumlahPengarang = Integer.parseInt(jumlahText);
            if (jumlahPengarang <= 0) throw new NumberFormatException();

            pengarangSet.clear();
            pengarangKe = 1;
            editNamaPengarang(kode, judul);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah pengarang harus angka > 0!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editNamaPengarang(String kode, String judul) throws Exception{
        if (pengarangKe > jumlahPengarang) {
            perpustakaan.editBuku(kode,judul, pengarangSet);
            JOptionPane.showMessageDialog(null, "Buku berhasil diedit!");
            return;
        }

        String nama = JOptionPane.showInputDialog(null, "Masukkan nama pengarang ke-" + pengarangKe + ":");
        if (nama != null && !nama.isBlank()) {
            pengarangSet.add(nama.trim());
            pengarangKe++;
            inputNamaPengarang(kode, judul);
        } else {
            JOptionPane.showMessageDialog(null, "Nama pengarang tidak boleh kosong.");
            editNamaPengarang(kode, judul);
        }
    }

    public void deleteBuku() {
        try {
            String kode = kodeField.getText().trim();
            perpustakaan.hapusBuku(kode);
            JOptionPane.showMessageDialog(null, "Buku berhasil didelete!");
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null || message.isBlank()) {
                message = "Terjadi kesalahan saat menghapus buku.";
            }
            JOptionPane.showMessageDialog(this, message);
        }
    }

    public void tampilkanTabelBuku(){
        tableModel.setRowCount(0);
        try{
            File file = new File("dataBuku.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(";");
                String kode = parts[0];
                String judul = parts[1];
                String pengarang = parts[2];
                tableModel.addRow(new Object[]{kode,judul, pengarang});
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        BukuInputGUI gui = new BukuInputGUI();
    }
}

