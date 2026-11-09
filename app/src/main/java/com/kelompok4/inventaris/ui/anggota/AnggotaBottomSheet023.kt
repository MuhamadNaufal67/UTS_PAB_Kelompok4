// Bagian NIM 102042300023 – Muhamad Naufal Nazih (UI Barang & Animasi)
package com.kelompok4.inventaris.ui.anggota

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kelompok4.inventaris.R
import com.kelompok4.inventaris.databinding.BottomSheetAnggota023Binding

/**
 * Menampilkan BottomSheetDialog berisi daftar anggota Kelompok 4.
 * Data diambil dari string-array "anggota_kelompok_023" (format "Nama|NIM").
 * Baris muncul berurutan dengan layout animation slide-up.
 */
fun showAnggotaKelompok023(context023: Context) {
    val daftarAnggota023 = context023.resources
        .getStringArray(R.array.anggota_kelompok_023)
        .map { baris023 ->
            val bagian023 = baris023.split("|")
            Anggota023(
                nama023 = bagian023[0].trim(),
                nim023 = bagian023.getOrElse(1) { "" }.trim()
            )
        }

    val dialog023 = BottomSheetDialog(context023)
    val binding023 = BottomSheetAnggota023Binding.inflate(LayoutInflater.from(context023))

    binding023.rvAnggota023.layoutManager = LinearLayoutManager(context023)
    binding023.rvAnggota023.adapter = AnggotaAdapter023(daftarAnggota023)

    dialog023.setContentView(binding023.root)
    dialog023.setOnShowListener {
        // Putar animasi slide-up berurutan saat sheet tampil
        binding023.rvAnggota023.scheduleLayoutAnimation()
    }
    dialog023.show()
}
