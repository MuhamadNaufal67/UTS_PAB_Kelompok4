// Bagian NIM 102042300005 – Zabila Nanda Irawan (Input Validation & Testing)
package com.kelompok4.inventaris.util

import com.kelompok4.inventaris.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit Testing untuk Validasi Input Barang (NIM 005 - Zabila Nanda Irawan)
 */
class ValidationUtil005Test {

    @Test
    fun testValidateNamaBarang005_EmptyOrNull_ReturnsError() {
        assertEquals(R.string.err_nama_kosong, ValidationUtil005.validateNamaBarang005(""))
        assertEquals(R.string.err_nama_kosong, ValidationUtil005.validateNamaBarang005("   "))
        assertEquals(R.string.err_nama_kosong, ValidationUtil005.validateNamaBarang005(null))
    }

    @Test
    fun testValidateNamaBarang005_Valid_ReturnsNull() {
        assertNull(ValidationUtil005.validateNamaBarang005("Laptop Asus"))
    }

    @Test
    fun testValidateJumlahBarang005_InvalidOrZero_ReturnsError() {
        assertEquals(R.string.err_jumlah_kosong, ValidationUtil005.validateJumlahBarang005(""))
        assertEquals(R.string.err_angka_tidak_valid, ValidationUtil005.validateJumlahBarang005("0"))
        assertEquals(R.string.err_angka_tidak_valid, ValidationUtil005.validateJumlahBarang005("-5"))
        assertEquals(R.string.err_angka_tidak_valid, ValidationUtil005.validateJumlahBarang005("abc"))
    }

    @Test
    fun testValidateJumlahBarang005_Positive_ReturnsNull() {
        assertNull(ValidationUtil005.validateJumlahBarang005("10"))
    }

    @Test
    fun testValidateHargaBarang005_InvalidOrZero_ReturnsError() {
        assertEquals(R.string.err_harga_kosong, ValidationUtil005.validateHargaBarang005(""))
        assertEquals(R.string.err_harga_nol, ValidationUtil005.validateHargaBarang005("0"))
        assertEquals(R.string.err_harga_nol, ValidationUtil005.validateHargaBarang005("-1000"))
    }

    @Test
    fun testValidateHargaBarang005_Positive_ReturnsNull() {
        assertNull(ValidationUtil005.validateHargaBarang005("150000"))
    }

    @Test
    fun testValidateItemInput005_CompleteValidation() {
        val invalidResult005 = ValidationUtil005.validateItemInput005("", "0", "-500")
        assertFalse(invalidResult005.isValid005)
        assertEquals(R.string.err_nama_kosong, invalidResult005.errorNama005)
        assertEquals(R.string.err_angka_tidak_valid, invalidResult005.errorJumlah005)
        assertEquals(R.string.err_harga_nol, invalidResult005.errorHarga005)

        val validResult005 = ValidationUtil005.validateItemInput005("Mouse Wireless", "5", "125000")
        assertTrue(validResult005.isValid005)
        assertNull(validResult005.errorNama005)
        assertNull(validResult005.errorJumlah005)
        assertNull(validResult005.errorHarga005)
    }
}
