// Bagian NIM 102042300023 – Muhamad Naufal Nazih (UI Barang & Animasi)
package com.kelompok4.inventaris.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.kelompok4.inventaris.R

/*
 * Kumpulan helper animasi untuk seluruh aplikasi.
 * Semua memakai API bawaan Android (ViewPropertyAnimator, ObjectAnimator, ValueAnimator),
 * tanpa library tambahan.
 */

private fun View.dp023(nilai023: Float): Float = nilai023 * resources.displayMetrics.density

/**
 * Stagger: view muncul bergantian (fade + slide dari bawah).
 * Dipanggil di onCreate sebelum view tampil.
 */
fun staggerIn023(
    views023: List<View>,
    delayAwal023: Long = 80L,
    jeda023: Long = 70L,
    durasi023: Long = 420L
) {
    views023.forEachIndexed { index023, view023 ->
        view023.alpha = 0f
        view023.translationY = view023.dp023(24f)
        view023.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(delayAwal023 + index023 * jeda023)
            .setDuration(durasi023)
            .setInterpolator(DecelerateInterpolator(1.6f))
            .start()
    }
}

/** Pop-in: view membesar dari 0 dengan efek memantul (OvershootInterpolator). */
fun View.popIn023(delay023: Long = 0L, durasi023: Long = 520L, onSelesai023: (() -> Unit)? = null) {
    alpha = 0f
    scaleX = 0f
    scaleY = 0f
    animate()
        .alpha(1f)
        .scaleX(1f)
        .scaleY(1f)
        .setStartDelay(delay023)
        .setDuration(durasi023)
        .setInterpolator(OvershootInterpolator(2f))
        .withEndAction { onSelesai023?.invoke() }
        .start()
}

/**
 * Melayang naik-turun terus-menerus (ObjectAnimator infinite).
 * Animator otomatis di-cancel saat view lepas dari window (onViewDetachedFromWindow),
 * dan dijalankan lagi kalau view di-attach ulang. Tidak memakai doOnDetach karena
 * doOnDetach langsung dieksekusi bila view belum ter-attach.
 */
fun View.startFloat023(jarakDp023: Float = 8f, durasi023: Long = 1600L) {
    if (getTag(R.id.tag_float_animator_023) != null) return

    val animator023 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0f, -dp023(jarakDp023)).apply {
        duration = durasi023
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = AccelerateDecelerateInterpolator()
    }
    setTag(R.id.tag_float_animator_023, animator023)

    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v023: View) {
            if (!animator023.isStarted) animator023.start()
        }

        override fun onViewDetachedFromWindow(v023: View) {
            animator023.cancel()
            v023.translationY = 0f
        }
    })
    if (isAttachedToWindow) animator023.start()
}

/** Getar kiri-kanan (dipakai untuk field yang error). */
fun View.shake023() {
    val dx023 = dp023(10f)
    ObjectAnimator.ofFloat(
        this, View.TRANSLATION_X,
        0f, dx023, -dx023, dx023 * 0.7f, -dx023 * 0.7f, dx023 * 0.35f, -dx023 * 0.35f, 0f
    ).apply {
        duration = 450L
        start()
    }
}

/** Getarkan setiap TextInputLayout yang sedang menampilkan error. */
fun shakeIfError023(vararg til023: TextInputLayout) {
    til023.filter { it.error != null }.forEach { it.shake023() }
}

/** Pulse: membesar sedikit (1.04) lalu kembali ke ukuran semula. */
fun View.pulse023(skala023: Float = 1.04f) {
    ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, skala023, 1f),
        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, skala023, 1f)
    ).apply {
        duration = 380L
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }
}

/** Fade-in saat view berubah dari tersembunyi menjadi terlihat. */
fun View.fadeInVisible023(durasi023: Long = 350L) {
    if (visibility == View.VISIBLE) return
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setStartDelay(0L)
        .setDuration(durasi023)
        .setInterpolator(DecelerateInterpolator())
        .start()
}

/**
 * Count-up angka dari nilai yang sedang tampil ke [nilaiBaru023] (ValueAnimator).
 * Nilai yang tampil & animator disimpan di view tag (R.id.tag_count_value_023 / tag_count_animator_023).
 * @return true jika sebelumnya sudah ada nilai tampil DAN nilainya berubah
 *         (false saat pertama kali tampil) – dipakai untuk memicu efek pulse.
 */
fun TextView.animateCount023(
    nilaiBaru023: Long,
    durasi023: Long = 800L,
    format023: (Long) -> String = { it.toString() }
): Boolean {
    val nilaiLama023 = getTag(R.id.tag_count_value_023) as? Long
    (getTag(R.id.tag_count_animator_023) as? Animator)?.cancel()

    val awal023 = nilaiLama023 ?: 0L
    val berubah023 = nilaiLama023 != null && nilaiLama023 != nilaiBaru023

    if (awal023 == nilaiBaru023) {
        setTag(R.id.tag_count_value_023, nilaiBaru023)
        text = format023(nilaiBaru023)
        return berubah023
    }

    val animator023 = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = durasi023
        interpolator = DecelerateInterpolator(1.8f)
        addUpdateListener { anim023 ->
            val fraksi023 = anim023.animatedValue as Float
            val sekarang023 = awal023 + ((nilaiBaru023 - awal023) * fraksi023.toDouble()).toLong()
            setTag(R.id.tag_count_value_023, sekarang023)
            text = format023(sekarang023)
        }
    }
    setTag(R.id.tag_count_animator_023, animator023)
    setTag(R.id.tag_count_value_023, awal023)
    animator023.start()
    return berubah023
}

/**
 * Efek tekan: view mengecil ke 0.96 saat ditekan lalu kembali.
 * OnTouchListener mengembalikan false agar onClick tetap berjalan.
 * Tidak dipasang pada view yang tidak clickable.
 */
@SuppressLint("ClickableViewAccessibility")
fun View.addPressEffect023(skala023: Float = 0.96f) {
    if (!isClickable) return
    setOnTouchListener { view023, event023 ->
        when (event023.actionMasked) {
            MotionEvent.ACTION_DOWN -> view023.animatePressScale023(skala023)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> view023.animatePressScale023(1f)
        }
        false
    }
}

private fun View.animatePressScale023(skala023: Float) {
    animate()
        .scaleX(skala023)
        .scaleY(skala023)
        .setStartDelay(0L)
        .setDuration(120L)
        .setInterpolator(DecelerateInterpolator())
        .start()
}
