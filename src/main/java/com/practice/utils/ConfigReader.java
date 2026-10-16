package com.practice.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read properties from config.properties.
 * Ini adalah kelas utilitas untuk membaca konfigurasi dari config.properties.
 */
public class ConfigReader {
    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                log.error("Maaf, config.properties tidak ditemukan di resources path.");
            } else {
                // Load properties file
                properties.load(input);
                log.info("Konfigurasi config.properties berhasil dimuat.");
            }
        } catch (IOException ex) {
            log.error("Terjadi error saat membaca config.properties: ", ex);
        }
    }

    /**
     * Retrieves the property value for a given key.
     * Mengambil nilai properti berdasarkan key.
     *
     * @param key Nama properti
     * @return Nilai properti
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return value.trim();
        }
        log.warn("Properti key '{}' tidak ditemukan atau bernilai null.", key);
        return null;
    }
}
