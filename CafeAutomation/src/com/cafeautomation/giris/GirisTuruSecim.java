package com.cafeautomation.giris;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GirisTuruSecim extends JFrame {

    public GirisTuruSecim() {
        // Pencere Başlığı
        setTitle("Giriş Türü Seçim");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Merkezde açılır

        // Panel oluşturma
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Label ekleme
        JLabel label = new JLabel("Giriş Türünüzü Seçin:", SwingConstants.LEFT);
        panel.add(label, gbc);

        // Yönetici Girişi butonu
        JButton yoneticiGirisButton = new JButton("Yönetici Girişi");
        gbc.gridy++;
        panel.add(yoneticiGirisButton, gbc);
        yoneticiGirisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Yönetici Giriş Sayfasına geçiş
                JOptionPane.showMessageDialog(null, "Yönetici Girişi Sayfasına Yönlendiriliyor...");
                dispose(); // Mevcut pencereyi kapat
                new YoneticiGiris(); // Yönetici Giriş Sayfasını aç
            }
        });

        // Kullanıcı Girişi butonu
        JButton kullaniciGirisButton = new JButton("Kullanıcı (Müşteri) Girişi");
        gbc.gridy++;
        panel.add(kullaniciGirisButton, gbc);
        kullaniciGirisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kullanıcı Giriş Sayfasına geçiş
                JOptionPane.showMessageDialog(null, "Kullanıcı Girişi Sayfasına Yönlendiriliyor...");
                dispose(); // Mevcut pencereyi kapat
                new KullaniciGiris(); // Kullanıcı Giriş Sayfasını aç
            }
        });

        // Paneli çerçeveye ekleme
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }


}
