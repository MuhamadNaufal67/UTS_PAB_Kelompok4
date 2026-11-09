// Bagian NIM 102042300020 – Ibnu Augustiana (Autentikasi & Session)
package com.kelompok4.inventaris.session

import android.content.Context
import android.content.SharedPreferences

/**
 * Menyimpan sesi login (user_id & username) di SharedPreferences.
 */
class SessionManager020(context020: Context) {

    private val prefs020: SharedPreferences =
        context020.getSharedPreferences(PREF_NAME_020, Context.MODE_PRIVATE)

    fun createSession020(userId020: Int, username020: String) {
        prefs020.edit()
            .putBoolean(KEY_IS_LOGIN_020, true)
            .putInt(KEY_USER_ID_020, userId020)
            .putString(KEY_USERNAME_020, username020)
            .apply()
    }

    fun isLoggedIn020(): Boolean =
        prefs020.getBoolean(KEY_IS_LOGIN_020, false) && getUserId020() != -1

    fun getUserId020(): Int = prefs020.getInt(KEY_USER_ID_020, -1)

    fun getUsername020(): String = prefs020.getString(KEY_USERNAME_020, "") ?: ""

    fun logout020() {
        prefs020.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME_020 = "session_inventaris_020"
        private const val KEY_IS_LOGIN_020 = "is_login_020"
        private const val KEY_USER_ID_020 = "user_id_020"
        private const val KEY_USERNAME_020 = "username_020"
    }
}
