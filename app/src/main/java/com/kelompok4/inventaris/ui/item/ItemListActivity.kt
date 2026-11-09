// Bagian NIM 102042300023 – Muhamad Naufal Nazih (UI Barang & Animasi)
package com.kelompok4.inventaris.ui.item

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kelompok4.inventaris.R
import com.kelompok4.inventaris.data.AppDatabase012
import com.kelompok4.inventaris.data.Item012
import com.kelompok4.inventaris.databinding.ActivityItemListBinding
import com.kelompok4.inventaris.session.SessionManager020
import com.kelompok4.inventaris.util.addPressEffect023
import com.kelompok4.inventaris.util.applySystemBarsPadding023
import com.kelompok4.inventaris.util.fadeInVisible023
import com.kelompok4.inventaris.util.formatRupiah023
import com.kelompok4.inventaris.util.popIn023
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ItemListActivity : AppCompatActivity() {

    private lateinit var binding023: ActivityItemListBinding
    private lateinit var session023: SessionManager020
    private lateinit var adapter023: ItemAdapter023
    private val db023 by lazy { AppDatabase012.getInstance012(this) }
    private val keyword023 = MutableStateFlow("")
    private var loginUserId023 = -1

    // Kata pencarian terakhir yang sudah dianimasikan (null = list belum pernah tampil)
    private var keywordTeranimasi023: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session023 = SessionManager020(this)
        loginUserId023 = session023.getUserId020()
        if (loginUserId023 == -1) {
            finish()
            return
        }

        enableEdgeToEdge()
        binding023 = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding023.root)
        binding023.root.applySystemBarsPadding023()

        setSupportActionBar(binding023.toolbar023)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView023()

        // Bonus: Search bar (cari berdasarkan nama barang)
        binding023.etSearch023.doAfterTextChanged { text023 ->
            keyword023.value = text023?.toString()?.trim().orEmpty()
        }

        binding023.fabTambah023.setOnClickListener {
            startActivity(Intent(this, ItemFormActivity::class.java))
        }
        setupFabAnimation023()

        observeData023()
    }

    /** FAB pop-in, mengecil saat scroll ke bawah & melebar lagi saat scroll ke atas */
    private fun setupFabAnimation023() {
        val fab023 = binding023.fabTambah023
        fab023.popIn023(delay023 = 300L)
        fab023.addPressEffect023()
        binding023.rvBarang023.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && fab023.isExtended) fab023.shrink()
                else if (dy < 0 && !fab023.isExtended) fab023.extend()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupRecyclerView023() {
        adapter023 = ItemAdapter023(
            onItemClick023 = { item023 ->
                // Buka form dalam mode EDIT
                val intent023 = Intent(this, ItemFormActivity::class.java)
                intent023.putExtra(ItemFormActivity.EXTRA_ITEM_ID_023, item023.id012)
                startActivity(intent023)
            },
            onDeleteClick023 = { item023 -> confirmDelete023(item023) }
        )
        binding023.rvBarang023.layoutManager = LinearLayoutManager(this)
        binding023.rvBarang023.adapter = adapter023
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeData023() {
        val itemDao023 = db023.itemDao012()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // List barang milik user yang login (otomatis ter-update saat data berubah)
                launch {
                    keyword023
                        .flatMapLatest { kata023 -> itemDao023.getItemsByUser012(loginUserId023, kata023) }
                        .collect { list023 ->
                            val kataSekarang023 = keyword023.value
                            adapter023.submitList(list023) {
                                // Layout animation hanya saat pertama tampil / kata pencarian berubah
                                if (kataSekarang023 != keywordTeranimasi023) {
                                    keywordTeranimasi023 = kataSekarang023
                                    binding023.rvBarang023.scheduleLayoutAnimation()
                                }
                            }
                            val kosong023 = list023.isEmpty()
                            if (kosong023) binding023.tvEmpty023.fadeInVisible023()
                            else binding023.tvEmpty023.isVisible = false
                            binding023.tvEmpty023.setText(
                                if (keyword023.value.isEmpty()) R.string.list_kosong
                                else R.string.list_tidak_ditemukan
                            )
                        }
                }
                // Ringkasan total nilai di atas list
                launch {
                    itemDao023.getSummary012(loginUserId023).collect { summary023 ->
                        binding023.tvRingkasan023.text = getString(
                            R.string.list_ringkasan,
                            summary023.totalJenis012,
                            formatRupiah023(summary023.totalNilai012)
                        )
                    }
                }
            }
        }
    }

    private fun confirmDelete023(item023: Item012) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.hapus_barang)
            .setMessage(getString(R.string.hapus_confirm, item023.nama012))
            .setNegativeButton(R.string.batal, null)
            .setPositiveButton(R.string.hapus) { _, _ ->
                lifecycleScope.launch {
                    db023.itemDao012().deleteItemById012(item023.id012, loginUserId023)
                    Toast.makeText(this@ItemListActivity, R.string.msg_hapus_berhasil, Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }
}
