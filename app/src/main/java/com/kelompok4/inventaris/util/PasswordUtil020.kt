// Bagian NIM 102042300020 – Ibnu Augustiana (Autentikasi & Session)
package com.kelompok4.inventaris.util

import java.security.MessageDigest

/** Hash password dengan SHA-256 supaya password tidak disimpan sebagai teks asli */
fun hashPassword020(password020: String): String {
    val bytes020 = MessageDigest.getInstance("SHA-256")
        .digest(password020.toByteArray(Charsets.UTF_8))
    return bytes020.joinToString("") { "%02x".format(it) }
}
