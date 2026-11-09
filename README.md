# UTS Pengembangan Aplikasi Bergerak — Kelompok 4
## Aplikasi Inventaris Barang (Kotlin + Room)

### Anggota Kelompok 4
| No | Nama | NIM |
|---|---|---|
| 1 | Muhammad Syuja Al Abid | 102042330012 |
| 2 | Muhamad Naufal Nazih | 102042300023 |
| 3 | Ibnu Augustiana | 102042300020 |
| 4 | Zabila Nanda Irawan | 102042300005 |
| 5 | Damara Dihyan Darpita | 102042330009 |
| 6 | Salsa Salfania | 102042300016 |

Daftar anggota juga bisa dilihat di aplikasi: tautan **"Lihat anggota kelompok"** di halaman Login atau tombol **Anggota Kelompok** di Dashboard (BottomSheet, `ui/anggota/AnggotaBottomSheet023.kt`).

### Pembagian NIM (3 digit) sebagai pembeda variabel & tabel
| NIM | Penanggung Jawab | Kode | Bagian yang memakai kode ini |
|---|---|---|---|
| 102042300020 | Ibnu Augustiana | **020** | Autentikasi & Session: `User020`, `UserDao020`, tabel `tb_user_020`, `SessionManager020`, `PasswordUtil020`, `LoginActivity`, `RegisterActivity` (semua variabelnya `...020`) |
| 102042330012 | Muhammad Syuja Al Abid | **012** | Room Database & Dashboard: `Item012`, `ItemDao012`, tabel `tb_item_012`, `InventorySummary012`, `AppDatabase012` (`db_inventaris_012`), `DashboardActivity` (semua variabelnya `...012`) |
| 102042300023 | Muhamad Naufal Nazih | **023** | UI Barang & Animasi: `ItemListActivity`, `ItemAdapter023`, `ItemFormActivity`, `UiUtil023`, `AnimUtil023`, `AnggotaBottomSheet023`, `AnggotaAdapter023` (semua variabelnya `...023`) |

Setiap file `.kt` diberi komentar di baris pertama: `// Bagian NIM ... – Nama (Bagian)`.

### 1. Cara membuka di Android Studio
1. Extract file ZIP.
2. Android Studio → **File › Open** → pilih folder `UTS_PAB_Kelompok4` → **OK**.
3. Tunggu **Gradle Sync** selesai (butuh internet saat pertama kali).
4. Pilih emulator / HP → klik **Run ▶**.

> Kalau Android Studio menawarkan *upgrade AGP*, boleh di-*skip*.
> Kalau muncul error versi Java/JDK: **Settings › Build, Execution, Deployment › Build Tools › Gradle › Gradle JDK** → pilih JDK 17 atau 21.

### 2. Alur navigasi
`Login → Dashboard → Daftar Barang (RecyclerView) → Form Tambah → Edit / Hapus`

### 3. Kesesuaian dengan rubrik
| Rubrik | Implementasi |
|---|---|
| Autentikasi & Session (30%) | `RegisterActivity` & `LoginActivity` query ke Room (`UserDao020`). `user_id` disimpan di SharedPreferences (`SessionManager020`). Sesi tetap tersimpan sampai **Logout**. |
| Room Database CRUD (30%) | Entity `User020`, `Item012`; DAO `UserDao020`, `ItemDao012`; `AppDatabase012`. Create/Read/Update/Delete barang. |
| UI & RecyclerView (15%) | `ItemListActivity` + `ItemAdapter023` (ListAdapter + DiffUtil), form di `ItemFormActivity`. |
| Data Integrity & Validation (15%) | `user_id` = **FOREIGN KEY** ke `tb_user_020`; semua query barang memakai `WHERE user_id = ...`. Validasi input kosong, password min. 6 karakter, konfirmasi password, **username unik** (unique index + cek query). |
| Bonus (maks 10%) | ✅ **Total nilai inventaris real-time** di Dashboard (Room `Flow`, `SUM(jumlah*harga)`) · ✅ **Search bar** nama barang · ✅ **ViewBinding** |

### 4. Database (Room / SQLite) — `db_inventaris_012`
| Tabel | Kolom |
|---|---|
| `tb_user_020` | `id` (PK), `username` (UNIQUE), `password` (hash SHA-256) |
| `tb_item_012` | `id` (PK), `user_id` (FK → tb_user_020.id), `nama`, `jumlah`, `harga` |

Cek isi tabel saat app berjalan: **View › Tool Windows › App Inspection › Database Inspector**.

### 5. Struktur file
```
app/src/main/java/com/kelompok4/inventaris/
├── data/
│   ├── User020.kt, UserDao020.kt                (NIM 020)
│   ├── Item012.kt, ItemDao012.kt,
│   │   InventorySummary012.kt, AppDatabase012.kt (NIM 012)
├── session/SessionManager020.kt                  (NIM 020)
├── util/PasswordUtil020.kt                       (NIM 020)
├── util/UiUtil023.kt, AnimUtil023.kt             (NIM 023)
└── ui/
    ├── auth/LoginActivity.kt, RegisterActivity.kt          (NIM 020)
    ├── dashboard/DashboardActivity.kt                      (NIM 012)
    ├── anggota/AnggotaBottomSheet023.kt,
    │           AnggotaAdapter023.kt                        (NIM 023)
    └── item/ItemListActivity.kt, ItemAdapter023.kt,
             ItemFormActivity.kt                            (NIM 023)
```

### 6. Skenario demo
1. Register akun **A** → coba password < 6 karakter & username yang sama (muncul error).
2. Login A → tambah 2–3 barang → total nilai di Dashboard berubah otomatis.
3. Edit salah satu barang, lalu hapus.
4. Coba fitur cari nama barang.
5. Logout → register & login akun **B** → daftar barang kosong (data A tidak terlihat = data terisolasi).

### 7. Animasi (NIM 023)
Semua animasi memakai API bawaan Android (`ViewPropertyAnimator`, `ObjectAnimator`, `ValueAnimator`, `LayoutAnimationController`) — **tanpa library tambahan**. Helper ada di `util/AnimUtil023.kt`, resource animasi di `res/anim/*_023.xml`, id tag view di `res/values/ids.xml`.

| Halaman | Animasi |
|---|---|
| **Semua Activity** | Transisi slide kanan-kiri (±320ms) lewat `android:windowAnimationStyle` → style `ActivitySlideAnim023` di `themes.xml` (`slide_in_right_023`, `slide_out_left_023`, `slide_in_left_023`, `slide_out_right_023`). |
| **Login** | Logo *pop-in* (scale + `OvershootInterpolator`) lalu melayang naik-turun terus (`ObjectAnimator` infinite, otomatis di-*cancel* saat view lepas dari window). Judul, subjudul, field, tombol, teks daftar & footer muncul bergantian (*stagger* fade + slide up). Field yang error bergetar (*shake*); saat login gagal field username & password bergetar. |
| **Register** | Judul, field, tombol muncul bergantian (*stagger*). Field yang error (termasuk username sudah dipakai) bergetar. |
| **Dashboard** | Greeting, kartu total, kartu statistik & tombol muncul bergantian. Angka **Total Nilai** (Rupiah), **Jenis Barang**, **Total Stok** memakai *count-up* (`ValueAnimator`) dari angka yang sedang tampil ke angka baru. Kartu total *pulse* (scale 1.04 → 1) saat nilainya berubah (tidak saat pertama tampil). |
| **Daftar Barang** | Item RecyclerView muncul dengan *layout animation* slide-up (`layout_item_slide_up_023`), diputar ulang saat pertama tampil & saat kata pencarian berubah. FAB *pop-in*, mengecil (`shrink()`) saat scroll ke bawah dan melebar (`extend()`) saat scroll ke atas. Teks kosong muncul dengan fade-in. |
| **Form Barang** | Field, kartu preview & tombol muncul bergantian. Angka "Nilai barang ini" memakai *count-up* singkat. Field yang error bergetar. |
| **Efek tekan** | Tombol utama, FAB & kartu item mengecil ke 0.96 saat ditekan lalu kembali (`OnTouchListener` mengembalikan `false` sehingga `onClick` tetap berjalan; hanya dipasang pada view yang *clickable*). |
