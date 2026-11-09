// Bagian NIM 102042300020 – Ibnu Augustiana (Autentikasi & Session)
package com.kelompok4.inventaris.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao020 {

    // Register: ABORT -> akan melempar exception jika username sudah ada (unique index)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser020(user020: User020): Long

    // Dipakai untuk cek username unik & proses login
    @Query("SELECT * FROM tb_user_020 WHERE username = :username020 COLLATE NOCASE LIMIT 1")
    suspend fun getUserByUsername020(username020: String): User020?
}
