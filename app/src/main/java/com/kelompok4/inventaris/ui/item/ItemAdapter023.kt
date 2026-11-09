// Bagian NIM 102042300023 – Muhamad Naufal Nazih (UI Barang & Animasi)
package com.kelompok4.inventaris.ui.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok4.inventaris.R
import com.kelompok4.inventaris.data.Item012
import com.kelompok4.inventaris.databinding.ItemBarangBinding
import com.kelompok4.inventaris.util.addPressEffect023
import com.kelompok4.inventaris.util.formatRupiah023

/**
 * Adapter RecyclerView untuk daftar barang.
 * Klik kartu -> Edit, klik ikon tempat sampah -> Hapus.
 */
class ItemAdapter023(
    private val onItemClick023: (Item012) -> Unit,
    private val onDeleteClick023: (Item012) -> Unit
) : ListAdapter<Item012, ItemAdapter023.ItemViewHolder023>(DIFF_023) {

    class ItemViewHolder023(val binding023: ItemBarangBinding) :
        RecyclerView.ViewHolder(binding023.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder023 {
        val binding023 = ItemBarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Efek tekan: kartu mengecil ke 0.96 lalu kembali (onClick tetap jalan)
        binding023.root.addPressEffect023()
        return ItemViewHolder023(binding023)
    }

    override fun onBindViewHolder(holder: ItemViewHolder023, position: Int) {
        val item023 = getItem(position)
        val context023 = holder.itemView.context
        val subtotal023 = item023.jumlah012.toLong() * item023.harga012

        with(holder.binding023) {
            tvInisial023.text = item023.nama012.firstOrNull()?.uppercase() ?: "?"
            tvNama023.text = item023.nama012
            tvDetail023.text = context023.getString(
                R.string.item_detail,
                item023.jumlah012,
                formatRupiah023(item023.harga012)
            )
            tvSubtotal023.text = formatRupiah023(subtotal023)

            root.setOnClickListener { onItemClick023(item023) }
            btnDelete023.setOnClickListener { onDeleteClick023(item023) }
        }
    }

    companion object {
        private val DIFF_023 = object : DiffUtil.ItemCallback<Item012>() {
            override fun areItemsTheSame(oldItem: Item012, newItem: Item012): Boolean =
                oldItem.id012 == newItem.id012

            override fun areContentsTheSame(oldItem: Item012, newItem: Item012): Boolean =
                oldItem == newItem
        }
    }
}
