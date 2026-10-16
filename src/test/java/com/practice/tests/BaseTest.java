package com.practice.tests;

import com.microsoft.playwright.Page;
import com.practice.utils.ConfigReader;
import com.practice.utils.PlaywrightFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base Test class. Semua kelas test harus mewarisi (extends) kelas ini.
 * Mengatur siklus hidup browser untuk setiap test method.
 */
public class BaseTest {
    protected Page page;
    protected String baseUrl;

    /**
     * Berjalan sebelum setiap metode test dimulai.
     * Menginisialisasi browser dan mengambil base URL dari config.properties.
     */
    @BeforeMethod
    public void setUp() {
        // Menginisialisasi browser, context, dan page
        page = PlaywrightFactory.initBrowser();
        // Mengambil target URL dari properti global
        baseUrl = ConfigReader.getProperty("url");
    }

    /**
     * Berjalan setelah setiap metode test selesai.
     * Menutup browser dan membersihkan resource Playwright yang digunakan.
     */
    @AfterMethod
    public void tearDown() {
        // Menutup driver untuk thread saat ini
        PlaywrightFactory.closeBrowser();
    }
}
