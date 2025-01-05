package com.cafeautomation;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.cafeautomation.giris.GirisTuruSecim;
import com.cafeautomation.urun.UrunIslemleri;

public class Main {
	public static void main(String[] args) {
		// Nimbus görünümünü ayarla
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Uygulamayı başlat
		SwingUtilities.invokeLater(GirisTuruSecim::new);
		
//		public static void main(String[] args) {
//			SwingUtilities.invokeLater(UrunIslemleri::new);
//		}

	}
}
