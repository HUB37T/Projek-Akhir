import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class HalamanAktivitasMahasiswa extends JFrame {
    Perpustakaan perpustakaan = new Perpustakaan();
    private String nim = OperatorMahasiswa.nimLog; 
    private String nama = OperatorMahasiswa.namaLog;

    private JTextField cariFieldPinjam;
    private JTextField cariFieldKembali;

    private DefaultTableModel tabelModelPinjam;
    private DefaultTableModel tabelModelKembali;
    private JTable tabelKembali;

    public HalamanAktivitasMahasiswa() {
        setTitle("Aktivitas Mahasiswa - " + nama + " (" + nim + ")");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Pinjam Buku", createPinjamBukuTab());
        tabbedPane.addTab("Pengembalian Buku", createPengembalianBukuTab());
        tabbedPane.addTab("Manajemen Akun", createManajemenAkunTab());

        add(tabbedPane);
        
        tampilkanTabelSemuaPinjaman();
        tampilkanTabelPinjamanMahasiswa();
    }

    // ------------------ TAB PINJAM ------------------
    private JPanel createPinjamBukuTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x3B1A12));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        cariFieldPinjam = new JTextField(20);
        JButton cariBtn = new JButton("Cari");
        JButton listBtn = new JButton("List Buku Tersedia");
        JButton pinjamBtn = new JButton("Pinjam");

        topPanel.add(new JLabel("Kode Buku:"));
        topPanel.add(cariFieldPinjam);
        topPanel.add(cariBtn);
        topPanel.add(pinjamBtn);
        topPanel.add(listBtn);

        tabelModelPinjam = new DefaultTableModel();
        tabelModelPinjam.setColumnIdentifiers(new String[]{"NIM", "Kode Buku", "Judul", "Tanggal Pinjam"});
        JTable table = styledTable(tabelModelPinjam);
        JScrollPane tableScroll = new JScrollPane(table);

        cariBtn.addActionListener(e -> {
            String kodeCari = cariFieldPinjam.getText();
            if (kodeCari.trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Silakan isi kode buku.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String hasil = perpustakaan.cariBuku(kodeCari);
                if (hasil != null) {
                    String[] tes = hasil.split(";");
                    JOptionPane.showMessageDialog(panel, "Buku ditemukan!\nKode: " + tes[0] + "\nJudul: " + tes[1] + "\nPengarang: " + tes[2] + "\nJumlah Stok: " + tes[3], "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Buku tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(panel, "Gagal mencari buku: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        pinjamBtn.addActionListener(e -> pinjam());

        listBtn.addActionListener(e -> {
            StringBuilder daftarBuku = new StringBuilder("=== Daftar Buku Tersedia ===\n\n");
            File file = new File("dataBuku.txt");
            boolean adaBuku = false;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length >= 4) {
                        int jumlahStok = Integer.parseInt(data[3].trim());
                        if (jumlahStok > 0) {
                            daftarBuku.append(String.format("Kode: %s - Judul: %s (Stok: %d)\n", data[0].trim(), data[1].trim(), jumlahStok));
                            adaBuku = true;
                        }
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(panel, "Gagal membaca file data buku.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (adaBuku) {
                JOptionPane.showMessageDialog(panel, daftarBuku.toString(), "List Buku Tersedia", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel, "Saat ini tidak ada buku yang tersedia.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        return panel;
    }

    // ------------------ TAB PENGEMBALIAN (DENGAN PERUBAHAN BESAR) ------------------
    private JPanel createPengembalianBukuTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x3B1A12));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        cariFieldKembali = new JTextField(20);
        JButton kembalikanBtn = new JButton("Kembalikan");
        JButton listBtn = new JButton("List Buku Dipinjam");
        topPanel.add(new JLabel("Kode Buku:"));
        topPanel.add(cariFieldKembali);
        topPanel.add(kembalikanBtn);
        topPanel.add(listBtn);
        tabelModelKembali = new DefaultTableModel();
        tabelModelKembali.setColumnIdentifiers(new String[]{"Kode Buku", "Judul", "Tanggal Pinjam"});
        tabelKembali = styledTable(tabelModelKembali);
        JScrollPane tableScroll = new JScrollPane(tabelKembali);
        tabelKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabelKembali.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    cariFieldKembali.setText(tabelKembali.getValueAt(row, 0).toString());
                }
            }
        });

        kembalikanBtn.addActionListener(e -> {
            String kodeBuku = cariFieldKembali.getText().trim();
            if (kodeBuku.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Masukkan kode buku atau klik dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String recordPinjaman = findPinjamanRecord(nim, kodeBuku);
                if (recordPinjaman == null) {
                    JOptionPane.showMessageDialog(panel, "Anda tidak sedang meminjam buku dengan kode " + kodeBuku + ".", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[] parts = recordPinjaman.split(";");
                LocalDate tanggalPinjam = LocalDate.parse(parts[3]);
                LocalDate tanggalKembali = LocalDate.now();
                long durasiPinjam = ChronoUnit.DAYS.between(tanggalPinjam, tanggalKembali);

                if (durasiPinjam <= 7) {
                    String pesan = String.format("Pengembalian Tepat Waktu!\n\nDurasi Peminjaman: %d hari.\n\nLanjutkan proses pengembalian?", durasiPinjam);
                    int konfirmasi = JOptionPane.showConfirmDialog(panel, pesan, "Konfirmasi Pengembalian", JOptionPane.YES_NO_OPTION);
                    if (konfirmasi == JOptionPane.YES_OPTION) {
                        prosesPengembalianBuku(kodeBuku);
                    }
                } else {
                    long hariTerlambat = durasiPinjam - 7;
                    long denda = hariTerlambat * 1000;
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                    String pesan = String.format("Anda Terlambat Mengembalikan Buku!\n\nTotal Denda: %s", formatRupiah.format(denda));
                    
                    // Gunakan showOptionDialog untuk tombol custom
                    String[] options = {"Lanjutkan Pembayaran", "Batal"};
                    int pilihan = JOptionPane.showOptionDialog(panel, pesan, "Pemberitahuan Denda",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

                    if (pilihan == 0) { // Jika user memilih "Lanjutkan Pembayaran"
                        showPaymentDialog(kodeBuku);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        listBtn.addActionListener(e -> {
            StringBuilder daftarPinjaman = new StringBuilder("=== Buku yang Anda Pinjam ===\n\n");
            File file = new File("dataPinjam.txt");
            boolean adaPinjaman = false;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length >= 3 && data[0].equals(nim)) {
                        daftarPinjaman.append(String.format("Kode: %s - Judul: %s (Tgl: %s)\n", data[1], data[2], data[3]));
                        adaPinjaman = true;
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(panel, "Gagal membaca data pinjaman.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (adaPinjaman) {
                JOptionPane.showMessageDialog(panel, daftarPinjaman.toString(), "List Buku Dipinjam", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel, "Anda tidak sedang meminjam buku apapun.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        return panel;
    }

    // ------------------ TAB MANAJEMEN AKUN ------------------
    private JPanel createManajemenAkunTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x3B1A12));
        GridBagConstraints gbc = new GridBagConstraints();

        JButton editBtn = new JButton("Edit Akun");
        JButton hapusBtn = new JButton("Hapus Akun");

        editBtn.setPreferredSize(new Dimension(200, 40));
        hapusBtn.setPreferredSize(new Dimension(200, 40));

        editBtn.setBackground(new Color(111, 0, 44));
        hapusBtn.setBackground(new Color(111, 0, 44));
        editBtn.setForeground(Color.WHITE);
        hapusBtn.setForeground(Color.WHITE);
        
        gbc.gridy = 0;
        panel.add(editBtn, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0); // Jarak atas
        panel.add(hapusBtn, gbc);

        // ========================================================
        // ==          ACTION LISTENER UNTUK EDIT AKUN           ==
        // ========================================================
        editBtn.addActionListener(e -> {
            try {
                String[] data = new OperatorMahasiswa().getMahasiswaData(nim);
                if (data == null) {
                    JOptionPane.showMessageDialog(panel, "Gagal memuat data akun.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Buat panel custom untuk dialog input
                JPanel editPanel = new JPanel(new GridLayout(0, 1, 5, 5));
                JTextField namaField = new JTextField(data[1]);
                JPasswordField passField = new JPasswordField(data[2]);
                JTextField prodiField = new JTextField(data[3]);
                
                editPanel.add(new JLabel("Nama:"));
                editPanel.add(namaField);
                editPanel.add(new JLabel("Password Baru:"));
                editPanel.add(passField);
                editPanel.add(new JLabel("Prodi:"));
                editPanel.add(prodiField);
                
                int result = JOptionPane.showConfirmDialog(panel, editPanel, "Edit Data Akun",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String newNama = namaField.getText().trim();
                    String newPass = new String(passField.getPassword()).trim();
                    String newProdi = prodiField.getText().trim();

                    if (newNama.isEmpty() || newPass.isEmpty() || newProdi.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    new OperatorMahasiswa().editMahasiswa(nim, newNama, newPass, newProdi);
                    
                    JOptionPane.showMessageDialog(panel, "Akun berhasil diperbarui. Silakan login kembali.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Navigasi ke halaman login
                    new MahasiswaSignInPage().setVisible(true);
                    dispose();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(panel, "Error saat mengakses data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // =========================================================
        // ==          ACTION LISTENER UNTUK HAPUS AKUN           ==
        // =========================================================
        hapusBtn.addActionListener(e -> {
            try {
                // Cek dulu apakah mahasiswa masih punya pinjaman
                if (checkHasBorrowedBooks(nim)) {
                    JOptionPane.showMessageDialog(panel, "Tidak dapat menghapus akun.\nAnda masih memiliki buku yang belum dikembalikan.", "Aksi Ditolak", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(panel,
                        "Apakah Anda yakin ingin menghapus akun ini secara permanen?\nSemua data Anda akan hilang.",
                        "Konfirmasi Hapus Akun", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    new OperatorMahasiswa().hapusMahasiswa(nim);
                    
                    JOptionPane.showMessageDialog(panel, "Akun Anda telah berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Navigasi ke halaman utama mahasiswa
                    new MahasiswaPage().setVisible(true);
                    dispose();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(panel, "Error saat mengakses data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private boolean checkHasBorrowedBooks(String nim) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("dataPinjam.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(nim + ";")) {
                    return true; // Ditemukan record pinjaman untuk NIM ini
                }
            }
        } catch (FileNotFoundException e) {
            return false; // Jika file tidak ada, berarti tidak ada pinjaman
        }
        return false;
    }

    // =========================================================================
    // ==          METHOD BARU UNTUK MENAMPILKAN DIALOG PEMBAYARAN          ==
    // =========================================================================
    private void showPaymentDialog(String kodeBuku) {
        // Buat JDialog baru yang modal
        JDialog paymentDialog = new JDialog(this, "Pembayaran Denda via QRIS", true);
        paymentDialog.setSize(400, 500);
        paymentDialog.setLayout(new BorderLayout(10, 10));

        // Coba load gambar QR
        ImageIcon qrIcon = new ImageIcon("qris.jpg"); // PASTIKAN FILE INI ADA
        JLabel qrLabel;

        // Cek apakah gambar berhasil di-load
        if (qrIcon.getImageLoadStatus() == MediaTracker.ERRORED || !new File("qris.jpg").exists()) {
            qrLabel = new JLabel("Gagal memuat gambar qris.jpg", SwingConstants.CENTER);
            qrLabel.setForeground(Color.RED);
        } else {
            // Resize gambar agar pas di dialog
            Image image = qrIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            qrLabel = new JLabel(new ImageIcon(image));
        }

        JButton payButton = new JButton("Saya Sudah Bayar");
        payButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        payButton.addActionListener(e -> {
            // Setelah tombol ini diklik, baru proses pengembalian dijalankan
            prosesPengembalianBuku(kodeBuku);
            paymentDialog.dispose(); // Tutup dialog pembayaran
        });

        // Panel untuk menampung tombol agar ada margin
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomPanel.add(payButton);
        
        paymentDialog.add(qrLabel, BorderLayout.CENTER);
        paymentDialog.add(bottomPanel, BorderLayout.SOUTH);
        paymentDialog.setLocationRelativeTo(this); // Muncul di tengah frame utama
        paymentDialog.setVisible(true);
    }
    
    /**
     * Metode terpusat untuk menjalankan logika pengembalian buku dan update UI.
     */
    private void prosesPengembalianBuku(String kodeBuku) {
        try {
            perpustakaan.kembalikanBuku(nim, kodeBuku);
            perpustakaan.tambahStokBuku(kodeBuku);
            JOptionPane.showMessageDialog(this, "Buku '" + kodeBuku + "' berhasil dikembalikan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            tampilkanTabelPinjamanMahasiswa();
            tampilkanTabelSemuaPinjaman();
            cariFieldKembali.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal memproses pengembalian: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // ------------------ HELPER METHODS ------------------
    private String findPinjamanRecord(String nim, String kodeBuku) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("dataPinjam.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 1 && parts[0].equals(nim) && parts[1].equals(kodeBuku)) {
                    return line;
                }
            }
        }
        return null;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0x5D2E2E));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(0xA0522D));
        table.setSelectionForeground(Color.WHITE);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(0xF2E8DC) : new Color(0xEADBC8));
                }
                return c;
            }
        });
        return table;
    }

    private void pinjam() {
        String kodeBuku = cariFieldPinjam.getText().trim();
        if (kodeBuku.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan kode buku.", "Input Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            perpustakaan.kurangiStokBuku(kodeBuku); // Ini akan melempar Exception jika stok habis atau buku tidak ada
            String dataBuku = perpustakaan.cariBuku(kodeBuku);
            String judulBuku = dataBuku.split(";")[1];
            
            perpustakaan.pinjamBuku(nim, kodeBuku, judulBuku, LocalDate.now());

            JOptionPane.showMessageDialog(this, "Buku '" + judulBuku + "' berhasil dipinjam!");
            // Refresh kedua tabel
            tampilkanTabelSemuaPinjaman();
            tampilkanTabelPinjamanMahasiswa();
            cariFieldPinjam.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal meminjam buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menampilkan SEMUA data pinjaman di TAB PINJAM
    public void tampilkanTabelSemuaPinjaman() {
        tabelModelPinjam.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("dataPinjam.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                tabelModelPinjam.addRow(parts);
            }
        } catch (IOException e) {
            // Abaikan jika file belum ada
        }
    }

    // Menampilkan data pinjaman HANYA MILIK MAHASISWA yang login di TAB PENGEMBALIAN
    public void tampilkanTabelPinjamanMahasiswa() {
        tabelModelKembali.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("dataPinjam.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                // Cek apakah NIM cocok dengan mahasiswa yang login
                if (parts[0].equals(nim)) {
                    // Hanya tambahkan kolom yang relevan
                    tabelModelKembali.addRow(new Object[]{parts[1], parts[2], parts[3]});
                }
            }
        } catch (IOException e) {
            // Abaikan jika file belum ada
        }
    }

    public static void main(String[] args) {
        // Simulasi login untuk testing
        OperatorMahasiswa.nimLog = "000"; 
        OperatorMahasiswa.namaLog = "Guest";
        SwingUtilities.invokeLater(() -> new HalamanAktivitasMahasiswa().setVisible(true));
    }
}