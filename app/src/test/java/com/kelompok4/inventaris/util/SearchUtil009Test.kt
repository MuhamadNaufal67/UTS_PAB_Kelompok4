// Bagian NIM 102042300009 – Damara Dihyan Darpita (Search & Sorting Barang)
package com.kelompok4.inventaris.util

import com.kelompok4.inventaris.data.Item012
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit Testing untuk Pencarian & Sorting Barang (NIM 009 - Damara Dihyan Darpita)
 */
class SearchUtil009Test {

    private val sampleItems009 = listOf(
        Item012(id012 = 1, userId012 = 1, nama012 = "Monitor LED", jumlah012 = 5, harga012 = 1500000L),
        Item012(id012 = 2, userId012 = 1, nama012 = "Keyboard Mechanical", jumlah012 = 12, harga012 = 450000L),
        Item012(id012 = 3, userId012 = 1, nama012 = "Mouse Wireless", jumlah012 = 20, harga012 = 150000L),
        Item012(id012 = 4, userId012 = 1, nama012 = "Headset Gaming", jumlah012 = 2, harga012 = 800000L)
    )

    @Test
    fun testSearchItems009_MatchingKeyword() {
        val result009 = SearchUtil009.searchItems009(sampleItems009, "key")
        assertEquals(1, result009.size)
        assertEquals("Keyboard Mechanical", result009[0].nama012)
    }

    @Test
    fun testSearchItems009_EmptyKeyword_ReturnsAll() {
        val result009 = SearchUtil009.searchItems009(sampleItems009, "")
        assertEquals(4, result009.size)
    }

    @Test
    fun testSortItems009_NamaAsc() {
        val sorted009 = SearchUtil009.sortItems009(sampleItems009, SortOption009.NAMA_ASC)
        assertEquals("Headset Gaming", sorted009[0].nama012)
        assertEquals("Keyboard Mechanical", sorted009[1].nama012)
        assertEquals("Monitor LED", sorted009[2].nama012)
        assertEquals("Mouse Wireless", sorted009[3].nama012)
    }

    @Test
    fun testSortItems009_JumlahDesc() {
        val sorted009 = SearchUtil009.sortItems009(sampleItems009, SortOption009.JUMLAH_DESC)
        assertEquals(20, sorted009[0].jumlah012)
        assertEquals(12, sorted009[1].jumlah012)
        assertEquals(5, sorted009[2].jumlah012)
        assertEquals(2, sorted009[3].jumlah012)
    }

    @Test
    fun testSortItems009_HargaAsc() {
        val sorted009 = SearchUtil009.sortItems009(sampleItems009, SortOption009.HARGA_ASC)
        assertEquals(150000L, sorted009[0].harga012)
        assertEquals(450000L, sorted009[1].harga012)
        assertEquals(800000L, sorted009[2].harga012)
        assertEquals(1500000L, sorted009[3].harga012)
    }

    @Test
    fun testFilterAndSortItems009() {
        val result009 = SearchUtil009.filterAndSortItems009(sampleItems009, "m", SortOption009.HARGA_DESC)
        // Matching: "Monitor LED", "Mouse Wireless", "Keyboard Mechanical", "Headset Gaming" (all contain 'm')
        assertEquals(4, result009.size)
        assertEquals("Monitor LED", result009[0].nama012) // Highest price: 1,500,000
    }
}
