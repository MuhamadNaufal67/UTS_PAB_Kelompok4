// Bagian NIM 102042300023 – Muhamad Naufal Nazih (UI Barang & Animasi)
package com.kelompok4.inventaris.util

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/** Format angka menjadi Rupiah, contoh: 1500000 -> "Rp 1.500.000" */
fun formatRupiah023(nilai023: Long): String {
    val symbols023 = DecimalFormatSymbols(Locale.forLanguageTag("id-ID")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    val format023 = DecimalFormat("#,##0", symbols023)
    return "Rp " + format023.format(nilai023)
}

/** Memberi padding sesuai status bar, navigation bar & keyboard (edge-to-edge) */
fun View.applySystemBarsPadding023() {
    val left023 = paddingLeft
    val top023 = paddingTop
    val right023 = paddingRight
    val bottom023 = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view023, insets023 ->
        val bars023 = insets023.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
        )
        view023.setPadding(
            left023 + bars023.left,
            top023 + bars023.top,
            right023 + bars023.right,
            bottom023 + bars023.bottom
        )
        insets023
    }
}
