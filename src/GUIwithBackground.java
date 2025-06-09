import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUIwithBackground extends JFrame {
    private Clip backgroundClip;
    private boolean isMuted = false;

    private RoundedButton adminButton;
    private RoundedButton mahasiswaButton;
    private JButton exitButton;
    private JButton muteButton;
    private JLabel clockLabel;


    public GUIwithBackground() {
        initFrame();
        initComponents();
        setupLayout();
        registerEventListeners();
        playBackgroundMusic("sound.wav");
        startFadeInAnimation();

        setFocusable(true);
        setVisible(true);
    }

    // --- (Helper Methods) ---

    private void initFrame() {
        setTitle("Sistem Perpustakaan - Selamat Datang");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        adminButton = new RoundedButton("Admin");
        styleMainButton(adminButton);
        adminButton.setToolTipText("Masuk sebagai pengelola sistem perpustakaan");

        mahasiswaButton = new RoundedButton("Mahasiswa");
        styleMainButton(mahasiswaButton);

        exitButton = new JButton("Exit");
        styleBottomButton(exitButton, new Color(139, 0, 0));
        exitButton.setToolTipText("Keluar dari aplikasi (ESC)");

        muteButton = new JButton("Mute");
        styleBottomButton(muteButton, new Color(0, 100, 0));
        muteButton.setToolTipText("Heningkan atau bunyikan musik latar");

        clockLabel = new JLabel("Memuat...");
        clockLabel.setFont(new Font("Lato", Font.PLAIN, 16));
        clockLabel.setForeground(Color.BLACK);
    }

    private void setupLayout() {
        BackgroundPanel bgPanel = new BackgroundPanel("Start.jpg");
        bgPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("Selamat datang!", SwingConstants.CENTER);
        welcomeLabel.setOpaque(true);
        welcomeLabel.setBackground(new Color(255, 215, 0, 180));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setFont(new Font("Lato", Font.BOLD, 28));
        welcomeLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 153, 180), 4),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        gbc.gridwidth = 2;
        bgPanel.add(welcomeLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        bgPanel.add(adminButton, gbc);

        gbc.gridx = 1;
        bgPanel.add(mahasiswaButton, gbc);

        JPanel southWrapperPanel = new JPanel(new BorderLayout());
        southWrapperPanel.setOpaque(false);

        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomRightPanel.setOpaque(false);
        bottomRightPanel.add(muteButton);
        bottomRightPanel.add(exitButton);

        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        bottomLeftPanel.setOpaque(false);
        bottomLeftPanel.add(clockLabel);

        southWrapperPanel.add(bottomLeftPanel, BorderLayout.WEST);
        southWrapperPanel.add(bottomRightPanel, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(bgPanel, BorderLayout.CENTER);
        wrapper.add(southWrapperPanel, BorderLayout.SOUTH);

        setContentPane(wrapper);
    }

    private void registerEventListeners() {
        exitButton.addActionListener(e -> showExitConfirmation());

        muteButton.addActionListener(e -> toggleMute());

        Timer clockTimer = new Timer(1000, e -> updateClock());
        clockTimer.start();

        adminButton.addActionListener(e -> {
            playSoundEffect("click.wav");
            new AdminPage();
        });
        mahasiswaButton.addActionListener(e -> {
            playSoundEffect("click.wav");
            new MahasiswaPage();
        });


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    showExitConfirmation();
                }
            }
        });
    }

    private void playBackgroundMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (audioFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioStream);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.err.println("File musik tidak ditemukan: " + filePath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }



    private void playSoundEffect(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (audioFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip sfxClip = AudioSystem.getClip();
                sfxClip.open(audioStream);
                sfxClip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startFadeInAnimation() {
        setOpacity(0f);
        Timer fadeInTimer = new Timer(20, e -> {
            float opacity = getOpacity();
            if (opacity < 1.0f) {
                opacity += 0.02f;
                setOpacity(Math.min(opacity, 1.0f));
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        fadeInTimer.start();
    }

    // --- Method untuk Aksi & Logika ---

    private void toggleMute() {
        if (backgroundClip != null) {
            if (isMuted) {
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                muteButton.setText("Mute");
            } else {
                backgroundClip.stop();
                muteButton.setText("Unmute");
            }
            isMuted = !isMuted;
        }
    }

    private void updateClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss, EEEE, dd MMMM yyyy", new java.util.Locale("id", "ID"));
        clockLabel.setText(LocalDateTime.now().format(formatter));
    }

    private void showExitConfirmation() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin keluar?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // --- Method untuk Styling Komponen (DRY Principle) ---

    private void styleMainButton(RoundedButton button) {
        button.setBackground(new Color(218, 165, 32));
        button.setPreferredSize(new Dimension(140, 40));
        button.setFont(new Font("Lato", Font.BOLD, 15));
        button.setForeground(Color.BLACK);
    }

    private void styleBottomButton(JButton button, Color color) {
        button.setPreferredSize(new Dimension(100, 30));
        button.setFont(new Font("Lato", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(color.darker()));
    }

    // --- Kelas Internal & Main Method ---

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

