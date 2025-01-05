package com.cafeautomation.urun;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.cafeautomation.database.DatabaseConnection;
import com.cafeautomation.menu.YoneticiMenu;

public class UrunIslemleri extends JFrame {
	private DefaultTableModel tableModel;

	public UrunIslemleri() {
		setTitle("Ürün Yönetimi");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;

		// Başlık
		JLabel label = new JLabel("Tüm Ürün Listesi", SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(label, gbc);

		// Ürün Tablosu
		String[] columnNames = { "Ürün ID", "Ürün Adı", "Fiyat (₺)", "Kategori" };
		tableModel = new DefaultTableModel(columnNames, 0);
		JTable urunTablosu = new JTable(tableModel);
		JScrollPane tableScrollPane = new JScrollPane(urunTablosu);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		panel.add(tableScrollPane, gbc);

		// Ürünleri veritabanından yükle
		urunleriYukle();

		// Ürün Ekleme Alanı
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;

		JLabel urunAdiLabel = new JLabel("Ürün Adı:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(urunAdiLabel, gbc);

		JTextField urunAdiField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(urunAdiField, gbc);

		JLabel urunFiyatiLabel = new JLabel("Fiyat (₺):");
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(urunFiyatiLabel, gbc);

		JTextField urunFiyatiField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(urunFiyatiField, gbc);

		JLabel kategoriLabel = new JLabel("Kategori:");
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(kategoriLabel, gbc);

		String[] kategoriler = { "Yiyecek", "İçecek", "Tatlı" };
		JComboBox<String> kategoriComboBox = new JComboBox<>(kategoriler);
		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(kategoriComboBox, gbc);

		JButton urunEkleButton = new JButton("Ürün Ekle");
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		panel.add(urunEkleButton, gbc);

		// Ürün Silme Alanı
		JLabel urunIdLabel = new JLabel("Ürün ID:");
		gbc.gridx = 0;
		gbc.gridy = 6;
		panel.add(urunIdLabel, gbc);

		JTextField urunIdField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 6;
		panel.add(urunIdField, gbc);

		JButton urunSilButton = new JButton("Ürün Sil");
		gbc.gridx = 1;
		gbc.gridy = 7;
		panel.add(urunSilButton, gbc);

		JButton urunGuncelleButton = new JButton("Ürün Güncelle");
		gbc.gridx = 1;
		gbc.gridy = 8;
		panel.add(urunGuncelleButton, gbc);

		JButton geriButton = new JButton("Geri");
		gbc.gridx = 0;
		gbc.gridy = 9;
		panel.add(geriButton, gbc);

		// Ürün Ekle Butonu İşlevi
		urunEkleButton.addActionListener(e -> {
			String urunAdi = urunAdiField.getText().trim();
			String urunFiyatiStr = urunFiyatiField.getText().trim();
			String kategori = (String) kategoriComboBox.getSelectedItem();

			if (urunAdi.isEmpty() || urunFiyatiStr.isEmpty() || kategori == null) {
				JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				double urunFiyati = Double.parseDouble(urunFiyatiStr);
				urunEkle(urunAdi, urunFiyati, kategori);
				JOptionPane.showMessageDialog(this, "Ürün başarıyla eklendi!");
				urunAdiField.setText("");
				urunFiyatiField.setText("");
				urunleriYukle(); // Tablodaki verileri yenile
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Geçerli bir fiyat girin!", "Hata", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Ürün Sil Butonu İşlevi
		urunSilButton.addActionListener(e -> {
			String urunIdStr = urunIdField.getText().trim();

			if (urunIdStr.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Lütfen silmek istediğiniz ürünün ID'sini girin!", "Hata",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				int urunId = Integer.parseInt(urunIdStr);
				urunSil(urunId);
				JOptionPane.showMessageDialog(this, "Ürün başarıyla silindi!");
				urunIdField.setText("");
				urunleriYukle(); // Tablodaki verileri yenile
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Geçerli bir ID girin!", "Hata", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Ürün Güncelle Butonu İşlevi
		urunGuncelleButton.addActionListener(e -> {
			String urunIdStr = urunIdField.getText().trim();
			String yeniUrunAdi = urunAdiField.getText().trim();
			String yeniUrunFiyatiStr = urunFiyatiField.getText().trim();
			String yeniKategori = (String) kategoriComboBox.getSelectedItem();

			if (urunIdStr.isEmpty() || yeniUrunAdi.isEmpty() || yeniUrunFiyatiStr.isEmpty() || yeniKategori == null) {
				JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				int urunId = Integer.parseInt(urunIdStr);
				double yeniUrunFiyati = Double.parseDouble(yeniUrunFiyatiStr);
				urunGuncelle(urunId, yeniUrunAdi, yeniUrunFiyati, yeniKategori);
				JOptionPane.showMessageDialog(this, "Ürün başarıyla güncellendi!");
				urunIdField.setText("");
				urunAdiField.setText("");
				urunFiyatiField.setText("");
				urunleriYukle(); // Tablodaki verileri yenile
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Geçerli bir ID ve fiyat girin!", "Hata",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// Geri Butonu İşlevi
		geriButton.addActionListener(e -> {
			dispose();
			new YoneticiMenu();
		});

		add(panel);
		setVisible(true);
	}

	private void urunleriYukle() {
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT id, isim, fiyat, kategori FROM urunler");
				ResultSet rs = ps.executeQuery()) {

			tableModel.setRowCount(0); // Mevcut tabloyu temizle

			while (rs.next()) {
				int id = rs.getInt("id");
				String isim = rs.getString("isim");
				double fiyat = rs.getDouble("fiyat");
				String kategori = rs.getString("kategori");
				tableModel.addRow(new Object[] { id, isim, fiyat, kategori });
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Ürünler yüklenirken hata oluştu: " + e.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void urunEkle(String urunAdi, double urunFiyati, String kategori) {
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO urunler (isim, fiyat, kategori) VALUES (?, ?, ?)");) {
			ps.setString(1, urunAdi);
			ps.setDouble(2, urunFiyati);
			ps.setString(3, kategori);
			ps.executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Ürün eklenirken hata oluştu: " + e.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void urunSil(int urunId) {
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM urunler WHERE id = ?");) {
			ps.setInt(1, urunId);
			ps.executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Ürün silinirken hata oluştu: " + e.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void urunGuncelle(int urunId, String yeniUrunAdi, double yeniUrunFiyati, String yeniKategori) {
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("UPDATE urunler SET isim = ?, fiyat = ?, kategori = ? WHERE id = ?");) {
			ps.setString(1, yeniUrunAdi);
			ps.setDouble(2, yeniUrunFiyati);
			ps.setString(3, yeniKategori);
			ps.setInt(4, urunId);
			ps.executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Ürün güncellenirken hata oluştu: " + e.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(UrunIslemleri::new);
	}
}
