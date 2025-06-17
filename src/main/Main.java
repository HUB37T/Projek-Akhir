package main;
import view.RoundedButton;
import view.AdminPage;
import view.MahasiswaPage;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

public class Main extends JFrame {
    private Clip backgroundClip;
    private boolean isMuted = false;
    private FloatControl gainControl;

    private RoundedButton adminButton;
    private RoundedButton mahasiswaButton;
    private JButton exitButton;
    private JLabel clockLabel;
    private JSlider volumeSlider;
    private JLabel volumePercentageLabel;
    private JLabel volumeIconLabel;
    private int previousVolume = 100;

    private Font poppinsRegularFont;

    public Main() {
        loadCustomFonts();
        initFrame();
        initComponents();
        setupLayout();
        registerEventListeners();
        playBackgroundMusic("assets/sounds/sound.wav");
        startFadeInAnimation();

        setFocusable(true);
        setVisible(true);
    }

    // --- (Helper Methods) ---

    private void loadCustomFonts() {
        try {
            InputStream isPoppinsRegular = getClass().getResourceAsStream("/fonts/Poppin-Story.ttf");
            if (isPoppinsRegular != null) {
                poppinsRegularFont = Font.createFont(Font.TRUETYPE_FONT, isPoppinsRegular);
                isPoppinsRegular.close();
            } else {
                System.err.println("Gagal memuat font. Font tidak ditemukan di resource");
                poppinsRegularFont = new Font("Arial", Font.PLAIN, 16);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Gagal memuat font. Font tidak ditemukan di resource");
            poppinsRegularFont = new Font("Arial", Font.PLAIN, 16);
        }
    }

    private void initFrame() {
        setTitle("Sistem main.Perpustakaan - Selamat Datang");
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

        clockLabel = new JLabel("Memuat...");
        clockLabel.setFont(new Font("Lato", Font.PLAIN, 16));
        clockLabel.setForeground(Color.BLACK);

        volumePercentageLabel = new JLabel("100%");
        volumePercentageLabel.setFont(new Font("Lato", Font.PLAIN, 16));
        volumePercentageLabel.setForeground(Color.BLACK);

        volumeSlider = new JSlider(JSlider.HORIZONTAL,0 , 100, 100);
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setFocusable(false);
        volumeSlider.setPaintTicks(false);
        volumeSlider.setPaintLabels(false);
        volumeSlider.setPaintTrack(true);
        volumeSlider.setForeground(Color.BLACK);

        volumeIconLabel = new JLabel(new ImageIcon("assets/icons/volume_icon.png"));
        volumeIconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ImageIcon icon = new ImageIcon("assets/icons/volume_up.png");
        Image image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        volumeIconLabel.setIcon(new ImageIcon(image));
        volumeIconLabel.setToolTipText("Klik untuk mute/unmute suara");
    }

    private void setupLayout() {
        BackgroundPanel bgPanel = new BackgroundPanel("assets/images/Start.jpg");
        bgPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("Selamat datang!", SwingConstants.CENTER);
        welcomeLabel.setOpaque(true);
        welcomeLabel.setBackground(new Color(255, 215, 0, 180));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setFont(poppinsRegularFont.deriveFont(Font.BOLD, 32));
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
        bottomRightPanel.add(exitButton);

        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        bottomLeftPanel.setOpaque(false);
        bottomLeftPanel.add(clockLabel);
        bottomLeftPanel.add(volumeIconLabel);
        bottomLeftPanel.add(volumeSlider);
        bottomLeftPanel.add(volumePercentageLabel);

        southWrapperPanel.add(bottomLeftPanel, BorderLayout.WEST);
        southWrapperPanel.add(bottomRightPanel, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(bgPanel, BorderLayout.CENTER);
        wrapper.add(southWrapperPanel, BorderLayout.SOUTH);

        setContentPane(wrapper);
    }

    private void registerEventListeners() {
        addHoverEffect(adminButton, 140, 40, 150, 45, 15f, 20f);
        addHoverEffect(mahasiswaButton, 140, 40, 150, 45, 15f, 20f);

        exitButton.addActionListener(e -> showExitConfirmation());

        volumeIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleMute();
            }
        });

        volumeSlider.addChangeListener(e -> {
            int vol = volumeSlider.getValue();
            volumePercentageLabel.setText(vol + "%");

            if (gainControl != null) {
                if (vol == 0) {
                    gainControl.setValue(gainControl.getMinimum());
                    isMuted = true;
                    ImageIcon icon = new ImageIcon("assets/icons/volume_mute.png");
                    Image image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                    volumeIconLabel.setIcon(new ImageIcon(image));
                    backgroundClip.stop();
                } else {
                    float gain = (float) (Math.log10(vol / 100f) * 20);
                    gainControl.setValue(Math.max(gain, gainControl.getMinimum()));
                    isMuted = false;
                    ImageIcon icon = new ImageIcon("assets/icons/volume_up.png");
                    Image image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                    volumeIconLabel.setIcon(new ImageIcon(image));
                    if (!backgroundClip.isRunning()) backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        });

        Timer clockTimer = new Timer(1000, e -> updateClock());
        clockTimer.start();

        adminButton.addActionListener(e -> {
            playSoundEffect("assets/sounds/click.wav");;
            new AdminPage();
        });

        mahasiswaButton.addActionListener(e -> {
            playSoundEffect("assets/sounds/click.wav");
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

    // --- Method untuk hover Button ---

    private void addHoverEffect(RoundedButton selectedButton, int originalWidth, int originalHeight, int hoverWidth, int hoverHeight, float originalFontSize, float hoverFontSize) {
        Dimension originalSize = new Dimension(originalWidth, originalHeight);
        Dimension hoverSize = new Dimension(hoverWidth, hoverHeight);

        selectedButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selectedButton.setPreferredSize(hoverSize);
                selectedButton.setFont(selectedButton.getFont().deriveFont(hoverFontSize));
                selectedButton.revalidate();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selectedButton.setPreferredSize(originalSize);
                selectedButton.setFont(selectedButton.getFont().deriveFont(originalFontSize));
                selectedButton.revalidate();
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

                if (backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                    updateGainFromSlider();
                }

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
        if (backgroundClip != null && gainControl != null) {
            if (isMuted) {
                isMuted = false;
                volumeSlider.setValue(previousVolume > 0 ? previousVolume : 100);
                ImageIcon icon = new ImageIcon("assets/icons/volume_up.png");
                Image image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                volumeIconLabel.setIcon(new ImageIcon(image));
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                isMuted = true;
                previousVolume = volumeSlider.getValue();
                volumeSlider.setValue(0);
                ImageIcon icon = new ImageIcon("assets/icons/volume_mute.png");
                Image image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                volumeIconLabel.setIcon(new ImageIcon(image));
                backgroundClip.stop();
            }
        }
    }

    private void updateClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss, EEEE, dd MMMM yyyy", new java.util.Locale("id", "ID"));
        clockLabel.setText(LocalDateTime.now().format(formatter));
    }

    private void showExitConfirmation() {
        JDialog exitDialog = new JDialog();
        exitDialog.setAlwaysOnTop(true);
        int response = JOptionPane.showConfirmDialog(
                exitDialog,
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

    private void updateGainFromSlider() {
        if (gainControl != null) {
            float volume = volumeSlider.getValue() / 100f;
            if (volume == 0) {
                gainControl.setValue(gainControl.getMinimum());
            } else {
                float gain = (float) (Math.log10(volume) * 20);
                gainControl.setValue(Math.max(gain, gainControl.getMinimum()));
            }
        }
    }

    // --- Kelas Internal & main.Main Method ---

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
        SwingUtilities.invokeLater(Main::new);
    }
}

