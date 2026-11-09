// Bagian NIM 102042300023 – Muhamad Naufal Nazih (UI Barang & Animasi)
package com.kelompok4.inventaris.ui.item

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.kelompok4.inventaris.R
import com.kelompok4.inventaris.data.AppDatabase012
import com.kelompok4.inventaris.data.Item012
import com.kelompok4.inventaris.databinding.ActivityItemFormBinding
import com.kelompok4.inventaris.session.SessionManager020
import com.kelompok4.inventaris.util.InventoryInteraction016
import com.kelompok4.inventaris.util.ValidationUtil005
import com.kelompok4.inventaris.util.addPressEffect023
import com.kelompok4.inventaris.util.animateCount023
import com.kelompok4.inventaris.util.applySystemBarsPadding023
import com.kelompok4.inventaris.util.formatRupiah023
import com.kelompok4.inventaris.util.shakeIfError023
import com.kelompok4.inventaris.util.staggerIn023
import kotlinx.coroutines.launch

/**
 * Form Tambah & Edit Barang.
 * - Tanpa extra  -> mode TAMBAH
 * - Dengan extra EXTRA_ITEM_ID_023 -> mode EDIT (+ tombol Hapus)
 */
class ItemFormActivity : AppCompatActivity() {

    private lateinit var binding023: ActivityItemFormBinding
    private lateinit var session023: SessionManager020
    private val db023 by lazy { AppDatabase012.getInstance012(this) }
    private var loginUserId023 = -1
    private var itemId023 = 0
    private var itemLama023: Item012? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session023 = SessionManager020(this)
        loginUserId023 = session023.getUserId020()
        if (loginUserId023 == -1) {
            finish()
            return
        }

        enableEdgeToEdge()
        binding023 = ActivityItemFormBinding.inflate(layoutInflater)
        setContentView(binding023.root)
        binding023.root.applySystemBarsPadding023()

        setSupportActionBar(binding023.toolbar023)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        itemId023 = intent.getIntExtra(EXTRA_ITEM_ID_023, 0)
        val modeEdit023 = itemId023 > 0

        supportActionBar?.title = getString(if (modeEdit023) R.string.edit_barang else R.string.tambah_barang)
        binding023.btnSimpan023.setText(if (modeEdit023) R.string.simpan_perubahan else R.string.simpan)
        binding023.btnHapus023.isVisible = modeEdit023

        // Preview total nilai barang ini (jumlah x harga) saat user mengetik
        binding023.etJumlah023.doAfterTextChanged { updatePreview023() }
        binding023.etHarga023.doAfterTextChanged { updatePreview023() }
        updatePreview023()

        if (modeEdit023) loadItem023()

        binding023.btnSimpan023.setOnClickListener { simpan023() }
        binding023.btnHapus023.setOnClickListener { confirmDelete023() }

        playIntroAnimation023()
    }

    /** Animasi pembuka: field, kartu preview, dan tombol muncul bergantian */
    private fun playIntroAnimation023() {
        staggerIn023(
            listOf(
                binding023.tilNama023,
                binding023.tilJumlah023,
                binding023.tilHarga023,
                binding023.cardPreview023,
                binding023.btnSimpan023,
                binding023.btnHapus023
            )
        )
        binding023.btnSimpan023.addPressEffect023()
        binding023.btnHapus023.addPressEffect023()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadItem023() {
        lifecycleScope.launch {
            val item023 = db023.itemDao012().getItemById012(itemId023, loginUserId023)
            if (item023 == null) {
                InventoryInteraction016.showToast016(this@ItemFormActivity, R.string.msg_data_tidak_ada)
                finish()
                return@launch
            }
            itemLama023 = item023
            binding023.etNama023.setText(item023.nama012)
            binding023.etJumlah023.setText(item023.jumlah012.toString())
            binding023.etHarga023.setText(item023.harga012.toString())
        }
    }

    private fun updatePreview023() {
        val jumlahPreview023 = binding023.etJumlah023.text.toString().toLongOrNull() ?: 0L
        val hargaPreview023 = binding023.etHarga023.text.toString().toLongOrNull() ?: 0L
        // Count-up singkat dari angka yang sedang tampil ke angka baru
        binding023.tvPreview023.animateCount023(
            jumlahPreview023 * hargaPreview023,
            durasi023 = 300L,
            format023 = ::formatRupiah023
        )
    }

    private fun simpan023() {
        val namaInput023 = binding023.etNama023.text.toString().trim()
        val jumlahText023 = binding023.etJumlah023.text.toString().trim()
        val hargaText023 = binding023.etHarga023.text.toString().trim()

        binding023.tilNama023.error = null
        binding023.tilJumlah023.error = null
        binding023.tilHarga023.error = null

        // NIM 005 – Helper Validasi Input Barang (Zabila Nanda Irawan)
        val validation005 = ValidationUtil005.validateItemInput005(namaInput023, jumlahText023, hargaText023)
        if (!validation005.isValid005) {
            binding023.tilNama023.error = validation005.errorNama005?.let { getString(it) }
            binding023.tilJumlah023.error = validation005.errorJumlah005?.let { getString(it) }
            binding023.tilHarga023.error = validation005.errorHarga005?.let { getString(it) }
            shakeIfError023(binding023.tilNama023, binding023.tilJumlah023, binding023.tilHarga023)
            return
        }

        val jumlahInput023 = jumlahText023.toInt()
        val hargaInput023 = hargaText023.toLong()

        // Mode edit tapi data lama belum selesai dimuat -> jangan simpan dulu
        if (itemId023 > 0 && itemLama023 == null) return

        binding023.btnSimpan023.isEnabled = false
        lifecycleScope.launch {
            val itemDao023 = db023.itemDao012()
            val lama023 = itemLama023
            if (lama023 == null) {
                // CREATE – user_id diambil dari sesi login
                itemDao023.insertItem012(
                    Item012(
                        userId012 = loginUserId023,
                        nama012 = namaInput023,
                        jumlah012 = jumlahInput023,
                        harga012 = hargaInput023
                    )
                )
                // NIM 016 – Toast Notifikasi (Salsa Salfania)
                InventoryInteraction016.showToast016(this@ItemFormActivity, R.string.msg_tambah_berhasil)
            } else {
                // UPDATE – id & user_id tetap milik data lama
                itemDao023.updateItem012(
                    lama023.copy(
                        nama012 = namaInput023,
                        jumlah012 = jumlahInput023,
                        harga012 = hargaInput023
                    )
                )
                // NIM 016 – Toast Notifikasi (Salsa Salfania)
                InventoryInteraction016.showToast016(this@ItemFormActivity, R.string.msg_update_berhasil)
            }
            finish()
        }
    }

    private fun confirmDelete023() {
        val namaHapus023 = itemLama023?.nama012 ?: return
        // NIM 016 – Dialog Konfirmasi Hapus Barang (Salsa Salfania)
        InventoryInteraction016.showDeleteConfirmation016(this, namaHapus023) {
            lifecycleScope.launch {
                db023.itemDao012().deleteItemById012(itemId023, loginUserId023)
                InventoryInteraction016.showToast016(this@ItemFormActivity, R.string.msg_hapus_berhasil)
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_ITEM_ID_023 = "extra_item_id_023"
    }
}
