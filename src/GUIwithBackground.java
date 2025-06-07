

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class GUIwithBackground extends JFrame {

    public GUIwithBackground() {
        setTitle("GUI dengan Background Gambar");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        playBackgroundMusic("sound.wav");

        // Panel dengan background gambar dan layout manager
        BackgroundPanel bgPanel = new BackgroundPanel("Start.jpg");
        bgPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // === LABEL "Selamat datang!" dengan highlight dan glow ===
        JLabel label = new JLabel("Selamat datang!", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(255, 215, 0, 180));
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Lato", Font.BOLD, 28));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 153, 180), 4),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        bgPanel.add(label, gbc);
        gbc.gridwidth = 1;

        // === TOMBOL Admin ===
        JButton Admin = new JButton("Admin");
        Admin.setPreferredSize(new Dimension(140, 35));
        Admin.setFont(new Font("Lato", Font.BOLD, 15));
        Admin.setBackground(new Color(218, 165, 32));
        Admin.setForeground(Color.BLACK);
        Admin.setFocusPainted(false);
        Admin.setBorder(BorderFactory.createLineBorder(new Color(255, 223, 0), 2));
        gbc.gridx = 0;
        gbc.gridy = 1;
        bgPanel.add(Admin, gbc);

        // === TOMBOL Mahasiswa ===
        JButton Mahasiswa = new JButton("Mahasiswa");
        Mahasiswa.setPreferredSize(new Dimension(140, 35));
        Mahasiswa.setFont(new Font("Lato", Font.BOLD, 15));
        Mahasiswa.setBackground(new Color(218, 165, 32));
        Mahasiswa.setForeground(Color.BLACK);
        Mahasiswa.setFocusPainted(false);
        Mahasiswa.setBorder(BorderFactory.createLineBorder(new Color(255, 223, 0), 2));
        gbc.gridx = 1;
        gbc.gridy = 1;
        bgPanel.add(Mahasiswa, gbc);

        // === WRAPPER UTAMA PAKAI BORDERLAYOUT ===
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomRightPanel.setOpaque(false);

        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(100, 30));
        exitButton.setFont(new Font("Lato", Font.BOLD, 14));
        exitButton.setBackground(new Color(139, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        bottomRightPanel.add(exitButton);

        exitButton.addActionListener(e -> System.exit(0));

        wrapper.add(bgPanel, BorderLayout.CENTER);
        wrapper.add(bottomRightPanel, BorderLayout.SOUTH);
        setContentPane(wrapper);

        Admin.addActionListener(e -> new AdminPage());
        Mahasiswa.addActionListener(e -> new MahasiswaPage());
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.exit(0);
                }
            }
        });

        setFocusable(true);
        setVisible(true);
    }

    public void playBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String filePath) {
            try {
                backgroundImage = new ImageIcon(filePath).getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIwithBackground::new);
    }
}
