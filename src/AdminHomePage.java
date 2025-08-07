import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class AdminHomePage extends JFrame{
    Perpustakaan perpustakaan = new Perpustakaan();
    private JTable tableBuku;
    private JTable tableTransaksi;
    private DefaultTableModel modelBuku;
    private DefaultTableModel modelTransaksi;
    private JTextField tfKode, tfJudul, tfPengarang, tfJumlah, tfCari;
    private TreeSet<String> pengarangSet = new TreeSet<>();
    private int jumlah;
    private int pengarangKe = 0;
    private TableRowSorter<TableModel> sorterTransaksi; // Cukup satu sorter untuk transaksi
    public AdminHomePage() {
        setTitle("Halaman Utama Admin");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Manajemen Buku
        JPanel panelBuku = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfKode = new JTextField(15);
        tfJudul = new JTextField(15);
        tfPengarang = new JTextField(15);
        tfJumlah = new JTextField(15);

        String[] labels = {"Kode Buku", "Judul", "Jumlah Pengarang", "Jumlah"};
        JTextField[] fields = {tfKode, tfJudul, tfPengarang, tfJumlah};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }


        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnSearch = new JButton("Search");
        JButton btnDelete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnDelete);
        buttonPanel.add(refresh);


        modelBuku = new DefaultTableModel(new Object[]{"Kode", "Judul", "Pengarang", "Jumlah"}, 0);
        tableBuku = new JTable(modelBuku);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tableBuku), BorderLayout.CENTER);

        panelBuku.add(formPanel, BorderLayout.NORTH);
        panelBuku.add(centerPanel, BorderLayout.CENTER);

        // Tab 2: Transaksi Mahasiswa
        JPanel panelTransaksi = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfCari = new JTextField(20);
        JButton btnCari = new JButton("Cari");
        JButton btnSort = new JButton("Sort by Day");
        JButton refreshButton = new JButton("Refresh");

        topPanel.add(new JLabel("Cari:"));
        topPanel.add(tfCari);
        topPanel.add(btnCari);
        topPanel.add(btnSort);
        topPanel.add(refreshButton);

        String[] columnNames = {"NIM", "Kode Buku", "Judul", "Tanggal Pinjam"};
        modelTransaksi = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return LocalDate.class;
                }
                return Object.class;
            }
        };
        tableTransaksi = new JTable(modelTransaksi);

        // PINDAHKAN & UBAH: Inisialisasi sorterTransaksi di sini, hanya sekali.
        sorterTransaksi = new TableRowSorter<>(modelTransaksi);
        tableTransaksi.setRowSorter(sorterTransaksi);

        // HAPUS: Baris ini tidak diperlukan lagi karena duplikat.
        // TableRowSorter<TableModel> sorter = new TableRowSorter<>(modelTransaksi);
        // tableTransaksi.setRowSorter(sorter);

        panelTransaksi.add(topPanel, BorderLayout.NORTH);
        panelTransaksi.add(new JScrollPane(tableTransaksi), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            try {
                addBuku();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnEdit.addActionListener(e -> {
            try {
                editBuku();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnSearch.addActionListener(e -> {
            try {
                cariBuku();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnDelete.addActionListener(e -> hapusBuku());
        refresh.addActionListener(e -> tampilkanTabelBuku());
        refreshButton.addActionListener(e -> tampilkanTabelPinjam());

        // HAPUS: Inisialisasi kedua untuk sorterTransaksi di sini tidak diperlukan.
        // sorterTransaksi = new TableRowSorter<>(modelTransaksi);
        // tableTransaksi.setRowSorter(sorterTransaksi);

        btnCari.addActionListener(e -> {
            String teksPencarian = tfCari.getText().trim();

            if (teksPencarian.length() == 0) {
                sorterTransaksi.setRowFilter(null);
            } else {
                sorterTransaksi.setRowFilter(RowFilter.regexFilter("(?i)" + teksPencarian, 0));
            }
        });

        // UBAH: Pastikan listener ini menggunakan field `sorterTransaksi`, bukan variabel lokal `sorter`.
        btnSort.addActionListener(e -> {
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
            sorterTransaksi.setSortKeys(sortKeys); // Menggunakan sorterTransaksi yang benar
        });


        // Tambahkan ke tabbedPane
        tabbedPane.addTab("Manajemen Buku", panelBuku);
        tabbedPane.addTab("Transaksi Mahasiswa", panelTransaksi);

        add(tabbedPane);
        tampilkanTabelBuku();
        tampilkanTabelPinjam();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                tampilkanTabelBuku();
                tampilkanTabelPinjam();
            }
        });

        setVisible(true);
    }
    private void addBuku() throws IOException {
        String Kode = tfKode.getText();
        String Judul = tfJudul.getText();
        String jumlahPengarang = tfPengarang.getText().trim();
        String Jumlah = tfJumlah.getText().trim();
        if(perpustakaan.cariBuku(Kode) != null){
            JOptionPane.showMessageDialog(this, "Kode buku tidak boleh sama!");
            return;
        }
        try {
            if (perpustakaan.cariBuku(Kode) != null) {
                JOptionPane.showMessageDialog(this, "Kode buku sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            jumlah = Integer.parseInt(jumlahPengarang);
            if (jumlah <= 0) throw new NumberFormatException();

            pengarangSet.clear();
            pengarangKe = 1;
            inputNamaPengarang(Kode, Judul, Integer.parseInt(Jumlah));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah pengarang dan jumlah buku harus angka > 0!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memeriksa buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void inputNamaPengarang(String kode, String judul, int jumlahBuku) {
        if (pengarangKe > jumlah) {
            try {
                perpustakaan.simpanBuku(kode, judul, pengarangSet, jumlahBuku);
                JOptionPane.showMessageDialog(null, "Buku berhasil disimpan!");
                tampilkanTabelBuku(); // Refresh tabel
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan buku ke file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        String nama = JOptionPane.showInputDialog(null, "Masukkan nama pengarang ke-" + pengarangKe + ":");
        if (nama != null && !nama.trim().isEmpty()) {
            pengarangSet.add(nama.trim());
            pengarangKe++;
            inputNamaPengarang(kode, judul, jumlahBuku);
        } else if (nama != null) {
            JOptionPane.showMessageDialog(null, "Nama pengarang tidak boleh kosong.");
            inputNamaPengarang(kode, judul, jumlahBuku);
        }
    }
    public void tampilkanTabelBuku() {
        modelBuku.setRowCount(0);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("dataBuku.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                modelBuku.addRow(parts);
            }
        } catch (IOException e) {
            try {
                Files.createFile(Paths.get("dataBuku.txt"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal membaca file dataBuku.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editBuku() throws Exception {
        String kode = tfKode.getText().trim();
        String judul = tfJudul.getText().trim();
        String jumlahPengarangText = tfPengarang.getText().trim();
        String jumlahBukuText = tfJumlah.getText().trim();

        if (kode.isEmpty() || judul.isEmpty() || jumlahPengarangText.isEmpty() || jumlahBukuText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            jumlah = Integer.parseInt(jumlahPengarangText);
            if (jumlah <= 0) throw new NumberFormatException();

            pengarangSet.clear();
            pengarangKe = 1;
            // Panggil metode pengumpul nama, sisanya akan ditangani di sana
            editNamaPengarang(kode, judul, Integer.parseInt(jumlahBukuText));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah pengarang harus angka > 0!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editNamaPengarang(String kode, String judul, int jumlahBuku) {

        if (pengarangKe > jumlah) {
            try {
                perpustakaan.editBuku(kode, judul, pengarangSet, jumlahBuku);
                JOptionPane.showMessageDialog(null, "Buku berhasil diedit!");
                tampilkanTabelBuku(); // Refresh tabel setelah berhasil disimpan
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal menyimpan buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        String nama = JOptionPane.showInputDialog(null, "Masukkan nama pengarang ke-" + pengarangKe + ":");
        if (nama != null && !nama.trim().isEmpty()) {
            pengarangSet.add(nama.trim());
            pengarangKe++;
            editNamaPengarang(kode, judul, jumlahBuku);
        } else if (nama != null) {
            JOptionPane.showMessageDialog(null, "Nama pengarang tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            editNamaPengarang(kode, judul, jumlahBuku);
        }

    }
    private void cariBuku() throws Exception {
        String kode = tfKode.getText().trim();
        try {
            String hasil = perpustakaan.cariBuku(kode);
            if (hasil != null && !hasil.trim().isEmpty()) {
                String[] baris = hasil.split(";");
                modelBuku.setRowCount(0);

                modelBuku.addRow(new Object[]{baris[0], baris[1], baris[2], baris[3]});
            }else{
                JOptionPane.showMessageDialog(this, "Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void hapusBuku(){
        String kode = tfKode.getText().trim();
        try{
            perpustakaan.hapusBuku(kode);
            JOptionPane.showMessageDialog(null, "Buku berhasil dihapus!");
            tampilkanTabelBuku();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void tampilkanTabelPinjam() {
        if (sorterTransaksi != null) {
            sorterTransaksi.setRowFilter(null);
        }


        if (tfCari != null) {
            tfCari.setText("");
        }
        modelTransaksi.setRowCount(0);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("dataPinjam.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length > 3) {
                    String nim = parts[0];
                    String kode = parts[1];
                    String judul = parts[2];
                    LocalDate tanggal = LocalDate.parse(parts[3]);
                    modelTransaksi.addRow(new Object[]{nim, kode, judul, tanggal});
                }
            }
        } catch (IOException e) {
            try {
                Files.createFile(Paths.get("dataPinjam.txt"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal membaca file dataPinjam.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminHomePage::new);
    }
}