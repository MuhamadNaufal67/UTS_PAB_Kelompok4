// Bagian NIM 102042300020 – Ibnu Augustiana (Autentikasi & Session)
package com.kelompok4.inventaris.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kelompok4.inventaris.R
import com.kelompok4.inventaris.data.AppDatabase012
import com.kelompok4.inventaris.databinding.ActivityLoginBinding
import com.kelompok4.inventaris.session.SessionManager020
import com.kelompok4.inventaris.ui.anggota.showAnggotaKelompok023
import com.kelompok4.inventaris.ui.dashboard.DashboardActivity
import com.kelompok4.inventaris.util.addPressEffect023
import com.kelompok4.inventaris.util.applySystemBarsPadding023
import com.kelompok4.inventaris.util.hashPassword020
import com.kelompok4.inventaris.util.popIn023
import com.kelompok4.inventaris.util.shake023
import com.kelompok4.inventaris.util.shakeIfError023
import com.kelompok4.inventaris.util.staggerIn023
import com.kelompok4.inventaris.util.startFloat023
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding020: ActivityLoginBinding
    private lateinit var session020: SessionManager020
    private val db020 by lazy { AppDatabase012.getInstance012(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session020 = SessionManager020(this)

        // Jika sesi masih ada, langsung ke Dashboard (tidak perlu login ulang)
        if (session020.isLoggedIn020()) {
            openDashboard020()
            return
        }

        enableEdgeToEdge()
        binding020 = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding020.root)
        binding020.root.applySystemBarsPadding023()

        binding020.btnLogin020.setOnClickListener { login020() }
        binding020.tvToRegister020.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding020.tvAnggota020.setOnClickListener { showAnggotaKelompok023(this) }

        playIntroAnimation020()
    }

    /** Animasi pembuka: logo pop-in lalu melayang, elemen lain muncul bergantian */
    private fun playIntroAnimation020() {
        val logo020 = binding020.ivLogo020
        logo020.popIn023 { logo020.startFloat023() }

        staggerIn023(
            listOf(
                binding020.tvLoginTitle020,
                binding020.tvLoginSubtitle020,
                binding020.tilUsername020,
                binding020.tilPassword020,
                binding020.btnLogin020,
                binding020.tvToRegister020,
                binding020.tvFooter020,
                binding020.tvAnggota020
            ),
            delayAwal023 = 250L
        )
        binding020.btnLogin020.addPressEffect023()
    }

    private fun login020() {
        val username020 = binding020.etUsername020.text.toString().trim()
        val password020 = binding020.etPassword020.text.toString()

        binding020.tilUsername020.error = null
        binding020.tilPassword020.error = null

        // Validasi input
        var valid020 = true
        if (username020.isEmpty()) {
            binding020.tilUsername020.error = getString(R.string.err_username_kosong)
            valid020 = false
        }
        if (password020.isEmpty()) {
            binding020.tilPassword020.error = getString(R.string.err_password_kosong)
            valid020 = false
        } else if (password020.length < 6) {
            binding020.tilPassword020.error = getString(R.string.err_password_min)
            valid020 = false
        }
        if (!valid020) {
            shakeIfError023(binding020.tilUsername020, binding020.tilPassword020)
            return
        }

        binding020.btnLogin020.isEnabled = false
        lifecycleScope.launch {
            // Query ke Room
            val user020 = db020.userDao020().getUserByUsername020(username020)
            binding020.btnLogin020.isEnabled = true

            if (user020 == null || user020.password020 != hashPassword020(password020)) {
                Toast.makeText(this@LoginActivity, R.string.msg_login_gagal, Toast.LENGTH_SHORT).show()
                binding020.tilUsername020.shake023()
                binding020.tilPassword020.shake023()
            } else {
                // Simpan user_id ke SharedPreferences
                session020.createSession020(user020.id020, user020.username020)
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.msg_login_berhasil, user020.username020),
                    Toast.LENGTH_SHORT
                ).show()
                openDashboard020()
            }
        }
    }

    private fun openDashboard020() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
