// Bagian NIM 102042300020 – Ibnu Augustiana (Autentikasi & Session)
package com.kelompok4.inventaris.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity User -> tabel "tb_user_020"
 * Kolom: id, username, password
 * Username dibuat UNIQUE lewat index agar tidak ada username ganda.
 */
@Entity(
    tableName = "tb_user_020",
    indices = [Index(value = ["username"], unique = true)]
)
data class User020(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id020: Int = 0,

    @ColumnInfo(name = "username")
    val username020: String,

    // Disimpan dalam bentuk hash SHA-256 (bukan teks asli)
    @ColumnInfo(name = "password")
    val password020: String
)
