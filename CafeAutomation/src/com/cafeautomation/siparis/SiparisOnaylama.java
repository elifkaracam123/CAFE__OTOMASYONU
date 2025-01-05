package com.cafeautomation.siparis;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.cafeautomation.menu.YoneticiMenu;

public class SiparisOnaylama extends JFrame {
	public SiparisOnaylama() {
		setTitle("Sipariş Onaylama");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
		JLabel siparisLabel = new JLabel("Onaylanacak Siparişler:", SwingConstants.CENTER);
		JTextArea siparisListesi = new JTextArea("Sipariş 1\nSipariş 2\nSipariş 3");
		siparisListesi.setEditable(false);

		JButton onaylaButton = new JButton("Onayla");
		onaylaButton.addActionListener(e -> {
			// Veri tabanında sipariş durumu güncellenecek
			JOptionPane.showMessageDialog(null, "Siparişler onaylandı!");
			dispose();
			new YoneticiMenu();
		});

		JButton geriButton = new JButton("Geri");
		geriButton.addActionListener(e -> {
			dispose();
			new YoneticiMenu();
		});

		panel.add(siparisLabel);
		panel.add(new JScrollPane(siparisListesi));
		panel.add(onaylaButton);
		panel.add(geriButton);
		add(panel);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(SiparisOnaylama::new);
	}
}
