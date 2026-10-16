package com.practice.pages;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Page Object Model untuk halaman Login.
 * Menyimpan locator dan aksi yang dapat dilakukan di halaman Login.
 */
public class LoginPage {
    private static final Logger log = LogManager.getLogger(LoginPage.class);
    private final Page page;

    // 1. Locators (Selector)
    private final String usernameInput = "#username";
    private final String passwordInput = "#password";
    private final String submitBtn = "#submit";
    private final String errorMessage = "#error";

    // Constructor
    public LoginPage(Page page) {
        this.page = page;
    }

    /**
     * Navigasi ke URL Login.
     */
    public void navigateTo(String url) {
        log.info("Membuka halaman login: {}", url);
        page.navigate(url);
    }

    /**
     * Memasukkan username ke input field.
     */
    public void enterUsername(String username) {
        log.info("Mengisi username: {}", username);
        page.fill(usernameInput, username);
    }

    /**
     * Memasukkan password ke input field.
     */
    public void enterPassword(String password) {
        log.info("Mengisi password: ****");
        page.fill(passwordInput, password);
    }

    /**
     * Klik tombol submit.
     */
    public void clickSubmit() {
        log.info("Klik tombol Submit.");
        page.click(submitBtn);
    }

    /**
     * Melakukan login secara langsung (Helper method).
     */
    public void doLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSubmit();
    }

    /**
     * Memeriksa apakah pesan error ditampilkan.
     */
    public boolean isErrorMessageDisplayed() {
        boolean visible = page.isVisible(errorMessage);
        log.info("Apakah pesan error tampil? {}", visible);
        return visible;
    }

    /**
     * Mengambil teks pesan error.
     */
    public String getErrorMessageText() {
        String text = page.textContent(errorMessage);
        log.info("Mendapatkan teks error: '{}'", text);
        return text != null ? text.trim() : "";
    }
}
