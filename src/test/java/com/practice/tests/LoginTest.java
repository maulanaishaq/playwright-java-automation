package com.practice.tests;

import com.practice.pages.LoginPage;
import com.practice.pages.LoginSuccessPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Kelas Test untuk fungsionalitas Login.
 * Mewarisi BaseTest untuk setup & teardown browser otomatis.
 */
public class LoginTest extends BaseTest {
    private static final Logger log = LogManager.getLogger(LoginTest.class);

    /**
     * Test Case 1: Positive LogIn test
     * Menguji login dengan username dan password yang benar.
     */
    @Test(priority = 1, description = "Verifikasi login berhasil dengan kredensial yang valid.")
    public void testSuccessfulLogin() {
        log.info("Memulai Test Case: testSuccessfulLogin");
        
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateTo(baseUrl);

        // Lakukan login dengan kredensial yang benar
        loginPage.doLogin("student", "Password123");

        LoginSuccessPage successPage = new LoginSuccessPage(page);
        
        // 1. Verifikasi URL baru mengandung path sukses
        String expectedUrlPart = "logged-in-successfully";
        Assert.assertTrue(successPage.getCurrentUrl().contains(expectedUrlPart), 
                "URL setelah login tidak sesuai! URL saat ini: " + successPage.getCurrentUrl());

        // 2. Verifikasi header sukses login ditampilkan
        Assert.assertTrue(successPage.isHeaderTitleDisplayed(), "Header sukses tidak ditampilkan!");
        Assert.assertEquals(successPage.getHeaderTitleText(), "Logged In Successfully", 
                "Teks header sukses tidak cocok!");

        // 3. Verifikasi teks pesan selamat datang
        String expectedWelcomeMsg = "Congratulations student. You successfully logged in!";
        Assert.assertEquals(successPage.getSuccessMessageText(), expectedWelcomeMsg, 
                "Pesan selamat datang tidak cocok!");

        // 4. Verifikasi tombol Logout ditampilkan
        Assert.assertTrue(successPage.isLogoutButtonDisplayed(), "Tombol logout tidak ditampilkan!");

        // Logout untuk membersihkan status session
        successPage.clickLogout();
        log.info("Test Case testSuccessfulLogin selesai dan sukses.");
    }

    /**
     * Test Case 2: Negative Username test
     * Menguji login dengan username yang salah tetapi password benar.
     */
    @Test(priority = 2, description = "Verifikasi login gagal dengan username salah.")
    public void testInvalidUsernameLogin() {
        log.info("Memulai Test Case: testInvalidUsernameLogin");
        
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateTo(baseUrl);

        // Lakukan login dengan username salah
        loginPage.doLogin("invalidUser", "Password123");

        // 1. Verifikasi error message muncul
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Pesan error tidak ditampilkan!");

        // 2. Verifikasi isi teks error
        String expectedError = "Your username is invalid!";
        Assert.assertEquals(loginPage.getErrorMessageText(), expectedError, 
                "Pesan error tidak sesuai!");
        
        log.info("Test Case testInvalidUsernameLogin selesai dan sukses.");
    }

    /**
     * Test Case 3: Negative Password test
     * Menguji login dengan username benar tetapi password salah.
     */
    @Test(priority = 3, description = "Verifikasi login gagal dengan password salah.")
    public void testInvalidPasswordLogin() {
        log.info("Memulai Test Case: testInvalidPasswordLogin");
        
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateTo(baseUrl);

        // Lakukan login dengan password salah
        loginPage.doLogin("student", "incorrectPassword");

        // 1. Verifikasi error message muncul
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Pesan error tidak ditampilkan!");

        // 2. Verifikasi isi teks error
        String expectedError = "Your password is invalid!";
        Assert.assertEquals(loginPage.getErrorMessageText(), expectedError, 
                "Pesan error tidak sesuai!");
        
        log.info("Test Case testInvalidPasswordLogin selesai dan sukses.");
    }


    /**
     * Test Case 4: Negative Empty Username test
     * Menguji login dengan username kosong.
     */
    @Test(priority = 4, description = "Verifikasi login gagal dengan username kosong.")
    public void testEmptyUsernameLogin() {
        log.info("Memulai Test Case: testEmptyUsernameLogin");
        
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateTo(baseUrl);

        // Biarkan username kosong dan isi password
        loginPage.doLogin("", "Password123");

        // Verifikasi error message muncul
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Pesan error tidak ditampilkan!");
        Assert.assertEquals(loginPage.getErrorMessageText(), "Your username is invalid!", 
                "Pesan error tidak sesuai!");
        
        log.info("Test Case testEmptyUsernameLogin selesai dan sukses.");
    }

    /**
     * Test Case 5: Negative Empty Password test
     * Menguji login dengan username benar tetapi password dikosongkan.
     */
    @Test(priority = 5, description = "Verifikasi login gagal dengan password kosong.")
    public void testEmptyPasswordLogin() {
        log.info("Memulai Test Case: testEmptyPasswordLogin");
        
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigateTo(baseUrl);

        // Masukkan username valid tapi kosongkan password
        loginPage.doLogin("student", "");

        // Verifikasi error message muncul
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Pesan error tidak ditampilkan!");
        Assert.assertEquals(loginPage.getErrorMessageText(), "Your password is invalid!", 
                "Pesan error tidak sesuai!");
        
        log.info("Test Case testEmptyPasswordLogin selesai dan sukses.");
    }
}
