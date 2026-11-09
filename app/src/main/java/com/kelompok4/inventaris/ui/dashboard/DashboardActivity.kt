// Bagian NIM 102042330012 – Muhammad Syuja Al Abid (Room Database & Dashboard)
package com.kelompok4.inventaris.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kelompok4.inventaris.R
import com.kelompok4.inventaris.data.AppDatabase012
import com.kelompok4.inventaris.databinding.ActivityDashboardBinding
import com.kelompok4.inventaris.session.SessionManager020
import com.kelompok4.inventaris.ui.anggota.showAnggotaKelompok023
import com.kelompok4.inventaris.ui.auth.LoginActivity
import com.kelompok4.inventaris.ui.item.ItemFormActivity
import com.kelompok4.inventaris.ui.item.ItemListActivity
import com.kelompok4.inventaris.util.addPressEffect023
import com.kelompok4.inventaris.util.animateCount023
import com.kelompok4.inventaris.util.applySystemBarsPadding023
import com.kelompok4.inventaris.util.formatRupiah023
import com.kelompok4.inventaris.util.pulse023
import com.kelompok4.inventaris.util.staggerIn023
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding012: ActivityDashboardBinding
    private lateinit var session012: SessionManager020
    private val db012 by lazy { AppDatabase012.getInstance012(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session012 = SessionManager020(this)
        if (!session012.isLoggedIn020()) {
            backToLogin012()
            return
        }

        enableEdgeToEdge()
        binding012 = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding012.root)
        binding012.root.applySystemBarsPadding023()
        setSupportActionBar(binding012.toolbar012)

        binding012.tvGreeting012.text =
            getString(R.string.dashboard_greeting, session012.getUsername020())

        binding012.btnDaftarBarang012.setOnClickListener {
            startActivity(Intent(this, ItemListActivity::class.java))
        }
        binding012.btnTambahBarang012.setOnClickListener {
            startActivity(Intent(this, ItemFormActivity::class.java))
        }
        binding012.btnAnggota012.setOnClickListener { showAnggotaKelompok023(this) }
        binding012.btnLogout012.setOnClickListener { confirmLogout012() }

        playIntroAnimation012()
        observeSummary012()
    }

    /** Animasi pembuka: greeting, kartu, dan tombol muncul bergantian */
    private fun playIntroAnimation012() {
        staggerIn023(
            listOf(
                binding012.tvGreeting012,
                binding012.tvDashboardSub012,
                binding012.cardTotal012,
                binding012.cardJenis012,
                binding012.cardStok012,
                binding012.btnDaftarBarang012,
                binding012.btnTambahBarang012,
                binding012.btnAnggota012,
                binding012.btnLogout012
            )
        )
        listOf(
            binding012.btnDaftarBarang012,
            binding012.btnTambahBarang012,
            binding012.btnAnggota012,
            binding012.btnLogout012
        ).forEach { tombol012 -> tombol012.addPressEffect023() }
    }

    /** BONUS: total nilai inventaris diperbarui otomatis (real-time) dari Room Flow */
    private fun observeSummary012() {
        val userId012 = session012.getUserId020()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                db012.itemDao012().getSummary012(userId012).collect { summary012 ->
                    // Animasi count-up dari angka yang sedang tampil ke angka baru
                    val nilaiBerubah012 = binding012.tvTotalNilai012.animateCount023(
                        summary012.totalNilai012,
                        format023 = ::formatRupiah023
                    )
                    binding012.tvTotalJenis012.animateCount023(summary012.totalJenis012.toLong())
                    binding012.tvTotalStok012.animateCount023(summary012.totalStok012.toLong())
                    // Kartu total "pulse" hanya saat nilainya berubah (bukan saat pertama tampil)
                    if (nilaiBerubah012) binding012.cardTotal012.pulse023()
                }
            }
        }
    }

    private fun confirmLogout012() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.logout)
            .setMessage(R.string.logout_confirm)
            .setNegativeButton(R.string.batal, null)
            .setPositiveButton(R.string.logout) { _, _ ->
                session012.logout020()
                backToLogin012()
            }
            .show()
    }

    private fun backToLogin012() {
        val intent012 = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent012)
        finish()
    }
}
