package com.practice.utils;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Factory class to initialize and manage Playwright, Browser, BrowserContext, and Page.
 * Menggunakan ThreadLocal untuk memastikan setiap thread test memiliki instansiasi driver tersendiri (Thread-safe).
 * Ini sangat berguna jika kita ingin menjalankan test secara paralel di masa mendatang.
 */
public class PlaywrightFactory {
    private static final Logger log = LogManager.getLogger(PlaywrightFactory.class);

    // ThreadLocal variables untuk thread safety
    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    /**
     * Menginisialisasi browser berdasarkan konfigurasi dari config.properties.
     * 
     * @return Page object untuk thread saat ini.
     */
    public static Page initBrowser() {
        // Prioritaskan nilai dari parameter CLI (-Dbrowser=... -Dheadless=...) jika ada
        String browserName = System.getProperty("browser", ConfigReader.getProperty("browser"));
        String headlessStr = System.getProperty("headless", ConfigReader.getProperty("headless"));
        boolean isHeadless = headlessStr != null && Boolean.parseBoolean(headlessStr);

        log.info("Menginisialisasi Playwright...");
        Playwright playwright = Playwright.create();
        tlPlaywright.set(playwright);

        Browser browser;
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(isHeadless);

        log.info("Membuka browser '{}' (Headless = {})...", browserName, isHeadless);
        switch (browserName.toLowerCase()) {
            case "chrome":
            case "chromium":
                // Menggunakan Google Chrome yang terinstal secara lokal di sistem untuk menghindari download file 169MB.
                options.setChannel("chrome");
                browser = playwright.chromium().launch(options);
                break;
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                browser = playwright.webkit().launch(options);
                break;
            default:
                log.warn("Browser '{}' tidak dikenal! Defaulting ke Chromium.", browserName);
                browser = playwright.chromium().launch(options);
                break;
        }
        tlBrowser.set(browser);

        log.info("Membuat Browser Context baru...");
        // Disini kita bisa menambahkan konfigurasi viewport, video record, geolocation, dll jika diperlukan.
        BrowserContext context = browser.newContext();
        tlContext.set(context);

        log.info("Membuka tab baru (Page)...");
        Page page = context.newPage();
        
        // Mengatur timeout default untuk element-element jika diset
        String timeoutStr = ConfigReader.getProperty("timeout");
        if (timeoutStr != null) {
            double timeout = Double.parseDouble(timeoutStr);
            page.setDefaultTimeout(timeout);
            log.info("Mengatur default element timeout ke {} ms.", timeout);
        }

        tlPage.set(page);
        log.info("Browser dan Page berhasil disiapkan untuk thread saat ini.");
        return page;
    }

    // Getter untuk Playwright
    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    // Getter untuk Browser
    public static Browser getBrowser() {
        return tlBrowser.get();
    }

    // Getter untuk BrowserContext
    public static BrowserContext getBrowserContext() {
        return tlContext.get();
    }

    // Getter untuk Page
    public static Page getPage() {
        return tlPage.get();
    }

    /**
     * Membersihkan dan menutup resource Playwright setelah pengetesan selesai pada thread saat ini.
     * Sangat penting dipanggil di akhir test untuk mencegah memory leak.
     */
    public static void closeBrowser() {
        log.info("Menutup browser dan melepaskan resource Playwright...");
        
        if (tlPage.get() != null) {
            tlPage.get().close();
            tlPage.remove();
        }
        if (tlContext.get() != null) {
            tlContext.get().close();
            tlContext.remove();
        }
        if (tlBrowser.get() != null) {
            tlBrowser.get().close();
            tlBrowser.remove();
        }
        if (tlPlaywright.get() != null) {
            tlPlaywright.get().close();
            tlPlaywright.remove();
        }
        log.info("Resource Playwright berhasil dibersihkan.");
    }
}
