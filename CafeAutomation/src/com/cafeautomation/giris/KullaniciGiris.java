package com.cafeautomation.giris;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.cafeautomation.menu.KullaniciMenu;

public class KullaniciGiris extends JFrame {

	public KullaniciGiris() {
		// Pencere başlığı ve ayarları
		setTitle("Kullanıcı Giriş Sayfası");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Ana Panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout()); // GridBagLayout kullanımı
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10); // Elemanlar arasındaki boşluk
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		// Butonlar
		JButton uyeGirisiButton = new JButton("Üye Girişi");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 5;
		panel.add(uyeGirisiButton, gbc);

		// Üye Olmadan Devam Et Butonu
		JButton uyeOlmadanButton = new JButton("Üye Olmadan Devam Et");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 5;
		panel.add(uyeOlmadanButton, gbc);

		// Geri Butonu
		JButton geriButton = new JButton("Geri");
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 5;
		panel.add(geriButton, gbc);

		// Üye Girişi Butonu Aksiyon
		uyeGirisiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // Mevcut pencereyi kapat
				new UyeGirisi(); // Üye Giriş ekranını aç
			}
		});

		// Üye Olmadan Devam Et Butonu Aksiyon
		uyeOlmadanButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // Mevcut pencereyi kapat

				JOptionPane.showMessageDialog(null, "Masa Seçim Ekranına Yönlendiriliyorsunuz...");
				// Burada masa seçim ekranını çağırabilirsiniz
				new KullaniciMenu();
			}
		});

		// Geri Butonu Aksiyon
		geriButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				JOptionPane.showMessageDialog(null, "Ana sayfaya geri dönüyorsunuz.");
				new GirisTuruSecim().setVisible(true);
			}
		});

		// Görünür Yap
		add(panel);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(KullaniciGiris::new);
	}

}
