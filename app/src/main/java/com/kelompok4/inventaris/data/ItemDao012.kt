// Bagian NIM 102042330012 – Muhammad Syuja Al Abid (Room Database & Dashboard)
package com.kelompok4.inventaris.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO Barang. SEMUA query memakai filter user_id
 * agar setiap user hanya bisa melihat / mengubah datanya sendiri.
 */
@Dao
interface ItemDao012 {

    // CREATE
    @Insert
    suspend fun insertItem012(item012: Item012): Long

    // READ (list + pencarian berdasarkan nama). Keyword kosong = tampilkan semua.
    @Query(
        "SELECT * FROM tb_item_012 " +
            "WHERE user_id = :userId012 AND nama LIKE '%' || :keyword012 || '%' " +
            "ORDER BY nama COLLATE NOCASE ASC"
    )
    fun getItemsByUser012(userId012: Int, keyword012: String): Flow<List<Item012>>

    // READ satu data (untuk form edit) – tetap dicek pemiliknya
    @Query("SELECT * FROM tb_item_012 WHERE id = :id012 AND user_id = :userId012 LIMIT 1")
    suspend fun getItemById012(id012: Int, userId012: Int): Item012?

    // UPDATE
    @Update
    suspend fun updateItem012(item012: Item012): Int

    // DELETE – hanya terhapus jika barang memang milik user yang login
    @Query("DELETE FROM tb_item_012 WHERE id = :id012 AND user_id = :userId012")
    suspend fun deleteItemById012(id012: Int, userId012: Int): Int

    // BONUS: ringkasan & total nilai inventaris (jumlah x harga) secara real-time
    @Query(
        "SELECT COUNT(*) AS total_jenis, " +
            "COALESCE(SUM(jumlah), 0) AS total_stok, " +
            "COALESCE(SUM(jumlah * harga), 0) AS total_nilai " +
            "FROM tb_item_012 WHERE user_id = :userId012"
    )
    fun getSummary012(userId012: Int): Flow<InventorySummary012>
}
