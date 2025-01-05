package com.cafeautomation.giris;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.cafeautomation.database.DatabaseConnection;

class UyeOl extends JFrame {
	public UyeOl() {
		setTitle("Üye Ol");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 0;

		// Email Girişi
		JLabel emailLabel = new JLabel("Email:", SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
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

		// Şifre Tekrar Label
		JLabel sifreTekrarLabel = new JLabel("Şifre Tekrar:", SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		panel.add(sifreTekrarLabel, gbc);

		// Şifre Tekrar Text Field
		JPasswordField sifreTekrarField = new JPasswordField(20);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		panel.add(sifreTekrarField, gbc);

		// Kayıt Ol Button
		JButton kayitOlButton = new JButton("Üye Ol");
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.gridwidth = 4;
		panel.add(kayitOlButton, gbc);

		// Kayıt Ol Button
		JButton geriButton = new JButton("Geri");
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 4;
		panel.add(geriButton, gbc);

		kayitOlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();
				String sifre = new String(sifreField.getPassword());
				String sifreTekrar = new String(sifreTekrarField.getPassword());

				if (sifre.equals(sifreTekrar)) {
					if (kayitYap(email, sifre)) {
						JOptionPane.showMessageDialog(null, "Kayıt Başarılı!");
						dispose();
						new KullaniciGiris(); // Giriş sayfasına yönlendirme
					} else {
						JOptionPane.showMessageDialog(null, "Kayıt Başarısız!", "Hata", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Şifreler Eşleşmiyor!", "Hata", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		geriButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new KullaniciGiris().setVisible(true);
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
			ps.setString(3, "Musteri"); // Varsayılan olarak 'Yonetici' tipi

			ps.executeUpdate();
			return true; // Kayıt başarılı
		} catch (Exception e) {
			e.printStackTrace();
			return false; // Kayıt başarısız
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(UyeOl::new);
	}
}
