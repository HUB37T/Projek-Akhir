import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
public class AdminHomePage extends JFrame{
    private JTable tableBuku;
    private JTable tableTransaksi;
    private DefaultTableModel modelBuku;
    private DefaultTableModel modelTransaksi;

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

        JTextField tfKode = new JTextField(15);
        JTextField tfJudul = new JTextField(15);
        JTextField tfPengarang = new JTextField(15);
        JTextField tfJumlah = new JTextField(15);

        String[] labels = {"Kode Buku", "Judul", "Pengarang", "Jumlah"};
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
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnDelete);

        modelBuku = new DefaultTableModel(new Object[]{"Kode", "Judul", "Pengarang", "Jumlah"}, 0);
        tableBuku = new JTable(modelBuku);

        panelBuku.add(formPanel, BorderLayout.NORTH);
        panelBuku.add(buttonPanel, BorderLayout.CENTER);
        panelBuku.add(new JScrollPane(tableBuku), BorderLayout.SOUTH);

        // Tab 2: Transaksi Mahasiswa
        JPanel panelTransaksi = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField tfCari = new JTextField(20);
        JButton btnCari = new JButton("Cari");
        topPanel.add(new JLabel("Cari:"));
        topPanel.add(tfCari);
        topPanel.add(btnCari);

        modelTransaksi = new DefaultTableModel(new Object[]{"NIM", "Kode Buku", "Tanggal Pinjam", "Jam Pinjam"}, 0);
        tableTransaksi = new JTable(modelTransaksi);

        panelTransaksi.add(topPanel, BorderLayout.NORTH);
        panelTransaksi.add(new JScrollPane(tableTransaksi), BorderLayout.CENTER);

        // Tombol cari aksi contoh
        btnCari.addActionListener(e -> {
            String keyword = tfCari.getText().trim();
            // TODO: filter tabel transaksi berdasarkan keyword
            JOptionPane.showMessageDialog(this, "Cari: " + keyword);
        });

        // Tambahkan ke tabbedPane
        tabbedPane.addTab("Manajemen Buku", panelBuku);
        tabbedPane.addTab("Transaksi Mahasiswa", panelTransaksi);

        add(tabbedPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminHomePage::new);
    }
}

