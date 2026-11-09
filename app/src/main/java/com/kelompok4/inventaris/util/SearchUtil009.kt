// Bagian NIM 102042300009 – Damara Dihyan Darpita (Search & Sorting Barang)
package com.kelompok4.inventaris.util

import com.kelompok4.inventaris.data.Item012

/**
 * Opsi Kriteria Sorting Barang (NIM 009)
 */
enum class SortOption009(val label009: String) {
    NAMA_ASC("Nama A-Z"),
    NAMA_DESC("Nama Z-A"),
    JUMLAH_DESC("Stok Terbanyak"),
    JUMLAH_ASC("Stok Terkecil"),
    HARGA_DESC("Harga Termahal"),
    HARGA_ASC("Harga Termurah")
}

/**
 * Helper pencarian dan sorting barang (NIM 009 - Damara Dihyan Darpita)
 */
object SearchUtil009 {

    /**
     * Pencarian barang berdasarkan nama barang.
     */
    fun searchItems009(items009: List<Item012>, keyword009: String): List<Item012> {
        val q009 = keyword009.trim()
        if (q009.isEmpty()) return items009
        return items009.filter { item009 ->
            item009.nama012.contains(q009, ignoreCase = true)
        }
    }

    /**
     * Sorting barang berdasarkan kriteria pilihan (Nama, Jumlah, Harga).
     */
    fun sortItems009(items009: List<Item012>, option009: SortOption009): List<Item012> {
        return when (option009) {
            SortOption009.NAMA_ASC -> items009.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.nama012 })
            SortOption009.NAMA_DESC -> items009.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.nama012 })
            SortOption009.JUMLAH_ASC -> items009.sortedBy { it.jumlah012 }
            SortOption009.JUMLAH_DESC -> items009.sortedByDescending { it.jumlah012 }
            SortOption009.HARGA_ASC -> items009.sortedBy { it.harga012 }
            SortOption009.HARGA_DESC -> items009.sortedByDescending { it.harga012 }
        }
    }

    /**
     * Kombinasi filter pencarian nama & pengurutan kriteria.
     */
    fun filterAndSortItems009(
        items009: List<Item012>,
        keyword009: String,
        option009: SortOption009
    ): List<Item012> {
        val filtered009 = searchItems009(items009, keyword009)
        return sortItems009(filtered009, option009)
    }
}
