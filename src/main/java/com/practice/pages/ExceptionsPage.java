package com.practice.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Page Object Model untuk halaman Test Exceptions (Dynamic Controls).
 * URL: https://practicetestautomation.com/practice-test-exceptions/
 * 
 * Halaman ini memiliki elemen dinamis (loading 5 detik), penambahan baris baru (Row 2),
 * edit teks makanan, dan konfirmasi penyimpanan/penghapusan.
 */
public class ExceptionsPage {
    private static final Logger log = LogManager.getLogger(ExceptionsPage.class);
    private final Page page;

    // Locators Global & Row 1
    private final String addBtn = "#add_btn";
    private final String instructions = "#instructions";
    private final String loadingSpinner = "#loading";
    private final String confirmationMsg = "#confirmation";
    private final String row1Input = "#row1 input";
    private final String row1EditBtn = "#row1 #edit_btn";
    private final String row1SaveBtn = "#row1 #save_btn";

    // Locators Row 2 (muncul dinamis setelah klik tombol Add)
    private final String row2 = "#row2";
    private final String row2Input = "#row2 input";
    private final String row2SaveBtn = "#row2 #save_btn";
    private final String row2EditBtn = "#row2 #edit_btn";
    private final String row2RemoveBtn = "#row2 #remove_btn";

    public ExceptionsPage(Page page) {
        this.page = page;
    }

    /**
     * Membuka halaman Test Exceptions.
     */
    public void navigateTo(String url) {
        log.info("Membuka halaman Exceptions: {}", url);
        page.navigate(url);
    }

    /**
     * Klik tombol Add untuk memicu pemuatan baris baru.
     */
    public void clickAddButton() {
        log.info("Klik tombol Add Row.");
        page.click(addBtn);
    }

    /**
     * Menunggu hingga baris Row 2 tampil di halaman.
     * Playwright auto-wait menunggu elemen muncul dan terlihat.
     */
    public void waitForRow2() {
        log.info("Menunggu baris Row 2 muncul (asynchronous load)...");
        page.waitForSelector(row2Input, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        log.info("Row 2 berhasil muncul di halaman.");
    }

    /**
     * Memeriksa apakah baris Row 2 terlihat di layar.
     */
    public boolean isRow2Displayed() {
        boolean displayed = page.locator(row2).isVisible();
        log.info("Apakah Row 2 tampil? {}", displayed);
        return displayed;
    }

    /**
     * Mengisi nilai input pada baris Row 2.
     */
    public void enterRow2Food(String foodName) {
        log.info("Mengisi nama makanan di Row 2: {}", foodName);
        page.fill(row2Input, foodName);
    }

    /**
     * Mendapatkan nilai teks saat ini dari input Row 2.
     */
    public String getRow2InputValue() {
        return page.inputValue(row2Input);
    }

    /**
     * Memeriksa apakah input Row 2 berstatus disabled.
     */
    public boolean isRow2InputDisabled() {
        return page.locator(row2Input).isDisabled();
    }

    /**
     * Klik tombol Save pada Row 2.
     */
    public void clickRow2SaveButton() {
        log.info("Klik tombol Save pada Row 2.");
        page.click(row2SaveBtn);
    }

    /**
     * Klik tombol Remove pada Row 2.
     */
    public void clickRow2RemoveButton() {
        log.info("Klik tombol Remove pada Row 2.");
        page.click(row2RemoveBtn);
    }

    /**
     * Klik tombol Edit pada Row 1.
     */
    public void clickRow1EditButton() {
        log.info("Klik tombol Edit pada Row 1.");
        page.click(row1EditBtn);
    }

    /**
     * Memeriksa apakah input Row 1 berstatus disabled (tidak bisa diedit).
     */
    public boolean isRow1InputDisabled() {
        boolean disabled = page.locator(row1Input).isDisabled();
        log.info("Apakah input Row 1 berstatus disabled? {}", disabled);
        return disabled;
    }

    /**
     * Mengubah nilai input makanan pada Row 1.
     */
    public void enterRow1Food(String foodName) {
        log.info("Mengubah nama makanan di Row 1 menjadi: {}", foodName);
        page.fill(row1Input, foodName);
    }

    /**
     * Mendapatkan nilai teks saat ini dari input Row 1.
     */
    public String getRow1InputValue() {
        return page.inputValue(row1Input);
    }

    /**
     * Klik tombol Save pada Row 1.
     */
    public void clickRow1SaveButton() {
        log.info("Klik tombol Save pada Row 1.");
        page.click(row1SaveBtn);
    }

    /**
     * Memeriksa apakah tombol Save pada Row 1 terlihat.
     */
    public boolean isRow1SaveButtonVisible() {
        return page.locator(row1SaveBtn).isVisible();
    }

    /**
     * Mendapatkan teks pesan konfirmasi (e.g. 'Row 2 was added', 'Row 1 was saved', dll).
     */
    public String getConfirmationMessage() {
        // Tunggu teks konfirmasi muncul
        page.waitForSelector(confirmationMsg, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        String text = page.textContent(confirmationMsg).trim();
        log.info("Mendapatkan pesan konfirmasi: '{}'", text);
        return text;
    }

    /**
     * Memeriksa apakah instruksi teks masih ada atau sudah dihapus/tersembunyi.
     */
    public boolean isInstructionsVisible() {
        Locator instructionLocator = page.locator(instructions);
        return instructionLocator.count() > 0 && instructionLocator.isVisible();
    }
}
