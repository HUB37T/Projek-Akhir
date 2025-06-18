package view;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.sound.sampled.*;

public class RoundedButton extends JButton {
    private Color originalBackgroundColor;
    private Color hoverBackgroundColor;
    private Clip clickSFX;

    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);

        loadSound("assets/sounds/click.wav");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverBackgroundColor != null) {
                    RoundedButton.super.setBackground(hoverBackgroundColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                RoundedButton.super.setBackground(originalBackgroundColor);
            }

        });

        addActionListener(e -> playSound());
    }



    private void loadSound(String soundPath) {
        try {
            File audioFile = new File(soundPath);
            if (audioFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                clickSFX = AudioSystem.getClip();
                clickSFX.open(audioStream);
            } else {
                System.err.println("File suara tidak ditemukan: " + soundPath);
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat suara: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void playSound() {
        if (clickSFX != null) {
            if (clickSFX.isRunning()) clickSFX.stop();
            clickSFX.setFramePosition(0);
            clickSFX.start();
        }
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        this.originalBackgroundColor = bg;
        this.hoverBackgroundColor = bg.darker();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        super.paintComponent(g2);
        g2.dispose();
    }
}
