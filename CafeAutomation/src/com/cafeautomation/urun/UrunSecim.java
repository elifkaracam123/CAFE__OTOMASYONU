package com.cafeautomation.urun;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.cafeautomation.database.DatabaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UrunSecim extends JFrame {
    private String masaNo;
    private DefaultTableModel urunTableModel;
    private JTable urunTablosu;
    private JTextField adetField;

    public UrunSecim(String masaNo) {
        this.masaNo = masaNo;
        setTitle("Ürün Seçim - Masa: " + masaNo);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Ürün Tablosu
        urunTableModel = new DefaultTableModel(new String[]{"Ürün ID", "İsim", "Fiyat (TL)", "Kategori"}, 0);
        urunTablosu = new JTable(urunTableModel);
        JScrollPane scrollPane = new JScrollPane(urunTablosu);

        // Ürünleri Veritabanından Yükle
        urunleriYukle();

        // Alt Panel
        JPanel altPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Ürün Adet Girişi
        JPanel adetPanel = new JPanel(new FlowLayout());
        JLabel adetLabel = new JLabel("Adet:");
        adetField = new JTextField(5);
        adetPanel.add(adetLabel);
        adetPanel.add(adetField);
        altPanel.add(adetPanel);

        // Sipariş Ver Butonu
        JButton siparisVerButton = new JButton("Sipariş Ver");
        siparisVerButton.addActionListener(this::siparisVer);
        altPanel.add(siparisVerButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(altPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void urunleriYukle() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id, isim, fiyat, kategori FROM urunler";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String isim = rs.getString("isim");
                double fiyat = rs.getDouble("fiyat");
                String kategori = rs.getString("kategori");
                urunTableModel.addRow(new Object[]{id, isim, fiyat, kategori});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ürünler yüklenirken hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void siparisVer(ActionEvent e) {
        int selectedRow = urunTablosu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir ürün seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String adetStr = adetField.getText();
        if (adetStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen adet girin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int adet = Integer.parseInt(adetStr);
            if (adet <= 0) throw new NumberFormatException();

            int urunId = (int) urunTableModel.getValueAt(selectedRow, 0);
            siparisiKaydet(urunId, adet);
            JOptionPane.showMessageDialog(this, "Sipariş başarıyla eklendi! Yönetici onayı bekleniyor.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir adet girin!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void siparisiKaydet(int urunId, int adet) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO siparisler (masa_no, urun_id, adet, durum) VALUES (?, ?, ?, 'ONAY_BEKLIYOR')";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, masaNo);
            ps.setInt(2, urunId);
            ps.setInt(3, adet);

            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Sipariş kaydedilirken hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UrunSecim("Masa1"));
    }
}
