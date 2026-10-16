package com.practice.pages;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Page Object Model untuk Halaman Sukses Login.
 * Menyimpan locator dan aksi setelah login sukses dilakukan.
 */
public class LoginSuccessPage {
    private static final Logger log = LogManager.getLogger(LoginSuccessPage.class);
    private final Page page;

    // 1. Locators (Selector)
    private final String headerTitle = "h1.post-title";
    private final String successMsg = "p.has-text-align-center strong";
    private final String logoutBtn = "text=Log out";

    // Constructor
    public LoginSuccessPage(Page page) {
        this.page = page;
    }

    /**
     * Mengambil URL halaman saat ini untuk validasi.
     */
    public String getCurrentUrl() {
        String url = page.url();
        log.info("Mendapatkan URL saat ini: {}", url);
        return url;
    }

    /**
     * Memeriksa apakah header sukses login ditampilkan.
     */
    public boolean isHeaderTitleDisplayed() {
        boolean visible = page.isVisible(headerTitle);
        log.info("Apakah header title tampil? {}", visible);
        return visible;
    }

    /**
     * Mengambil teks dari header halaman.
     */
    public String getHeaderTitleText() {
        String text = page.textContent(headerTitle);
        log.info("Mendapatkan teks header: '{}'", text);
        return text != null ? text.trim() : "";
    }

    /**
     * Mengambil teks ucapan selamat (congratulations).
     */
    public String getSuccessMessageText() {
        String text = page.textContent(successMsg);
        log.info("Mendapatkan pesan sukses: '{}'", text);
        return text != null ? text.trim() : "";
    }

    /**
     * Memeriksa apakah tombol Log out ditampilkan.
     */
    public boolean isLogoutButtonDisplayed() {
        boolean visible = page.isVisible(logoutBtn);
        log.info("Apakah tombol logout tampil? {}", visible);
        return visible;
    }

    /**
     * Melakukan klik pada tombol Log out.
     */
    public void clickLogout() {
        log.info("Klik tombol Log out.");
        page.click(logoutBtn);
    }
}
