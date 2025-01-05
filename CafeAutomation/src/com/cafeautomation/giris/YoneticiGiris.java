package com.cafeautomation.giris;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.cafeautomation.database.DatabaseConnection;
import com.cafeautomation.menu.YoneticiMenu;

public class YoneticiGiris extends JFrame {

	public YoneticiGiris() {
		// Pencere Ayarları
		setTitle("Yönetici Giriş Sayfası");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Pencereyi ortala

		// Panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout()); // GridBagLayout kullanımı
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10); // Elemanlar arasındaki boşluk
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		// Email Girişi
		JLabel emailLabel = new JLabel("Email:", SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(emailLabel, gbc);

		JTextField emailField = new JTextField(20);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
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
		gbc.gridwidth = 4;
		panel.add(sifreField, gbc);

		// Giriş Yap Butonu
		JButton girisButton = new JButton("Giriş Yap");
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 5;
		panel.add(girisButton, gbc);

		girisButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();
				String sifre = new String(sifreField.getPassword());

				if (yoneticiDogrula(email, sifre)) {
					JOptionPane.showMessageDialog(null, "Giriş Başarılı!");
					dispose();
					new YoneticiMenu();
				} else {
					JOptionPane.showMessageDialog(null, "Hatalı Email veya Şifre!");
				}
			}
		});

		// Şifre Unuttum ve Kayıt Ol Seçenekleri
		JPanel altPanel = new JPanel();
		altPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton sifreUnuttumButton = new JButton("Şifreyi Unuttum");
		sifreUnuttumButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new SifreUnutma(); // Şifre Sıfırlama Sayfasına geçiş
			}
		});

		JButton kayitOlButton = new JButton("Kayıt Ol");
		kayitOlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new KayitOl(); // Kayıt Sayfasına geçiş
			}
		});

		JButton geriButton = new JButton("Geri");
		geriButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new GirisTuruSecim(); // GirisTuruSecimSayfasına geçiş
			}
		});
		altPanel.add(sifreUnuttumButton);
		altPanel.add(kayitOlButton);
		altPanel.add(geriButton);

		// Ana Paneli Ekleme
		add(panel, BorderLayout.CENTER);
		add(altPanel, BorderLayout.SOUTH);
		setVisible(true);
	}

	private boolean yoneticiDogrula(String email, String sifre) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT * FROM kullanicilar WHERE email = ? AND sifre = ? AND kullanici_tipi = 'Yonetici'";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, sifre);

			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
