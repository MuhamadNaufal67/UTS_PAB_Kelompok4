// Bagian NIM 102042330012 – Muhammad Syuja Al Abid (Room Database & Dashboard)
package com.kelompok4.inventaris.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity Item (Barang) -> tabel "tb_item_012"
 * Kolom: id, user_id, nama, jumlah, harga
 * user_id adalah FOREIGN KEY ke tb_user_020(id) sehingga
 * setiap barang hanya dimiliki oleh satu user (data tidak bercampur).
 */
@Entity(
    tableName = "tb_item_012",
    foreignKeys = [
        ForeignKey(
            entity = User020::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id"])]
)
data class Item012(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id012: Int = 0,

    @ColumnInfo(name = "user_id")
    val userId012: Int,

    @ColumnInfo(name = "nama")
    val nama012: String,

    @ColumnInfo(name = "jumlah")
    val jumlah012: Int,

    @ColumnInfo(name = "harga")
    val harga012: Long
)
