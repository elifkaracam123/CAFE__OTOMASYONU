package com.cafeautomation.menu;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.cafeautomation.database.DatabaseConnection;
import com.cafeautomation.giris.KullaniciGiris;
import com.cafeautomation.urun.UrunSecim;

public class KullaniciMenu extends JFrame {
	private JComboBox<String> masaComboBox;

	public KullaniciMenu() {
		// Pencere Ayarları
		setTitle("Kullanıcı Menü - Masa Seçimi");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Panel
		JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 satır, 1 sütun

		// Boş Masaları Getir
		JLabel masaLabel = new JLabel("Boş Masalar:");
		masaComboBox = new JComboBox<>(getBosMasalar().toArray(new String[0])); // Boş masaları ekle
		panel.add(masaLabel);
		panel.add(masaComboBox);

		// Ürün Seçim Butonu
		JButton urunSecButton = new JButton("Ürün Seç ve Sipariş Ver");
		urunSecButton.addActionListener(e -> urunSecimiYap());
		panel.add(urunSecButton);

		// Geri Butonu
		JButton geriButton = new JButton("Geri");
		geriButton.addActionListener(e -> {
			dispose();
			new KullaniciGiris(); // Kullanıcı giriş ekranına geri dön
		});
		panel.add(geriButton);

		// Paneli Çerçeveye Ekleme
		add(panel);
		setVisible(true);
	}

	private ArrayList<String> getBosMasalar() {
		ArrayList<String> bosMasalar = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT masa_no FROM masalar WHERE durum = 'BOS'";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				bosMasalar.add(rs.getString("masa_no"));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Boş masalar yüklenirken hata oluştu: " + e.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
		return bosMasalar;
	}

	private void urunSecimiYap() {
		String secilenMasa = (String) masaComboBox.getSelectedItem();
		if (secilenMasa == null) {
			JOptionPane.showMessageDialog(this, "Lütfen bir masa seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this,
					"Masa: " + secilenMasa + " seçildi. Ürün seçimine yönlendiriliyorsunuz.");
			dispose();
			new UrunSecim(secilenMasa); // Ürün seçimini başlat, seçilen masayı ilet
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(KullaniciMenu::new);
	}
}
