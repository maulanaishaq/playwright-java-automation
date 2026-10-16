package com.practice.tests;

import com.practice.pages.ExceptionsPage;
import com.practice.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test Suite untuk fitur Test Exceptions & Dynamic Controls.
 * Menguji perilaku halaman dengan elemen asinkron, penambahan baris, pengeditan, dan penghapusan.
 */
public class ExceptionsTest extends BaseTest {
    private static final Logger log = LogManager.getLogger(ExceptionsTest.class);
    private String exceptionsUrl;

    @BeforeMethod
    public void setUpExceptionsUrl() {
        exceptionsUrl = ConfigReader.getProperty("url.exceptions");
        if (exceptionsUrl == null || exceptionsUrl.isEmpty()) {
            exceptionsUrl = "https://practicetestautomation.com/practice-test-exceptions/";
        }
    }

    /**
     * Test Case 1 (Positive): Menambah Row 2 dan menyimpan nama makanan.
     */
    @Test(priority = 1, description = "Verifikasi berhasil menambahkan Row 2, mengisi makanan, dan menyimpannya.")
    public void testAddRow2Successfully() {
        log.info("Memulai Test Case: testAddRow2Successfully");
        ExceptionsPage page = new ExceptionsPage(this.page);
        page.navigateTo(exceptionsUrl);

        // 1. Klik Add button
        page.clickAddButton();

        // 2. Tunggu Row 2 muncul (asynchronous loading 5 detik)
        page.waitForRow2();
        Assert.assertTrue(page.isRow2Displayed(), "Baris Row 2 tidak ditampilkan!");

        // 3. Masukkan nama makanan ke Row 2
        String foodName = "Cheeseburger";
        page.enterRow2Food(foodName);

        // 4. Klik Save pada Row 2
        page.clickRow2SaveButton();

        // 5. Verifikasi konfirmasi tersimpan
        String confirmation = page.getConfirmationMessage();
        Assert.assertEquals(confirmation, "Row 2 was saved", "Pesan konfirmasi simpan tidak sesuai!");

        // 6. Verifikasi input field Row 2 menjadi disabled setelah disimpan
        Assert.assertTrue(page.isRow2InputDisabled(), "Input Row 2 seharusnya berstatus disabled setelah disimpan!");
        Assert.assertEquals(page.getRow2InputValue(), foodName, "Nilai makanan pada Row 2 tidak cocok!");

        log.info("Test Case testAddRow2Successfully selesai dan sukses.");
    }

    /**
     * Test Case 2 (Positive): Mengedit nilai makanan di Row 1 dan menyimpannya.
     */
    @Test(priority = 2, description = "Verifikasi pengeditan teks makanan di Row 1 berhasil tersimpan.")
    public void testEditRow1Successfully() {
        log.info("Memulai Test Case: testEditRow1Successfully");
        ExceptionsPage page = new ExceptionsPage(this.page);
        page.navigateTo(exceptionsUrl);

        // 1. Klik tombol Edit di Row 1
        page.clickRow1EditButton();

        // 2. Verifikasi input sekarang aktif dan ubah nilainya
        Assert.assertFalse(page.isRow1InputDisabled(), "Input Row 1 seharusnya aktif setelah tombol Edit ditekan!");
        String newFood = "Japanese Ramen";
        page.enterRow1Food(newFood);

        // 3. Klik Save pada Row 1
        page.clickRow1SaveButton();

        // 4. Verifikasi pesan konfirmasi
        String confirmation = page.getConfirmationMessage();
        Assert.assertEquals(confirmation, "Row 1 was saved", "Pesan konfirmasi simpan Row 1 tidak sesuai!");

        // 5. Verifikasi input kembali disabled dan bernilai baru
        Assert.assertTrue(page.isRow1InputDisabled(), "Input Row 1 seharusnya terkunci (disabled) setelah disimpan!");
        Assert.assertEquals(page.getRow1InputValue(), newFood, "Nilai makanan pada Row 1 tidak terupdate!");

        log.info("Test Case testEditRow1Successfully selesai dan sukses.");
    }

    /**
     * Test Case 3 (Positive): Menghapus baris Row 2 setelah ditambahkan.
     */
    @Test(priority = 3, description = "Verifikasi berhasil menghapus baris Row 2 dengan tombol Remove.")
    public void testRemoveRow2Successfully() {
        log.info("Memulai Test Case: testRemoveRow2Successfully");
        ExceptionsPage page = new ExceptionsPage(this.page);
        page.navigateTo(exceptionsUrl);

        // 1. Tambahkan Row 2 terlebih dahulu
        page.clickAddButton();
        page.waitForRow2();

        // 2. Klik tombol Remove pada Row 2
        page.clickRow2RemoveButton();

        // 3. Verifikasi pesan konfirmasi penghapusan
        String confirmation = page.getConfirmationMessage();
        Assert.assertEquals(confirmation, "Row 2 was removed", "Pesan konfirmasi remove tidak cocok!");

        // 4. Verifikasi Row 2 sudah tidak tampil di layar
        Assert.assertFalse(page.isRow2Displayed(), "Row 2 masih terlihat setelah dihapus!");

        log.info("Test Case testRemoveRow2Successfully selesai dan sukses.");
    }

    /**
     * Test Case 4 (Negative): Memvalidasi kondisi default Row 1 adalah disabled dan tombol Save tersembunyi.
     */
    @Test(priority = 4, description = "Verifikasi input Row 1 terkunci (disabled) dan tombol Save tersembunyi secara default.")
    public void testRow1DefaultStateDisabled() {
        log.info("Memulai Test Case: testRow1DefaultStateDisabled");
        ExceptionsPage page = new ExceptionsPage(this.page);
        page.navigateTo(exceptionsUrl);

        // 1. Input field Row 1 harus berstatus disabled saat awal dibuka
        Assert.assertTrue(page.isRow1InputDisabled(), "Secara default input Row 1 harus disabled!");

        // 2. Tombol Save Row 1 harus tersembunyi secara default
        Assert.assertFalse(page.isRow1SaveButtonVisible(), "Tombol Save pada Row 1 seharusnya tersembunyi secara default!");

        log.info("Test Case testRow1DefaultStateDisabled selesai dan sukses.");
    }

    /**
     * Test Case 5 (Negative / Lifecycle): Memvalidasi teks instruksi terhapus setelah tombol Add diklik.
     */
    @Test(priority = 5, description = "Verifikasi elemen instruksi terhapus dari DOM setelah tombol Add ditekan.")
    public void testInstructionsRemovedAfterAddingRow() {
        log.info("Memulai Test Case: testInstructionsRemovedAfterAddingRow");
        ExceptionsPage page = new ExceptionsPage(this.page);
        page.navigateTo(exceptionsUrl);

        // 1. Sebelum klik Add, instruksi harus tampil
        Assert.assertTrue(page.isInstructionsVisible(), "Instruksi seharusnya terlihat saat halaman pertama kali dibuka!");

        // 2. Klik tombol Add
        page.clickAddButton();

        // 3. Verifikasi elemen instruksi langsung terhapus dari tampilan
        Assert.assertFalse(page.isInstructionsVisible(), "Instruksi seharusnya tidak lagi terlihat setelah tombol Add diklik!");

        log.info("Test Case testInstructionsRemovedAfterAddingRow selesai dan sukses.");
    }
}
