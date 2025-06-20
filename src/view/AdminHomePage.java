package view;
import main.Perpustakaan;
import util.RoundedButton;

import java.io.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.swing.*;
import java.util.List;
import java.nio.file.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class AdminHomePage extends JFrame {
    private Perpustakaan perpustakaan = new Perpustakaan();
    private DefaultTableModel modelBuku;
    private DefaultTableModel modelTransaksi;
    private TableRowSorter<TableModel> sorterTransaksi;

    private JTable tableBuku;
    private JTable tableTransaksi;
    private JTextField tfKode, tfJudul, tfPengarang, tfJumlah, tfCari, fieldKode;
    private TreeSet<String> pengarangSet = new TreeSet<>();
    private int jumlah;
    private int pengarangKe;

    public AdminHomePage() {
        initFrame();
        setAlwaysOnTop(true);

        JTabbedPane tabbedPane = createStyledTabbedPane();

        tabbedPane.addTab("Manajemen Buku", createBukuPanel());
        tabbedPane.addTab("Transaksi Mahasiswa", createTransaksiPanel());

        add(tabbedPane);

        tampilkanTabelBuku();
        tampilkanTabelPinjam();

        registerWindowListener();

        setVisible(true);
    }

    private void initFrame() {
        setTitle("Halaman Utama Admin - Perpustakaan");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    // --- Panel Manajemen Buku ---
    private JPanel createBukuPanel() {
        JPanel panelBuku = new JPanel(new BorderLayout(10, 10));
        panelBuku.setOpaque(false);
        panelBuku.setBorder(new EmptyBorder(10, 10, 10, 10));


        panelBuku.add(createBukuFormPanel(), BorderLayout.WEST);
        panelBuku.add(createBukuTablePanel(), BorderLayout.CENTER);

        return panelBuku;
    }

    private JPanel createBukuFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xDAA520)),
                "Form Data Buku",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Lato", Font.BOLD, 16),
                new Color(0xDAA520)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;


        tfKode = createStyledTextField();
        tfJudul = createStyledTextField();
        tfPengarang = createStyledTextField();
        tfJumlah = createStyledTextField();


        String[] labels = {"Kode Buku", "Judul", "Jumlah Pengarang", "Jumlah"};
        JTextField[] fields = {tfKode, tfJudul, tfPengarang, tfJumlah};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(createStyledLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formPanel.add(fields[i], gbc);
            gbc.weightx = 0;
        }


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        RoundedButton btnAdd = createStyledButton("Add", "assets/icons/add_icon.png");
        RoundedButton btnEdit = createStyledButton("Edit", "assets/icons/edit_icon.png");
        RoundedButton btnDelete = createStyledButton("Delete", "assets/icons/delete_icon.png");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(buttonPanel, gbc);

        btnAdd.addActionListener(e -> {
            addBuku();
        });
        btnEdit.addActionListener(e -> {
            try {
                editBuku();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnDelete.addActionListener(e -> hapusBuku());

        return formPanel;
    }

    private JPanel createBukuTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setOpaque(false);

        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtonPanel.setOpaque(false);
        RoundedButton btnSearch = createStyledButton("Search", "assets/icons/search_icon.png");
        RoundedButton refresh = createStyledButton("Refresh", "assets/icons/refresh_icon.png");
        topButtonPanel.add(btnSearch);
        topButtonPanel.add(refresh);

        modelBuku = new DefaultTableModel(new Object[]{"Kode", "Judul", "Pengarang", "Jumlah"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tableBuku = new JTable(modelBuku);
        styleTable(tableBuku);

        JScrollPane tableScroll = new JScrollPane(tableBuku);
        tableScroll.getViewport().setBackground(new Color(0x4a2c2a));
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(0xDAA520)));

        tablePanel.add(topButtonPanel, BorderLayout.NORTH);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            try {
                cariBuku();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        refresh.addActionListener(e -> tampilkanTabelBuku());

        return tablePanel;
    }
    //tab transaksi
    private JPanel createTransaksiPanel() {
        JPanel panelTransaksi = new JPanel(new BorderLayout(10, 10));
        panelTransaksi.setOpaque(false);
        panelTransaksi.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xDAA520)),
                "Filter & Aksi",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Lato", Font.BOLD, 16),
                new Color(0xDAA520)
        ));

        tfCari = createStyledTextField();
        tfCari.setPreferredSize(new Dimension(200, 35));
        fieldKode = createStyledTextField();
        fieldKode.setPreferredSize(new Dimension(200, 35));
        RoundedButton btnCari = createStyledButton("Cari", "assets/icons/search_icon.png");
        RoundedButton btnSort = createStyledButton("Sort by Day", "assets/icons/sort_icon.png");
        RoundedButton refreshButton = createStyledButton("Refresh", "assets/icons/refresh_icon.png");
        RoundedButton kembali = createStyledButton("Kembali", "assets/icons/search_icon.png");

        topPanel.add(createStyledLabel("Cari NIM:"));
        topPanel.add(tfCari);
        topPanel.add(btnCari);
        topPanel.add(btnSort);
        topPanel.add(refreshButton);
        topPanel.add(fieldKode);
        topPanel.add(kembali);

        String[] columnNames = {"NIM", "Kode Buku", "Judul", "Tanggal Pinjam"};
        modelTransaksi = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) { return LocalDate.class; }
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tableTransaksi = new JTable(modelTransaksi);
        styleTable(tableTransaksi);

        JScrollPane tableScroll = new JScrollPane(tableTransaksi);
        tableScroll.getViewport().setBackground(new Color(0x4a2c2a));
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(0xDAA520)));

        sorterTransaksi = new TableRowSorter<>(modelTransaksi);
        tableTransaksi.setRowSorter(sorterTransaksi);

        panelTransaksi.add(topPanel, BorderLayout.NORTH);
        panelTransaksi.add(tableScroll, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> tampilkanTabelPinjam());
        btnCari.addActionListener(e -> {
            String teksPencarian = tfCari.getText().trim();
            if (teksPencarian.length() == 0) {
                sorterTransaksi.setRowFilter(null);
            } else {
                sorterTransaksi.setRowFilter(RowFilter.regexFilter("(?i)" + teksPencarian, 0));
            }
        });
        btnSort.addActionListener(e -> {
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
            sorterTransaksi.setSortKeys(sortKeys);
        });

        kembali.addActionListener(e -> {
            try {
                String nim = tfCari.getText().trim();
                String kode = fieldKode.getText().trim();
                BufferedReader br = new BufferedReader(new FileReader("data/dataPinjam.txt"));
                String line;
                while((line = br.readLine()) != null) {
                    String[] parts = line.split(";", -1);
                    if(parts[0].equals(nim) && parts[1].equals(kode)) {
                        LocalDate tanggalPinjam = LocalDate.parse(parts[3]);
                        LocalDate tanggalKembali = LocalDate.now();
                        long durasiPinjam = ChronoUnit.DAYS.between(tanggalPinjam, tanggalKembali);
                        if (durasiPinjam <= 7) {
                            String pesan = String.format("Pengembalian Tepat Waktu!\n\nDurasi Peminjaman: %d hari.\n\nLanjutkan proses pengembalian?", durasiPinjam);
                            int konfirmasi = JOptionPane.showConfirmDialog(this, pesan, "Konfirmasi Pengembalian", JOptionPane.YES_NO_OPTION);
                        } else {
                            long hariTerlambat = durasiPinjam - 7;
                            long denda = hariTerlambat * 1000;
                            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                            String pesan = String.format("Anda Terlambat Mengembalikan Buku!\n\nTotal Denda: %s", formatRupiah.format(denda));
                            String[] options = {"Lanjutkan Pembayaran", "Batal"};
                            int pilihan = JOptionPane.showOptionDialog(this, pesan, "Pemberitahuan Denda",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        }
                        perpustakaan.kembalikanBuku(nim, kode);
                        perpustakaan.tambahStokBuku(kode);
                        tampilkanTabelPinjam();
                    }
                }
                br.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);

            }
        });

        return panelTransaksi;
    }


    // --- Helper Methods untuk Styling & Fungsionalitas Asli ---

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
        JTextField textField = new JTextField(15);
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

    private RoundedButton createStyledButton(String text, String iconPath) {
        RoundedButton button = new RoundedButton(text);
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
    private void addBuku() {
        String kode = tfKode.getText().trim();
        String judul = tfJudul.getText().trim();
        String jumlahPengarangStr = tfPengarang.getText().trim();
        String jumlahBukuStr = tfJumlah.getText().trim();

        if (kode.isEmpty() || judul.isEmpty() || jumlahPengarangStr.isEmpty() || jumlahBukuStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (perpustakaan.cariBuku(kode) != null) {
                JOptionPane.showMessageDialog(this, "Kode buku sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int jumlahPengarang = Integer.parseInt(jumlahPengarangStr);
            int jumlahBuku = Integer.parseInt(jumlahBukuStr);
            TreeSet<String> pengarangSet = new TreeSet<>();

            if (jumlahPengarang <= 0 || jumlahBuku <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah pengarang dan jumlah buku harus angka > 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int pengarangKe = 1;
            while (pengarangKe <= jumlahPengarang) {
                String nama = JOptionPane.showInputDialog(this, "Masukkan nama pengarang ke-" + pengarangKe + ":");

                if (nama == null) {
                    JOptionPane.showMessageDialog(this, "Proses penambahan buku dibatalkan.", "Dibatalkan", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (!nama.trim().isEmpty()) {
                    pengarangSet.add(nama.trim());
                    pengarangKe++;
                } else {

                    JOptionPane.showMessageDialog(this, "Nama pengarang tidak boleh kosong.");
                }
            }

            perpustakaan.simpanBuku(kode, judul, pengarangSet, jumlahBuku);
            JOptionPane.showMessageDialog(this, "Buku berhasil disimpan!");
            tampilkanTabelBuku();

            tfKode.setText("");
            tfJudul.setText("");
            tfPengarang.setText("");
            tfJumlah.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah pengarang dan jumlah buku harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void tampilkanTabelBuku() {
        modelBuku.setRowCount(0);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data/dataBuku.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                modelBuku.addRow(parts);
            }
        } catch (IOException e) {
            try {
                Files.createFile(Paths.get("data/dataBuku.txt"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal membaca file dataBuku.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void editBuku() throws Exception{
        String kode = tfKode.getText().trim();
        String judul = tfJudul.getText().trim();
        String jumlahPengarangStr = tfPengarang.getText().trim();
        String jumlahBukuStr = tfJumlah.getText().trim();

        if (kode.isEmpty() || judul.isEmpty() || jumlahPengarangStr.isEmpty() || jumlahBukuStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi untuk mengedit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (perpustakaan.cariBuku(kode) == null) {
                JOptionPane.showMessageDialog(this, "Buku dengan kode '" + kode + "' tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int jumlahPengarang = Integer.parseInt(jumlahPengarangStr);
            int jumlahBuku = Integer.parseInt(jumlahBukuStr);
            TreeSet<String> pengarangSet = new TreeSet<>();

            if (jumlahPengarang <= 0 || jumlahBuku < 0) {
                JOptionPane.showMessageDialog(this, "Jumlah pengarang harus > 0 dan jumlah buku harus >= 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int pengarangKe = 1;
            while (pengarangKe <= jumlahPengarang) {
                String nama = JOptionPane.showInputDialog(this, "Masukkan nama pengarang baru ke-" + pengarangKe + ":");

                if (nama == null) {
                    JOptionPane.showMessageDialog(this, "Proses edit buku dibatalkan.", "Dibatalkan", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (!nama.trim().isEmpty()) {
                    pengarangSet.add(nama.trim());
                    pengarangKe++;
                } else {
                    JOptionPane.showMessageDialog(this, "Nama pengarang tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            }

            perpustakaan.editBuku(kode, judul, pengarangSet, jumlahBuku);
            JOptionPane.showMessageDialog(this, "Buku berhasil diedit!");
            tampilkanTabelBuku();

            tfKode.setText("");
            tfJudul.setText("");
            tfPengarang.setText("");
            tfJumlah.setText("");

        }catch(NumberFormatException ex) {

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
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void hapusBuku(){
        String kode = tfKode.getText().trim();
        try{
            perpustakaan.hapusBuku(kode);
            JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!");
            tampilkanTabelBuku();
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data/dataPinjam.txt"))) {
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
                Files.createFile(Paths.get("data/dataPinjam.txt"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal membaca file dataPinjam.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void registerWindowListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                tampilkanTabelBuku();
                tampilkanTabelPinjam();
            }
        });
    }
}