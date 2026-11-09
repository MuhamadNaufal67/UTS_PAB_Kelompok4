// Bagian NIM 102042330012 – Muhammad Syuja Al Abid (Room Database & Dashboard)
package com.kelompok4.inventaris.data

import androidx.room.ColumnInfo

/**
 * Hasil query ringkasan inventaris untuk Dashboard (Bonus: total nilai real-time).
 */
data class InventorySummary012(
    @ColumnInfo(name = "total_jenis")
    val totalJenis012: Int,

    @ColumnInfo(name = "total_stok")
    val totalStok012: Long,

    @ColumnInfo(name = "total_nilai")
    val totalNilai012: Long
)
