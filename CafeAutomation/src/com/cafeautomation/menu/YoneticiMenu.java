package com.cafeautomation.menu;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.cafeautomation.giris.YoneticiGiris;
import com.cafeautomation.masa.MasaIslemleri;
import com.cafeautomation.urun.UrunIslemleri;
import com.cafeautomation.siparis.SiparisOnaylama;
import com.cafeautomation.siparis.YoneticiSiparisOnay;

public class YoneticiMenu extends JFrame {

	public YoneticiMenu() {
		// Pencere Ayarları
		setTitle("Yönetici Menü");
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

		// Ürün Ekle Butonu
		JButton urunIslemleriButton = new JButton("Ürün İşlemleri");
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(urunIslemleriButton, gbc);

		// Sipariş Onaylama Butonu
		JButton siparisOnaylaButton = new JButton("Sipariş Onayla");
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(siparisOnaylaButton, gbc);

		// Ürünleri Görüntüleme Butonu
		JButton masaIslemleriButton = new JButton("Masa İşlemleri");
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(masaIslemleriButton, gbc);

		JButton geriButton = new JButton("Geri");
		gbc.gridx = 0;
		gbc.gridy = 10;
		panel.add(geriButton, gbc);

		urunIslemleriButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new UrunIslemleri(); // Ürün Ekleme Sayfasına geçiş
			}
		});

		// Sipariş Onaylama Butonu
		siparisOnaylaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new YoneticiSiparisOnay(); // Sipariş Onaylama Sayfasına geçiş
			}
		});

		// Masa Görüntüleme Butonu
		masaIslemleriButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new MasaIslemleri(); 
			}
		});

		// Geri Butonu
		geriButton.addActionListener(e -> {
			dispose();
			new YoneticiGiris();
		});

		// Ana Paneli Çerçeveye Ekleme
		add(panel);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(YoneticiMenu::new);
	}
}
