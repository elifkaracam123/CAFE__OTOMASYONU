package com.cafeautomation.giris;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.cafeautomation.database.DatabaseConnection;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


public class SifreUnutma extends JFrame {

    public SifreUnutma() {
        // Pencere başlığı ve ayarları
        setTitle("Şifre Sıfırlama");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel oluşturma
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Email Label
        JLabel emailLabel = new JLabel("Email adresinizi giriniz:");
        panel.add(emailLabel, gbc);

        // Email Text Field
        gbc.gridy++;
        JTextField emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Şifre Gönder Butonu
        gbc.gridy++;
        JButton sifreGonderButton = new JButton("Şifre Gönder");
        sifreGonderButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lütfen email adresinizi girin.");
                } else {
                    try {
                        if (emailKayitliMi(email)) {
                            String yeniSifre = rastgeleSifreOlustur();
                            sifreyiGonder(email, yeniSifre);
                            sifreyiVeritabaninaKaydet(email, yeniSifre);
                            JOptionPane.showMessageDialog(null, "Yeni şifre e-posta adresinize gönderildi.");
                            dispose();
                            new GirisTuruSecim();
                        } else {
                            JOptionPane.showMessageDialog(null, "E-posta adresi kayıtlı değil.", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(sifreGonderButton, gbc);

        // Paneli pencereye ekleme
        add(panel);

        // Görünür Yap
        setVisible(true);
    }

    private boolean emailKayitliMi(String email) {
        String query = "SELECT email FROM kullanicilar WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private String rastgeleSifreOlustur() {
        String karakterler = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(karakterler.charAt(random.nextInt(karakterler.length())));
        }
        return sb.toString();
    }

    private void sifreyiGonder(String email, String yeniSifre) {
        String host = "smtp.gmail.com";
        String kullaniciAdi = ""; // Gönderici e-posta adresi
        String sifre = ""; // Gönderici e-posta şifresi veya uygulama şifresi

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(kullaniciAdi, sifre);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(kullaniciAdi));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Şifre Sıfırlama");
            message.setText("Yeni şifreniz: " + yeniSifre);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Şifre gönderilirken hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sifreyiVeritabaninaKaydet(String email, String yeniSifre) {
        String query = "UPDATE kullanicilar SET sifre = ? WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, yeniSifre);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Şifre veritabanına kaydedilemedi: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

 
}
