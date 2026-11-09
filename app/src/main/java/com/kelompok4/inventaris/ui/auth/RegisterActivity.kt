// Bagian NIM 102042300020 – Ibnu Augustiana (Autentikasi & Session)
package com.kelompok4.inventaris.ui.auth

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kelompok4.inventaris.R
import com.kelompok4.inventaris.data.AppDatabase012
import com.kelompok4.inventaris.data.User020
import com.kelompok4.inventaris.databinding.ActivityRegisterBinding
import com.kelompok4.inventaris.util.addPressEffect023
import com.kelompok4.inventaris.util.applySystemBarsPadding023
import com.kelompok4.inventaris.util.hashPassword020
import com.kelompok4.inventaris.util.shake023
import com.kelompok4.inventaris.util.shakeIfError023
import com.kelompok4.inventaris.util.staggerIn023
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding020: ActivityRegisterBinding
    private val db020 by lazy { AppDatabase012.getInstance012(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding020 = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding020.root)
        binding020.root.applySystemBarsPadding023()

        setSupportActionBar(binding020.toolbar020)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding020.btnRegister020.setOnClickListener { register020() }
        binding020.tvToLogin020.setOnClickListener { finish() }

        playIntroAnimation020()
    }

    /** Animasi pembuka: judul & field muncul bergantian */
    private fun playIntroAnimation020() {
        staggerIn023(
            listOf(
                binding020.tvRegisterTitle020,
                binding020.tvRegisterSubtitle020,
                binding020.tilUsername020,
                binding020.tilPassword020,
                binding020.tilKonfirmasi020,
                binding020.btnRegister020,
                binding020.tvToLogin020
            )
        )
        binding020.btnRegister020.addPressEffect023()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun register020() {
        val username020 = binding020.etUsername020.text.toString().trim()
        val password020 = binding020.etPassword020.text.toString()
        val konfirmasi020 = binding020.etKonfirmasi020.text.toString()

        binding020.tilUsername020.error = null
        binding020.tilPassword020.error = null
        binding020.tilKonfirmasi020.error = null

        // Validasi: tidak boleh kosong, password min. 6 karakter, konfirmasi harus sama
        var valid020 = true
        if (username020.isEmpty()) {
            binding020.tilUsername020.error = getString(R.string.err_username_kosong)
            valid020 = false
        } else if (username020.contains(" ")) {
            binding020.tilUsername020.error = getString(R.string.err_username_spasi)
            valid020 = false
        }
        if (password020.isEmpty()) {
            binding020.tilPassword020.error = getString(R.string.err_password_kosong)
            valid020 = false
        } else if (password020.length < 6) {
            binding020.tilPassword020.error = getString(R.string.err_password_min)
            valid020 = false
        }
        if (konfirmasi020.isEmpty()) {
            binding020.tilKonfirmasi020.error = getString(R.string.err_konfirmasi_kosong)
            valid020 = false
        } else if (konfirmasi020 != password020) {
            binding020.tilKonfirmasi020.error = getString(R.string.err_konfirmasi_beda)
            valid020 = false
        }
        if (!valid020) {
            shakeIfError023(binding020.tilUsername020, binding020.tilPassword020, binding020.tilKonfirmasi020)
            return
        }

        binding020.btnRegister020.isEnabled = false
        lifecycleScope.launch {
            val userDao020 = db020.userDao020()

            // Cek username unik
            if (userDao020.getUserByUsername020(username020) != null) {
                binding020.tilUsername020.error = getString(R.string.err_username_dipakai)
                binding020.tilUsername020.shake023()
                binding020.btnRegister020.isEnabled = true
                return@launch
            }

            try {
                userDao020.insertUser020(
                    User020(
                        username020 = username020,
                        password020 = hashPassword020(password020)
                    )
                )
                Toast.makeText(this@RegisterActivity, R.string.msg_register_berhasil, Toast.LENGTH_LONG).show()
                finish() // kembali ke halaman Login
            } catch (e020: SQLiteConstraintException) {
                binding020.tilUsername020.error = getString(R.string.err_username_dipakai)
                binding020.tilUsername020.shake023()
                binding020.btnRegister020.isEnabled = true
            }
        }
    }
}
