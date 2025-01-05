CREATE DATABASE cafe_automation;
use cafe_automation;



CREATE TABLE `kullanicilar` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `sifre` varchar(100) NOT NULL,
  `kullanici_tipi` enum('Yonetici','Musteri') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `masalar` (
  `id` int NOT NULL AUTO_INCREMENT,
  `masa_no` int NOT NULL,
  `durum` enum('Boş','Dolu') DEFAULT 'Boş',
  PRIMARY KEY (`id`),
  UNIQUE KEY `masa_no` (`masa_no`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `urunler` (
  `id` int NOT NULL AUTO_INCREMENT,
  `kategori` varchar(50) NOT NULL,
  `isim` varchar(100) NOT NULL,
  `fiyat` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `siparisler` (
  `id` int NOT NULL AUTO_INCREMENT,
  `masa_no` varchar(10) NOT NULL,
  `urun_id` int NOT NULL,
  `adet` int DEFAULT '1',
  `durum` enum('ONAY_BEKLIYOR','ONAYLANDI','IPTAL_EDILDI') DEFAULT 'ONAY_BEKLIYOR',
  PRIMARY KEY (`id`),
  KEY `urun_id` (`urun_id`),
  CONSTRAINT `siparisler_ibfk_1` FOREIGN KEY (`urun_id`) REFERENCES `urunler` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




CREATE TABLE `urun_siparis` (
  `id` int NOT NULL AUTO_INCREMENT,
  `siparis_id` int NOT NULL,
  `urun_id` int NOT NULL,
  `adet` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `siparis_id` (`siparis_id`),
  KEY `urun_id` (`urun_id`),
  CONSTRAINT `urun_siparis_ibfk_1` FOREIGN KEY (`siparis_id`) REFERENCES `siparisler` (`id`),
  CONSTRAINT `urun_siparis_ibfk_2` FOREIGN KEY (`urun_id`) REFERENCES `urunler` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `teyit_kodlari` (
  `id` int NOT NULL AUTO_INCREMENT,
  `kod` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO teyit_kodlari (kod) 
VALUES ('777');
SELECT * FROM teyit_kodlari;
INSERT INTO teyit_kodlari (kod) 
VALUES ('999');



