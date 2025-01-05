package com.cafeautomation.siparis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.cafeautomation.database.DatabaseConnection;
import com.cafeautomation.menu.YoneticiMenu;

public class YoneticiSiparisOnay extends JFrame {
	private DefaultTableModel siparisTableModel;
	private JTable siparisTablosu;

	public YoneticiSiparisOnay() {
		setTitle("Yönetici Sipariş Onay");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new BorderLayout());

		// Sipariş Tablosu
		siparisTableModel = new DefaultTableModel(
				new String[] { "Sipariş ID", "Masa No", "Ürün Adı", "Fiyat", "Adet", "Durum" }, 0);
		siparisTablosu = new JTable(siparisTableModel);
		JScrollPane scrollPane = new JScrollPane(siparisTablosu);

		// Siparişleri Veritabanından Yükle
		siparisleriYukle();

		// Alt Panel
		JPanel altPanel = new JPanel(new FlowLayout());

		// Onayla Butonu
		JButton onaylaButton = new JButton("Onayla");
		onaylaButton.addActionListener(this::siparisOnayla);
		altPanel.add(onaylaButton);

		// İptal Et Butonu
		JButton iptalEtButton = new JButton("İptal Et");
		iptalEtButton.addActionListener(this::siparisIptalEt);
		altPanel.add(iptalEtButton);

		// Geri Butonu
		JButton geriButton = new JButton("geri");
		geriButton.addActionListener(e ->{
			dispose();
			new YoneticiMenu();
		}
				);
		altPanel.add(geriButton);

		// Paneli Birleştir
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(altPanel, BorderLayout.SOUTH);

		add(panel);
		setVisible(true);
	}

	private void siparisleriYukle() {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT s.id, s.masa_no, u.isim AS urun_adi, u.fiyat, s.adet, s.durum "
					+ "FROM siparisler s " + "JOIN urunler u ON s.urun_id = u.id " + "WHERE s.durum = 'ONAY_BEKLIYOR'";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			siparisTableModel.setRowCount(0); // Tabloyu temizle
			while (rs.next()) {
				int id = rs.getInt("id");
				String masaNo = rs.getString("masa_no");
				String urunAdi = rs.getString("urun_adi");
				double fiyat = rs.getDouble("fiyat");
				int adet = rs.getInt("adet");
				String durum = rs.getString("durum");

				siparisTableModel.addRow(new Object[] { id, masaNo, urunAdi, fiyat, adet, durum });
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Siparişler yüklenirken hata oluştu: " + e.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void siparisOnayla(ActionEvent e) {
		int selectedRow = siparisTablosu.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Lütfen bir sipariş seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int siparisId = (int) siparisTableModel.getValueAt(selectedRow, 0);
		String masaNo = (String) siparisTableModel.getValueAt(selectedRow, 1);

		try (Connection conn = DatabaseConnection.getConnection()) {
			// Sipariş durumu güncelleme
			String updateSiparisQuery = "UPDATE siparisler SET durum = 'ONAYLANDI' WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(updateSiparisQuery);
			ps.setInt(1, siparisId);
			ps.executeUpdate();

			// Masa durumu güncelleme
			String updateMasaQuery = "UPDATE masalar SET durum = 'DOLU' WHERE masa_no = ?";
			ps = conn.prepareStatement(updateMasaQuery);
			ps.setString(1, masaNo);
			ps.executeUpdate();

			JOptionPane.showMessageDialog(this, "Sipariş başarıyla onaylandı ve masa durumu güncellendi!");
			siparisleriYukle(); // Tabloyu güncelle
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Sipariş onaylanırken hata oluştu: " + ex.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void siparisIptalEt(ActionEvent e) {
		int selectedRow = siparisTablosu.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Lütfen bir sipariş seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int siparisId = (int) siparisTableModel.getValueAt(selectedRow, 0);
		String masaNo = (String) siparisTableModel.getValueAt(selectedRow, 1);

		try (Connection conn = DatabaseConnection.getConnection()) {
			// Sipariş durumu güncelleme
			String updateSiparisQuery = "UPDATE siparisler SET durum = 'IPTAL_EDILDI' WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(updateSiparisQuery);
			ps.setInt(1, siparisId);
			ps.executeUpdate();

			JOptionPane.showMessageDialog(this, "Sipariş başarıyla iptal edildi!");
			siparisleriYukle(); // Tabloyu güncelle
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Sipariş iptal edilirken hata oluştu: " + ex.getMessage(), "Hata",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(YoneticiSiparisOnay::new);
	}
}
