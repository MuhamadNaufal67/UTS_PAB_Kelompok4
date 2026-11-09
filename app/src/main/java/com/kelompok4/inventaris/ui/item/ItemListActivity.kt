// Bagian NIM 102042300023 – Muhamad Naufal Nazih (UI Barang & Animasi)
package com.kelompok4.inventaris.ui.item

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kelompok4.inventaris.R
import com.kelompok4.inventaris.data.AppDatabase012
import com.kelompok4.inventaris.data.Item012
import com.kelompok4.inventaris.databinding.ActivityItemListBinding
import com.kelompok4.inventaris.session.SessionManager020
import com.kelompok4.inventaris.util.InventoryInteraction016
import com.kelompok4.inventaris.util.SearchUtil009
import com.kelompok4.inventaris.util.SortOption009
import com.kelompok4.inventaris.util.addPressEffect023
import com.kelompok4.inventaris.util.applySystemBarsPadding023
import com.kelompok4.inventaris.util.formatRupiah023
import com.kelompok4.inventaris.util.popIn023
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ItemListActivity : AppCompatActivity() {

    private lateinit var binding023: ActivityItemListBinding
    private lateinit var session023: SessionManager020
    private lateinit var adapter023: ItemAdapter023
    private val db023 by lazy { AppDatabase012.getInstance012(this) }
    private val keyword023 = MutableStateFlow("")
    private val sortOption009 = MutableStateFlow(SortOption009.NAMA_ASC)
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
        setupSortChips009()

        // NIM 009 – Search Bar pencarian barang (Damara Dihyan Darpita)
        binding023.etSearch023.doAfterTextChanged { text023 ->
            keyword023.value = text023?.toString()?.trim().orEmpty()
        }

        // NIM 016 – Navigasi Tambah Barang (Salsa Salfania)
        binding023.fabTambah023.setOnClickListener {
            InventoryInteraction016.openItemFormForCreate016(this)
        }
        setupFabAnimation023()

        observeData023()
    }

    /** NIM 009 – Event listener chips sorting (Damara Dihyan Darpita) */
    private fun setupSortChips009() {
        binding023.chipGroupSort009.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            val option009 = when (checkedIds.first()) {
                R.id.chipNamaAsc009 -> SortOption009.NAMA_ASC
                R.id.chipNamaDesc009 -> SortOption009.NAMA_DESC
                R.id.chipJumlahDesc009 -> SortOption009.JUMLAH_DESC
                R.id.chipHargaDesc009 -> SortOption009.HARGA_DESC
                R.id.chipHargaAsc009 -> SortOption009.HARGA_ASC
                else -> SortOption009.NAMA_ASC
            }
            sortOption009.value = option009
        }
    }

    /** FAB pop-in, mengecil saat scroll ke bawah & melebar lagi saat scroll ke atas */
    private fun setupFabAnimation023() {
        val fab023 = binding023.fabTambah023
        fab023.popIn023(delay023 = 300L)
        fab023.addPressEffect023()
        binding023.rvBarang023.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // NIM 016 – Handling Pergerakan FAB Scroll (Salsa Salfania)
                InventoryInteraction016.handleFabScroll016(fab023, dy)
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
                // NIM 016 – Navigasi Edit Barang (Salsa Salfania)
                InventoryInteraction016.openItemFormForEdit016(this, item023.id012)
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
                // List barang milik user yang login dengan pencarian (NIM 009) & sorting (NIM 009)
                launch {
                    combine(keyword023, sortOption009) { kata009, sort009 ->
                        kata009 to sort009
                    }.flatMapLatest { (kata009, sort009) ->
                        itemDao023.getItemsByUser012(loginUserId023, kata009).map { list012 ->
                            // NIM 009 – Sorting List Barang (Damara Dihyan Darpita)
                            SearchUtil009.sortItems009(list012, sort009)
                        }
                    }.collect { sortedList009 ->
                        val kataSekarang023 = keyword023.value
                        adapter023.submitList(sortedList009) {
                            // Layout animation hanya saat pertama tampil / kata pencarian berubah
                            if (kataSekarang023 != keywordTeranimasi023) {
                                keywordTeranimasi023 = kataSekarang023
                                binding023.rvBarang023.scheduleLayoutAnimation()
                            }
                        }
                        // NIM 016 – Penanganan Kondisi List Kosong / Empty State (Salsa Salfania)
                        InventoryInteraction016.handleEmptyState016(
                            tvEmpty016 = binding023.tvEmpty023,
                            isEmpty016 = sortedList009.isEmpty(),
                            isSearching016 = keyword023.value.isNotEmpty()
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
        // NIM 016 – Konfirmasi Penghapusan Barang (Salsa Salfania)
        InventoryInteraction016.showDeleteConfirmation016(this, item023.nama012) {
            lifecycleScope.launch {
                db023.itemDao012().deleteItemById012(item023.id012, loginUserId023)
                InventoryInteraction016.showToast016(this@ItemListActivity, R.string.msg_hapus_berhasil)
            }
        }
    }
}
