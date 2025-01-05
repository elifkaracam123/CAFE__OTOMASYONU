package com.cafeautomation.giris;

import javax.swing.*;

import com.cafeautomation.masa.MasaIslemleri;
import com.cafeautomation.menu.KullaniciMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UyeGirisi extends JFrame {

	public UyeGirisi() {
		// Pencere başlığı ve ayarları
		setTitle("Üye Girişi");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Panel oluşturma
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout()); // GridBagLayout kullanımı
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10); // Elemanlar arasındaki boşluk
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		// Mail Alanı
		JLabel emailLabel = new JLabel("Mail:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(emailLabel, gbc);

		JTextField emailField = new JTextField(20);
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(emailField, gbc);

		// Şifre Alanı
		JLabel passwordLabel = new JLabel("Şifre:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(passwordLabel, gbc);

		JPasswordField passwordField = new JPasswordField(20);
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(passwordField, gbc);

		// Giriş Yap Butonu
		JButton loginButton = new JButton("Giriş Yap");
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(loginButton, gbc);

		// Şifremi Unuttum Butonu
		JButton forgotPasswordButton = new JButton("Şifremi Unuttum");
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(forgotPasswordButton, gbc);
		
		// Uye Ol Butonu
		JButton uyeOlButton = new JButton("Üye Ol");
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(uyeOlButton, gbc);

		// Geri Butonu
		JButton geriButton = new JButton("Geri");
		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(geriButton, gbc);

		// Giriş Yap Butonu Aksiyon
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();
				String password = new String(passwordField.getPassword());

				if (email.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurun.");
				} else {
					JOptionPane.showMessageDialog(null, "Giriş Başarılı!");
					new KullaniciMenu().setVisible(true);
					;
				}
			}
		});

		// Şifremi Unuttum Butonu Aksiyon
		forgotPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Şifre Sıfırlama Sayfasına Yönlendiriliyorsunuz...");
				// Şifre sıfırlama ekranını çağırabilirsiniz
				new SifreUnutma();
			}
		});
		// Geri Butonu Aksiyon
		uyeOlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new UyeOl(); // Kullanıcı giriş sayfasına geri dön
			}
		});

		// Geri Butonu Aksiyon
		geriButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new KullaniciGiris(); // Kullanıcı giriş sayfasına geri dön
			}
		});

		// Ana Paneli Ekleme
		add(panel);

		// Görünür Yap
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(UyeGirisi::new);
	}
}
