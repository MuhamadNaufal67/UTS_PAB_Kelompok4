// Bagian NIM 102042300005 – Zabila Nanda Irawan (Input Validation & Testing)
package com.kelompok4.inventaris.util

import com.kelompok4.inventaris.R

/**
 * Result data class untuk hasil validasi input barang (NIM 005)
 */
data class ValidationResult005(
    val isValid005: Boolean,
    val errorNama005: Int? = null,
    val errorJumlah005: Int? = null,
    val errorHarga005: Int? = null
)

/**
 * Helper validasi input barang dan pendukung testing (NIM 005 - Zabila Nanda Irawan)
 */
object ValidationUtil005 {

    /**
     * Validasi nama barang tidak boleh kosong atau hanya spasi.
     * @return String resource ID error, atau null jika valid.
     */
    fun validateNamaBarang005(nama005: String?): Int? {
        val trimmed005 = nama005?.trim().orEmpty()
        return if (trimmed005.isEmpty()) {
            R.string.err_nama_kosong
        } else {
            null
        }
    }

    /**
     * Validasi jumlah harus berupa angka positif (> 0).
     * @return String resource ID error, atau null jika valid.
     */
    fun validateJumlahBarang005(jumlahText005: String?): Int? {
        val trimmed005 = jumlahText005?.trim().orEmpty()
        if (trimmed005.isEmpty()) {
            return R.string.err_jumlah_kosong
        }
        val angka005 = trimmed005.toIntOrNull()
        if (angka005 == null || angka005 <= 0) {
            return R.string.err_angka_tidak_valid
        }
        return null
    }

    /**
     * Validasi harga tidak boleh kosong dan harus berupa angka positif (> 0).
     * @return String resource ID error, atau null jika valid.
     */
    fun validateHargaBarang005(hargaText005: String?): Int? {
        val trimmed005 = hargaText005?.trim().orEmpty()
        if (trimmed005.isEmpty()) {
            return R.string.err_harga_kosong
        }
        val harga005 = trimmed005.toLongOrNull()
        if (harga005 == null || harga005 <= 0L) {
            return R.string.err_harga_nol
        }
        return null
    }

    /**
     * Validasi keseluruhan input barang sebelum penyimpanan.
     */
    fun validateItemInput005(
        nama005: String?,
        jumlahText005: String?,
        hargaText005: String?
    ): ValidationResult005 {
        val errNama005 = validateNamaBarang005(nama005)
        val errJumlah005 = validateJumlahBarang005(jumlahText005)
        val errHarga005 = validateHargaBarang005(hargaText005)

        val isValid005 = errNama005 == null && errJumlah005 == null && errHarga005 == null

        return ValidationResult005(
            isValid005 = isValid005,
            errorNama005 = errNama005,
            errorJumlah005 = errJumlah005,
            errorHarga005 = errHarga005
        )
    }
}
