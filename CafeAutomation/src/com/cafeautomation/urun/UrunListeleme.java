package com.cafeautomation.urun;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.cafeautomation.database.DatabaseConnection;
import com.cafeautomation.menu.YoneticiMenu;

class UrunListeleme extends JFrame {
	public UrunListeleme() {
		setTitle("Ürün Listeleme");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Ana Panel
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

		// Başlık
		JLabel titleLabel = new JLabel("Ürün Listesi", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		// Ürün Listesi Alanı
		JTextArea urunListesi = new JTextArea();
		urunListesi.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(urunListesi);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// Ürünleri Veritabanından Yükle
		urunListesiniYukle(urunListesi);

		// Geri Düğmesi
		JButton geriButton = new JButton("Geri");
		geriButton.addActionListener(e -> {
			dispose();
			new YoneticiMenu(); // Yönetici Menü Sayfasına dön
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(geriButton);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
		setVisible(true);
	}

	private void urunListesiniYukle(JTextArea urunListesi) {
		StringBuilder icecekler = new StringBuilder("İçecekler:\n");
		StringBuilder yiyecekler = new StringBuilder("\nYiyecekler:\n");
		StringBuilder tatlilar = new StringBuilder("\nTatlılar:\n");

		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT * FROM urunler ORDER BY kategori, isim";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String kategori = rs.getString("kategori");
				String isim = rs.getString("isim");
				double fiyat = rs.getDouble("fiyat");

				switch (kategori.toLowerCase()) {
				case "içecekler":
					icecekler.append(" - ").append(isim).append(" (").append(fiyat).append(" TL)\n");
					break;
				case "yiyecekler":
					yiyecekler.append(" - ").append(isim).append(" (").append(fiyat).append(" TL)\n");
					break;
				case "tatlılar":
					tatlilar.append(" - ").append(isim).append(" (").append(fiyat).append(" TL)\n");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			urunListesi.setText("Ürünler yüklenirken hata oluştu.");
			return;
		}

		urunListesi.setText(icecekler.toString() + yiyecekler.toString() + tatlilar.toString());
	}

}
