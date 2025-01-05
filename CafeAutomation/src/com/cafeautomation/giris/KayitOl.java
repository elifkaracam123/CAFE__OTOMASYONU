package com.cafeautomation.giris;

import javax.swing.*;

import com.cafeautomation.database.DatabaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class KayitOl extends JFrame {
    public KayitOl() {
        setTitle("Yönetici Kayıt");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        // Email Girişi
        JLabel emailLabel = new JLabel("Email:", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(emailField, gbc);

        // Şifre Girişi
        JLabel sifreLabel = new JLabel("Şifre:", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(sifreLabel, gbc);

        JPasswordField sifreField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(sifreField, gbc);

        // Teyit Kodu
        JLabel teyitLabel = new JLabel("Teyit Kodu", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(teyitLabel, gbc);

        JTextField teyitField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(teyitField, gbc);

        // Kayıt Ol Button
        JButton kayitOlButton = new JButton("Kayıt Ol");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(kayitOlButton, gbc);

        // Geri Button
        JButton geriButton = new JButton("Geri");
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(geriButton, gbc);

        // Kayıt Ol Butonu İşlevi
        kayitOlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String sifre = new String(sifreField.getPassword());
                String teyitKodu = teyitField.getText();

                if (email.isEmpty() || sifre.isEmpty() || teyitKodu.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (teyitDogrula(teyitKodu)) {
                    if (kayitYap(email, sifre)) {
                        JOptionPane.showMessageDialog(null, "Kayıt Başarılı!");
                        dispose();
                        new YoneticiGiris(); // Giriş sayfasına yönlendirme
                    } else {
                        JOptionPane.showMessageDialog(null, "Kayıt Başarısız!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Teyit Kodu Geçersiz!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Geri Butonu İşlevi
        geriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new YoneticiGiris().setVisible(true);
            }
        });

        // Paneli pencereye ekleme
        add(panel);

        // Görünür Yap
        setVisible(true);
    }

    private boolean kayitYap(String email, String sifre) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO kullanicilar (email, sifre, kullanici_tipi) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, sifre);
            ps.setString(3, "Yonetici"); // Varsayılan olarak 'Yonetici' tipi

            ps.executeUpdate();
            return true; // Kayıt başarılı
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Kayıt başarısız
        }
    }

    private boolean teyitDogrula(String teyitKodu) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM teyit_kodlari WHERE kod = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, teyitKodu);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // Eğer eşleşen bir teyit kodu varsa true döner
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) {
		SwingUtilities.invokeLater(KayitOl::new);
	}
}
