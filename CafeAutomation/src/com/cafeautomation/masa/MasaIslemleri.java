package com.cafeautomation.masa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.cafeautomation.database.DatabaseConnection;
import com.cafeautomation.menu.YoneticiMenu;

public class MasaIslemleri extends JFrame {
	public MasaIslemleri() {
		setTitle("Masa Seçim");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(10, 10));

		// Üstteki başlık
		JLabel titleLabel = new JLabel("Masaların Durumu:", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		// Masalar paneli
		JPanel masalarPanel = new JPanel(new GridLayout(10, 10, 10, 10)); // 10x10 grid
		masalariYukle(masalarPanel);
		mainPanel.add(masalarPanel, BorderLayout.CENTER);

		// Alt butonlar paneli
		JPanel buttonPanel = new JPanel();
		JButton masaEkleButton = new JButton("Masa Ekle");
		JButton masaSilButton = new JButton("Masa Sil");
		JButton bosMasaYapButonu = new JButton("Boş Masa Yap");
		JButton geriButonu = new JButton("Geri");

		geriButonu.addActionListener(e -> {
			dispose();
			new YoneticiMenu();
		});

		masaEkleButton.addActionListener(e -> masaEkle());
		masaSilButton.addActionListener(e -> masaSil());
		bosMasaYapButonu.addActionListener(e -> bosMasaYap());

		buttonPanel.add(geriButonu);
		buttonPanel.add(masaEkleButton);
		buttonPanel.add(masaSilButton);
		buttonPanel.add(bosMasaYapButonu);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
		setVisible(true);
	}

	private void masalariYukle(JPanel panel) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT * FROM masalar ORDER BY masa_no";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int masaNo = rs.getInt("masa_no");
				String durum = rs.getString("durum");

				JButton masaButton = new JButton("Masa " + masaNo);
				masaButton.setFont(new Font("Arial", Font.PLAIN, 12));

				// Renk ve işlev ayarı
				if (durum.equals("Dolu")) {
					masaButton.setBackground(Color.RED);
					masaButton.addActionListener(e -> urunYonetimi(masaNo));
				} else {
					masaButton.setBackground(Color.GREEN);
					masaButton.addActionListener(e -> masaSecildi(masaNo));
				}

				panel.add(masaButton);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void masaSecildi(int masaNo) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "UPDATE masalar SET durum = 'Dolu' WHERE masa_no = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, masaNo);
			ps.executeUpdate();

			JOptionPane.showMessageDialog(null, "Masa " + masaNo + " seçildi. Sipariş vermeye başlayabilirsiniz.");
			urunYonetimi(masaNo); // Ürün yönetimine geçiş
			dispose();
			new MasaIslemleri();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void urunYonetimi(int masaNo) {
		JFrame urunYonetimiFrame = new JFrame("Masa " + masaNo + " Ürün Yönetimi");
		urunYonetimiFrame.setSize(600, 400);
		urunYonetimiFrame.setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		JTextArea urunListesi = new JTextArea();
		urunListesi.setEditable(false);
		mainPanel.add(new JScrollPane(urunListesi), BorderLayout.CENTER);

		// Ürün ComboBox
		JPanel urunPanel = new JPanel(new GridLayout(5, 0, 10, 10));
		JComboBox<String> urunComboBox = new JComboBox<>();
		urunleriComboBoxaYukle(urunComboBox);

		// Ürün ekleme ve işlemler
		JButton urunEkleButton = new JButton("Ürün Ekle");
		JButton urunSilButton = new JButton("Ürün Sil");
		JButton miktarArttirButton = new JButton("Miktar Arttır");
		JButton miktarAzaltButton = new JButton("Miktar Azalt");

		urunEkleButton.addActionListener(e -> urunEkle(masaNo, urunComboBox, urunListesi));
		miktarArttirButton.addActionListener(e -> urunMiktarDegistir(masaNo, urunComboBox, urunListesi, true));
		miktarAzaltButton.addActionListener(e -> urunMiktarDegistir(masaNo, urunComboBox, urunListesi, false));
		urunSilButton.addActionListener(e -> urunSil(masaNo, urunComboBox, urunListesi));

		urunPanel.add(new JLabel("Ürün Seç:"));
		urunPanel.add(urunComboBox);
		urunPanel.add(urunEkleButton);
		urunPanel.add(urunSilButton);
		urunPanel.add(miktarArttirButton);
		urunPanel.add(miktarAzaltButton);

		mainPanel.add(urunPanel, BorderLayout.SOUTH);

		urunYonetimiFrame.add(mainPanel);
		urunYonetimiFrame.setVisible(true);

		urunleriGuncelle(masaNo, urunListesi);
	}

	private void urunleriComboBoxaYukle(JComboBox<String> comboBox) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT isim FROM urunler";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				comboBox.addItem(rs.getString("isim"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void urunEkle(int masaNo, JComboBox<String> urunComboBox, JTextArea urunListesi) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String urun = (String) urunComboBox.getSelectedItem();
			int miktar = Integer.parseInt(JOptionPane.showInputDialog("Miktar Girin:"));

			String query = "INSERT INTO siparisler (masa_no, urun_id, adet, durum) "
					+ "SELECT ?, id, ?, 'ONAY_BEKLIYOR' FROM urunler WHERE isim = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, masaNo);
			ps.setInt(2, miktar);
			ps.setString(3, urun);
			ps.executeUpdate();

			JOptionPane.showMessageDialog(null, urun + " başarıyla eklendi!");
			urunleriGuncelle(masaNo, urunListesi);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void urunMiktarDegistir(int masaNo, JComboBox<String> urunComboBox, JTextArea urunListesi, boolean arttir) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String urun = (String) urunComboBox.getSelectedItem();
			int miktarDegisim = Integer.parseInt(JOptionPane.showInputDialog("Değişim miktarını girin:"));
			if (!arttir)
				miktarDegisim = -miktarDegisim;

			String query = "UPDATE siparisler s " + "JOIN urunler u ON s.urun_id = u.id "
					+ "SET s.adet = s.adet + ?, s.durum = 'ONAY_BEKLIYOR' " + "WHERE s.masa_no = ? AND u.isim = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, miktarDegisim);
			ps.setInt(2, masaNo);
			ps.setString(3, urun);
			int affectedRows = ps.executeUpdate();

			if (affectedRows > 0) {
				JOptionPane.showMessageDialog(null, "Ürün miktarı başarıyla güncellendi!");
				urunleriGuncelle(masaNo, urunListesi);
			} else {
				JOptionPane.showMessageDialog(null, "Ürün bulunamadı veya miktar güncellenemedi.", "Hata",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void urunSil(int masaNo, JComboBox<String> urunComboBox, JTextArea urunListesi) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String urun = (String) urunComboBox.getSelectedItem();

			String query = "DELETE s FROM siparisler s " + "JOIN urunler u ON s.urun_id = u.id "
					+ "WHERE s.masa_no = ? AND u.isim = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, masaNo);
			ps.setString(2, urun);
			ps.executeUpdate();

			JOptionPane.showMessageDialog(null, urun + " başarıyla silindi!");
			urunleriGuncelle(masaNo, urunListesi);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void urunleriGuncelle(int masaNo, JTextArea urunListesi) {
		StringBuilder urunBilgileri = new StringBuilder("Masa " + masaNo + " Ürün Listesi:\n");
		double toplamTutar = 0;

		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT u.isim AS urun_adi, s.adet, (u.fiyat * s.adet) AS toplam_fiyat "
					+ "FROM siparisler s " + "JOIN urunler u ON s.urun_id = u.id " + "WHERE s.masa_no = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, masaNo);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String urunAdi = rs.getString("urun_adi");
				int adet = rs.getInt("adet");
				double toplamFiyat = rs.getDouble("toplam_fiyat");

				urunBilgileri.append("Ürün: ").append(urunAdi).append(", Adet: ").append(adet)
						.append(", Toplam Fiyat: ").append(toplamFiyat).append(" TL\n");

				toplamTutar += toplamFiyat;
			}

			urunBilgileri.append("\nToplam Tutar: ").append(toplamTutar).append(" TL");
			urunListesi.setText(urunBilgileri.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void masaEkle() {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT MAX(masa_no) AS max_masa FROM masalar";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			int yeniMasaNo = 1;
			if (rs.next()) {
				yeniMasaNo = rs.getInt("max_masa") + 1;
			}

			query = "INSERT INTO masalar (masa_no, durum) VALUES (?, 'Boş')";
			ps = conn.prepareStatement(query);
			ps.setInt(1, yeniMasaNo);
			ps.executeUpdate();

			JOptionPane.showMessageDialog(null, "Masa " + yeniMasaNo + " başarıyla eklendi!");
			dispose();
			new MasaIslemleri(); // Sayfayı yenile
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void masaSil() {
		try {
			int masaNo = Integer.parseInt(JOptionPane.showInputDialog("Silmek istediğiniz masanın numarasını girin:"));

			// Onay mesajı
			int confirm = JOptionPane.showConfirmDialog(null, "Masa " + masaNo + " silinecek. Emin misiniz?", "Onay",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try (Connection conn = DatabaseConnection.getConnection()) {
					// Masa silmeden önce ilgili siparişleri sil
					String deleteSiparisQuery = "DELETE FROM siparisler WHERE masa_no = ?";
					PreparedStatement deleteSiparisStmt = conn.prepareStatement(deleteSiparisQuery);
					deleteSiparisStmt.setInt(1, masaNo);
					deleteSiparisStmt.executeUpdate();

					// Masa tablosundan masayı sil
					String deleteMasaQuery = "DELETE FROM masalar WHERE masa_no = ?";
					PreparedStatement deleteMasaStmt = conn.prepareStatement(deleteMasaQuery);
					deleteMasaStmt.setInt(1, masaNo);
					int affectedRows = deleteMasaStmt.executeUpdate();

					if (affectedRows > 0) {
						JOptionPane.showMessageDialog(null, "Masa " + masaNo + " başarıyla silindi!");
						dispose();
						new MasaIslemleri(); // Sayfayı yenile
					} else {
						JOptionPane.showMessageDialog(null, "Masa bulunamadı. Geçerli bir masa numarası girin.", "Hata",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Geçersiz masa numarası girdiniz!", "Hata", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Bir hata oluştu: " + e.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void bosMasaYap() {
		try (Connection conn = DatabaseConnection.getConnection()) {
			int masaNo = Integer
					.parseInt(JOptionPane.showInputDialog("Boşa almak istediğiniz masanın numarasını girin:"));
			// Onay mesajı
			int confirm = JOptionPane.showConfirmDialog(null,
					"Masa " + masaNo + " boş olarak ayarlanacak ve siparişler silinecek. Emin misiniz?", "Onay",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				// Siparişleri sil
				String deleteSiparisQuery = "DELETE FROM siparisler WHERE masa_no = ?";
				PreparedStatement deleteSiparisStmt = conn.prepareStatement(deleteSiparisQuery);
				deleteSiparisStmt.setInt(1, masaNo);
				deleteSiparisStmt.executeUpdate();

				// Masayı "Boş" olarak güncelle
				String updateMasaQuery = "UPDATE masalar SET durum = 'Boş' WHERE masa_no = ?";
				PreparedStatement updateMasaStmt = conn.prepareStatement(updateMasaQuery);
				updateMasaStmt.setInt(1, masaNo);
				int affectedRows = updateMasaStmt.executeUpdate();

				if (affectedRows > 0) {
					JOptionPane.showMessageDialog(null, "Masa " + masaNo + " artık boş!");
					dispose();
					new MasaIslemleri(); // Sayfayı yenile
				} else {
					JOptionPane.showMessageDialog(null, "Masa bulunamadı. Geçerli bir masa numarası girin.", "Hata",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Bir hata oluştu: " + e.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MasaIslemleri::new);
	}
}
