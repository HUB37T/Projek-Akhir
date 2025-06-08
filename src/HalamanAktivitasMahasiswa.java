import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class HalamanAktivitasMahasiswa extends JFrame {
    private Perpustakaan perpustakaan = new Perpustakaan();
    private String nim = OperatorMahasiswa.nimLog;
    private String nama = OperatorMahasiswa.namaLog;

    private JTextField cariFieldPinjam;
    private JTextField cariFieldKembali;
    private DefaultTableModel tabelModelPinjam;
    private DefaultTableModel tabelModelKembali;
    private JTable tabelKembali;
    private JTable tabelPinjam;

    public HalamanAktivitasMahasiswa() {
        initFrame();

        JTabbedPane tabbedPane = createStyledTabbedPane();

        tabbedPane.addTab("Pinjam Buku", createPinjamBukuTab());
        tabbedPane.addTab("Pengembalian Buku", createPengembalianBukuTab());
        tabbedPane.addTab("Manajemen Akun", createManajemenAkunTab());

        add(tabbedPane);

        tampilkanTabelSemuaPinjaman();
        tampilkanTabelPinjamanMahasiswa();

        setVisible(true);
    }

    private void initFrame() {
        setTitle("Aktivitas Mahasiswa - " + nama + " (" + nim + ")");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0x3B1A12));
    }

    private JTabbedPane createStyledTabbedPane() {
        UIManager.put("TabbedPane.selected", new Color(0x5a382b));
        UIManager.put("TabbedPane.contentAreaColor", new Color(0x5a382b));
        UIManager.put("TabbedPane.background", new Color(0x3B1A12));
        UIManager.put("TabbedPane.foreground", Color.WHITE);
        UIManager.put("TabbedPane.darkShadow", Color.BLACK);
        UIManager.put("TabbedPane.light", Color.GRAY);
        UIManager.put("TabbedPane.focus", new Color(0xDAA520));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Lato", Font.BOLD, 14));
        return tabbedPane;
    }

    // --- TAB 1: PINJAM BUKU ---
    private JPanel createPinjamBukuTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xDAA520)), "Aksi Peminjaman",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Lato", Font.BOLD, 16), new Color(0xDAA520)));

        //FIeld
        cariFieldPinjam = createStyledTextField();
        cariFieldPinjam.setPreferredSize(new Dimension(200, 35));

        //button
        JButton cariBtn = createStyledButton("Cari Buku", "search_icon.png");
        JButton pinjamBtn = createStyledButton("Pinjam Buku Ini", "borrow_icon.png");
        JButton listBtn = createStyledButton("List Buku Tersedia", "list_icon.png");

        topPanel.add(createStyledLabel("Kode Buku:"));
        topPanel.add(cariFieldPinjam);
        topPanel.add(cariBtn);
        topPanel.add(pinjamBtn);
        topPanel.add(listBtn);

        // Tabel semua pinjaman
        tabelModelPinjam = new DefaultTableModel(new String[]{"NIM", "Kode Buku", "Judul", "Tanggal Pinjam"}, 0);
        tabelPinjam = new JTable(tabelModelPinjam);
        styleTable(tabelPinjam);
        JScrollPane tableScroll = new JScrollPane(tabelPinjam);
        tableScroll.getViewport().setBackground(new Color(0x4a2c2a));
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(0xDAA520)));

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
                JTextArea textArea = new JTextArea(daftarBuku.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(panel, scrollPane, "List Buku Tersedia", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel, "Saat ini tidak ada buku yang tersedia.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        return panel;
    }

    // --- TAB 2: PENGEMBALIAN BUKU ---
    private JPanel createPengembalianBukuTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xDAA520)), "Aksi Pengembalian",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Lato", Font.BOLD, 16), new Color(0xDAA520)));

        //field
        cariFieldKembali = createStyledTextField();
        cariFieldKembali.setPreferredSize(new Dimension(200, 35));

        //button
        JButton kembalikanBtn = createStyledButton("Kembalikan Buku Ini", "return_icon.png");
        JButton listBtn = createStyledButton("List Buku Dipinjam", "list_icon.png");

        topPanel.add(createStyledLabel("Kode Buku:"));
        topPanel.add(cariFieldKembali);
        topPanel.add(kembalikanBtn);
        topPanel.add(listBtn);

        //tabel
        tabelModelKembali = new DefaultTableModel(new String[]{"Kode Buku", "Judul", "Tanggal Pinjam"}, 0);
        tabelKembali = new JTable(tabelModelKembali);
        styleTable(tabelKembali);
        JScrollPane tableScroll = new JScrollPane(tabelKembali);
        tableScroll.getViewport().setBackground(new Color(0x4a2c2a));
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(0xDAA520)));

        tabelKembali.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
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
                    String[] options = {"Lanjutkan Pembayaran", "Batal"};
                    int pilihan = JOptionPane.showOptionDialog(panel, pesan, "Pemberitahuan Denda",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (pilihan == 0) {
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
                JTextArea textArea = new JTextArea(daftarPinjaman.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(panel, scrollPane, "List Buku Dipinjam", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel, "Anda tidak sedang meminjam buku apapun.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        return panel;
    }

    // --- TAB 3: MANAJEMEN AKUN ---
    private JPanel createManajemenAkunTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel titleLabel = new JLabel("Pengaturan Akun", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lato", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JButton editBtn = createStyledButton("Edit Akun", "edit_user_icon.png");
        editBtn.setPreferredSize(new Dimension(250, 50));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(editBtn, gbc);

        JLabel editDesc = new JLabel("<html>Perbarui nama, password, atau prodi Anda.</html>");
        editDesc.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(editDesc, gbc);

        JButton hapusBtn = createStyledButton("Hapus Akun", "delete_user_icon.png");
        hapusBtn.setBackground(new Color(139, 0, 0));
        hapusBtn.setPreferredSize(new Dimension(250, 50));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(hapusBtn, gbc);

        JLabel hapusDesc = new JLabel("<html>Hapus akun Anda secara permanen dari sistem.</html>");
        hapusDesc.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(hapusDesc, gbc);

        editBtn.addActionListener(e -> {
            try {
                String[] data = new OperatorMahasiswa().getMahasiswaData(nim);
                if (data == null) {
                    JOptionPane.showMessageDialog(panel, "Gagal memuat data akun.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
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
                    new MahasiswaSignInPage().setVisible(true);
                    dispose();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(panel, "Error saat mengakses data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        hapusBtn.addActionListener(e -> {
            try {
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
                    new MahasiswaPage().setVisible(true);
                    dispose();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(panel, "Error saat mengakses data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- Helper Methods untuk Styling ---
    private void styleTable(JTable table) {
        table.setBackground(new Color(0x4a2c2a));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(0xDAA520));
        table.setRowHeight(25);
        table.setFont(new Font("Lato", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(0xDAA520));
        table.setSelectionForeground(Color.BLACK);
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0xDAA520));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Lato", Font.BOLD, 16));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Lato", Font.PLAIN, 14));
        textField.setBackground(new Color(0x5a382b));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDAA520)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Lato", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }
    private JButton createStyledButton(String text, String iconPath) {
        RoundedButton button = new RoundedButton(text); // Asumsi kelas RoundedButton ada
        button.setFont(new Font("Lato", Font.BOLD, 12));
        button.setBackground(new Color(218, 165, 32));
        button.setForeground(Color.BLACK);
        try {
            ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            button.setIcon(icon);
        } catch (Exception e) {
            System.err.println("Ikon tidak ditemukan: " + iconPath);
        }
        return button;
    }

    //Method pinjam
    private void pinjam() {
        String kodeBuku = cariFieldPinjam.getText().trim();
        if (kodeBuku.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan kode buku.", "Input Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String dataBuku = perpustakaan.cariBuku(kodeBuku);
            if (dataBuku == null) {
                JOptionPane.showMessageDialog(this, "Buku dengan kode '" + kodeBuku + "' tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            perpustakaan.kurangiStokBuku(kodeBuku);
            String judulBuku = dataBuku.split(";")[1];
            perpustakaan.pinjamBuku(nim, kodeBuku, judulBuku, LocalDate.now());
            JOptionPane.showMessageDialog(this, "Buku '" + judulBuku + "' berhasil dipinjam!");
            tampilkanTabelSemuaPinjaman();
            tampilkanTabelPinjamanMahasiswa();
            cariFieldPinjam.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal meminjam buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
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

    //method kembaliin
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
    private boolean checkHasBorrowedBooks(String nim) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("dataPinjam.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(nim + ";")) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }

    //method bayar
    private void showPaymentDialog(String kodeBuku) {
        JDialog paymentDialog = new JDialog(this, "Pembayaran Denda via QRIS", true);
        paymentDialog.setSize(400, 500);
        paymentDialog.setLayout(new BorderLayout(10, 10));
        paymentDialog.getContentPane().setBackground(new Color(0x3B1A12));

        ImageIcon qrIcon = new ImageIcon("qris.jpg");
        JLabel qrLabel;
        if (qrIcon.getImageLoadStatus() == MediaTracker.ERRORED || !new File("qris.jpg").exists()) {
            qrLabel = new JLabel("Gagal memuat gambar qris.jpg", SwingConstants.CENTER);
            qrLabel.setForeground(Color.RED);
        } else {
            Image image = qrIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            qrLabel = new JLabel(new ImageIcon(image));
        }

        JButton payButton = createStyledButton("Saya Sudah Bayar", "payment_icon.png");
        payButton.addActionListener(e -> {
            prosesPengembalianBuku(kodeBuku);
            paymentDialog.dispose();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(payButton);

        paymentDialog.add(qrLabel, BorderLayout.CENTER);
        paymentDialog.add(bottomPanel, BorderLayout.SOUTH);
        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.setVisible(true);
    }

    //tampilkan tabel
    public void tampilkanTabelSemuaPinjaman() {
        tabelModelPinjam.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("dataPinjam.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                tabelModelPinjam.addRow(parts);
            }
        } catch (IOException e) {
        }
    }
    public void tampilkanTabelPinjamanMahasiswa() {
        tabelModelKembali.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("dataPinjam.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(nim)) {
                    tabelModelKembali.addRow(new Object[]{parts[1], parts[2], parts[3]});
                }
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        OperatorMahasiswa.nimLog = "000";
        OperatorMahasiswa.namaLog = "Guest";
        SwingUtilities.invokeLater(() -> new HalamanAktivitasMahasiswa().setVisible(true));
    }
}