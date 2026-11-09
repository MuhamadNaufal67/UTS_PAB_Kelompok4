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
| 102042300005 | Zabila Nanda Irawan | **005** | Input Validation & Testing: `ValidationUtil005`, `ValidationUtil005Test` (Validasi nama barang, jumlah > 0, harga > 0, unit testing) |
| 102042330009 | Damara Dihyan Darpita | **009** | Search & Sorting Barang: `SearchUtil009`, `SortOption009`, `SearchUtil009Test` (Pencarian & sorting nama A-Z/Z-A, jumlah, harga) |
| 102042300016 | Salsa Salfania | **016** | Integrasi UI & Inventory Interaction: `InventoryInteraction016` (Navigasi form, konfirmasi dialog hapus, empty state, scroll FAB, Toast) |
| 102042300020 | Ibnu Augustiana | **020** | Autentikasi & Session: `User020`, `UserDao020`, tabel `tb_user_020`, `SessionManager020`, `PasswordUtil020`, `LoginActivity`, `RegisterActivity` (semua variabelnya `...020`) |
| 102042330012 | Muhammad Syuja Al Abid | **012** | Room Database & Dashboard: `Item012`, `ItemDao012`, tabel `tb_item_012`, `InventorySummary012`, `AppDatabase012` (`db_inventaris_012`), `DashboardActivity` (semua variabelnya `...012`) |
| 102042300023 | Muhamad Naufal Nazih | **023** | UI Barang & Animasi: `ItemListActivity`, `ItemAdapter023`, `ItemFormActivity`, `UiUtil023`, `AnimUtil023`, `AnggotaBottomSheet023`, `AnggotaAdapter023` (semua variabelnya `...023`) |

Setiap file `.kt` diberi komentar di baris pertama: `// Bagian NIM ... – Nama (Bagian)`.

### 1. Cara membuka di Android Studio
1. Extract file ZIP / clone repository.
2. Android Studio → **File › Open** → pilih folder `UTS_PAB_Kelompok4` → **OK**.
3. Tunggu **Gradle Sync** selesai (butuh internet saat pertama kali).
4. Pilih emulator / HP → klik **Run ▶**.

> Kalau Android Studio menawarkan *upgrade AGP*, boleh di-*skip*.
> Kalau muncul error versi Java/JDK: **Settings › Build, Execution, Deployment › Build Tools › Gradle › Gradle JDK** → pilih JDK 17 atau 21.

### 2. Alur navigasi
`Login → Dashboard → Daftar Barang (RecyclerView + Search + Sorting) → Form Tambah / Edit → Dialog Konfirmasi Hapus`

### 3. Kesesuaian dengan rubrik
| Rubrik | Implementasi |
|---|---|
| Autentikasi & Session (30%) | `RegisterActivity` & `LoginActivity` query ke Room (`UserDao020`). `user_id` disimpan di SharedPreferences (`SessionManager020`). Sesi tetap tersimpan sampai **Logout**. |
| Room Database CRUD (30%) | Entity `User020`, `Item012`; DAO `UserDao020`, `ItemDao012`; `AppDatabase012`. Create/Read/Update/Delete barang. |
| UI & RecyclerView (15%) | `ItemListActivity` + `ItemAdapter023` (ListAdapter + DiffUtil), form di `ItemFormActivity`, Chip Sorting `chipGroupSort009`. |
| Data Integrity & Validation (15%) | `user_id` = **FOREIGN KEY** ke `tb_user_020`; semua query barang memakai `WHERE user_id = ...`. Validasi input `ValidationUtil005` (nama tidak kosong, jumlah > 0, harga > 0), password min. 6 karakter, username unik. |
| Bonus (maks 10%) | ✅ **Total nilai inventaris real-time** di Dashboard (Room `Flow`, `SUM(jumlah*harga)`) · ✅ **Search bar & Filter Chips Sorting** · ✅ **ViewBinding & Modern Animations** |

### 4. Database (Room / SQLite) — `db_inventaris_012`
| Tabel | Kolom |
|---|---|
| `tb_user_020` | `id` (PK), `username` (UNIQUE), `password` (hash SHA-256) |
| `tb_item_012` | `id` (PK), `user_id` (FK → tb_user_020.id), `nama`, `jumlah`, `harga` |

Cek isi tabel saat app berjalan: **View › Tool Windows › App Inspection › Database Inspector**.

### 5. Struktur file
```
app/src/
├── main/java/com/kelompok4/inventaris/
│   ├── data/
│   │   ├── User020.kt, UserDao020.kt                     (NIM 020)
│   │   ├── Item012.kt, ItemDao012.kt,
│   │   │   InventorySummary012.kt, AppDatabase012.kt      (NIM 012)
│   ├── session/
│   │   └── SessionManager020.kt                           (NIM 020)
│   ├── util/
│   │   ├── ValidationUtil005.kt                           (NIM 005)
│   │   ├── SearchUtil009.kt                               (NIM 009)
│   │   ├── InventoryInteraction016.kt                     (NIM 016)
│   │   ├── PasswordUtil020.kt                             (NIM 020)
│   │   └── UiUtil023.kt, AnimUtil023.kt                   (NIM 023)
│   └── ui/
│       ├── auth/LoginActivity.kt, RegisterActivity.kt     (NIM 020)
│       ├── dashboard/DashboardActivity.kt                 (NIM 012)
│       ├── anggota/AnggotaBottomSheet023.kt,
│       │           AnggotaAdapter023.kt                   (NIM 023)
│       └── item/ItemListActivity.kt, ItemAdapter023.kt,
│                ItemFormActivity.kt                       (NIM 023)
└── test/java/com/kelompok4/inventaris/
    └── util/
        ├── ValidationUtil005Test.kt                       (NIM 005)
        └── SearchUtil009Test.kt                           (NIM 009)
```

### 6. Dokumentasi Fitur & Tanggung Jawab 6 Anggota

1. **Zabila Nanda Irawan (NIM 005 - Input Validation & Testing)**
   - Helper validasi `ValidationUtil005.kt` untuk memastikan input nama barang tidak kosong/blank, jumlah harus berupa angka positif (> 0), dan harga harus berupa angka positif (> 0).
   - Pengujian otomatis via Unit Testing (`ValidationUtil005Test.kt`).
   - Integrasi pesan error yang jelas pada `TextInputLayout` serta animasi shake error.

2. **Damara Dihyan Darpita (NIM 009 - Search & Sorting Barang)**
   - Helper `SearchUtil009.kt` dan enum `SortOption009`.
   - Pencarian real-time berdasarkan kata kunci nama barang.
   - Fitur sorting interaktif melalui Material Filter Chips (`chipGroupSort009`): Nama A-Z, Nama Z-A, Stok Terbanyak, Stok Terkecil, Harga Termahal, dan Harga Termurah.
   - Pengujian algoritma sorting & filtering via Unit Testing (`SearchUtil009Test.kt`).

3. **Salsa Salfania (NIM 016 - Integrasi UI & Inventory Interaction)**
   - Helper `InventoryInteraction016.kt` untuk mengelola interaksi pengguna dan navigasi antarlayar.
   - Navigasi pembukaan form dalam mode Tambah (`openItemFormForCreate016`) dan Edit (`openItemFormForEdit016`).
   - Dialog konfirmasi Material3 sebelum penghapusan barang (`showDeleteConfirmation016`).
   - Penanganan visibilitas dan pesan saat daftar barang kosong (`handleEmptyState016`).
   - Penanganan responsif pergerakan scroll FAB (`handleFabScroll016`) dan notifikasi Toast.

4. **Ibnu Augustiana (NIM 020 - Autentikasi & Session)**
   - Entity `User020`, DAO `UserDao020`, dan tabel `tb_user_020`.
   - Pengelolaan sesi login user pada SharedPreferences via `SessionManager020`.
   - Enkripsi password SHA-256 via `PasswordUtil020`.
   - Implementasi `LoginActivity` dan `RegisterActivity`.

5. **Muhammad Syuja Al Abid (NIM 012 - Room Database & Dashboard)**
   - Entity `Item012`, DAO `ItemDao012`, dan tabel `tb_item_012`.
   - Singleton Room Database `AppDatabase012` (`db_inventaris_012`).
   - Model ringkasan statistik `InventorySummary012`.
   - Implementasi `DashboardActivity` dengan kalkulasi total nilai inventaris real-time via Flow.

6. **Muhamad Naufal Nazih (NIM 023 - UI Barang & Animasi)**
   - Implementasi `ItemListActivity`, `ItemFormActivity`, dan `ItemAdapter023` (ListAdapter + DiffUtil).
   - BottomSheet dialog anggota kelompok (`AnggotaBottomSheet023`, `AnggotaAdapter023`).
   - Helper UI & Sistem Insets (`UiUtil023`).
   - Sistem animasi modern Android tanpa library tambahan (`AnimUtil023`, `res/anim/*_023.xml`).

### 7. Skenario Demo & Testing

#### Unit Testing (Automated)
Jalankan perintah berikut di terminal / command prompt:
```bash
./gradlew test
```
- `ValidationUtil005Test`: Menguji skenario validasi input nama, jumlah (positif), dan harga (> 0).
- `SearchUtil009Test`: Menguji skenario pencarian kata kunci dan 6 mode pengurutan barang.

#### Manual Testing Scenario
1. **Validasi Input (NIM 005)**:
   - Masuk ke form Tambah Barang. Tekan Simpan dengan input kosong -> muncul pesan error & animasi shake.
   - Masukkan jumlah = 0 atau negatif -> muncul pesan error angka tidak valid.
   - Masukkan harga = 0 -> muncul pesan error harga harus lebih dari 0.
2. **Pencarian & Sorting (NIM 009)**:
   - Tambahkan beberapa barang (misal: "Laptop", "Mouse", "Keyboard").
   - Ketik kata pada search bar -> list otomatis tersaring.
   - Klik chip sorting ("Nama Z-A", "Stok Terbanyak", "Harga Termahal") -> urutan list langsung berubah sesuai kriteria.
3. **Interaksi & Navigasi (NIM 016)**:
   - Klik barang di list -> berpindah ke Form Edit.
   - Klik tombol Hapus -> muncul dialog konfirmasi. Pilih "Hapus" -> barang terhapus dan Toast notifikasi muncul.
   - Hapus semua barang -> tampil pesan empty state "Belum ada barang".
   - Scroll list ke bawah -> FAB otomatis mengecil (shrink), scroll ke atas -> FAB melebar (extend).
4. **Isolasi Data (NIM 012 & 020)**:
   - Register akun **User A** -> tambah barang -> Logout.
   - Register akun **User B** -> Login B -> daftar barang kosong (data User A terisolasi penuh).
