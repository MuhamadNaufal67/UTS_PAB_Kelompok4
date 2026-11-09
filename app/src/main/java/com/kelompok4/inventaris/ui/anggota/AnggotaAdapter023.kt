// Bagian NIM 102042300023 – Muhamad Naufal Nazih (UI Barang & Animasi)
package com.kelompok4.inventaris.ui.anggota

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kelompok4.inventaris.databinding.ItemAnggota023Binding

/** Data satu anggota kelompok */
data class Anggota023(val nama023: String, val nim023: String)

/** Adapter RecyclerView untuk daftar anggota Kelompok 4 (ditampilkan di bottom sheet). */
class AnggotaAdapter023(
    private val daftarAnggota023: List<Anggota023>
) : RecyclerView.Adapter<AnggotaAdapter023.AnggotaViewHolder023>() {

    class AnggotaViewHolder023(val binding023: ItemAnggota023Binding) :
        RecyclerView.ViewHolder(binding023.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnggotaViewHolder023 {
        val binding023 = ItemAnggota023Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnggotaViewHolder023(binding023)
    }

    override fun onBindViewHolder(holder: AnggotaViewHolder023, position: Int) {
        val anggota023 = daftarAnggota023[position]
        with(holder.binding023) {
            tvInisialAnggota023.text = inisial023(anggota023.nama023)
            tvNamaAnggota023.text = anggota023.nama023
            tvNimAnggota023.text = anggota023.nim023
        }
    }

    override fun getItemCount(): Int = daftarAnggota023.size

    /** Inisial dari 2 kata pertama nama, contoh: "Salsa Salfania" -> "SS" */
    private fun inisial023(nama023: String): String =
        nama023.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifEmpty { "?" }
}
