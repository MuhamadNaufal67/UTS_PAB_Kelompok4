// Bagian NIM 102042300016 – Salsa Salfania (Integrasi UI & Inventory Interaction)
package com.kelompok4.inventaris.util

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.kelompok4.inventaris.R
import com.kelompok4.inventaris.ui.item.ItemFormActivity

/**
 * Helper Integrasi UI, Navigasi & Interaksi Inventaris (NIM 016 - Salsa Salfania)
 */
object InventoryInteraction016 {

    /**
     * Navigasi ke Form Tambah Barang.
     */
    fun openItemFormForCreate016(context016: Context) {
        val intent016 = Intent(context016, ItemFormActivity::class.java)
        context016.startActivity(intent016)
    }

    /**
     * Navigasi ke Form Edit Barang berdasarkan ID barang.
     */
    fun openItemFormForEdit016(context016: Context, itemId016: Int) {
        val intent016 = Intent(context016, ItemFormActivity::class.java).apply {
            putExtra(ItemFormActivity.EXTRA_ITEM_ID_023, itemId016)
        }
        context016.startActivity(intent016)
    }

    /**
     * Menampilkan dialog konfirmasi sebelum penghapusan barang dari inventaris.
     */
    fun showDeleteConfirmation016(
        context016: Context,
        namaBarang016: String,
        onConfirm016: () -> Unit
    ) {
        MaterialAlertDialogBuilder(context016)
            .setTitle(R.string.hapus_barang)
            .setMessage(context016.getString(R.string.hapus_confirm, namaBarang016))
            .setNegativeButton(R.string.batal, null)
            .setPositiveButton(R.string.hapus) { _, _ ->
                onConfirm016()
            }
            .show()
    }

    /**
     * Mengatur status pesan & visibilitas saat list inventaris kosong / tidak ditemukan.
     */
    fun handleEmptyState016(
        tvEmpty016: TextView,
        isEmpty016: Boolean,
        isSearching016: Boolean
    ) {
        if (isEmpty016) {
            tvEmpty016.fadeInVisible023()
            tvEmpty016.setText(
                if (isSearching016) R.string.list_tidak_ditemukan
                else R.string.list_kosong
            )
        } else {
            tvEmpty016.visibility = View.GONE
        }
    }

    /**
     * Penanganan pergerakan scroll FAB (shrink / extend) pada RecyclerView.
     */
    fun handleFabScroll016(fab016: ExtendedFloatingActionButton, dy016: Int) {
        if (dy016 > 0 && fab016.isExtended) {
            fab016.shrink()
        } else if (dy016 < 0 && !fab016.isExtended) {
            fab016.extend()
        }
    }

    /**
     * Menampilkan Toast notifikasi aksi inventaris dengan ID string resource.
     */
    fun showToast016(context016: Context, resId016: Int) {
        Toast.makeText(context016, resId016, Toast.LENGTH_SHORT).show()
    }

    /**
     * Menampilkan Toast notifikasi pesan kustom.
     */
    fun showToastMessage016(context016: Context, message016: String) {
        Toast.makeText(context016, message016, Toast.LENGTH_SHORT).show()
    }
}
