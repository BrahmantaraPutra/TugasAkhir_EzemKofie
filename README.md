CARA AGAR KODE BISA BERJALAN

menjalankan bakend :
( penggguna emulator android studio ) = di folder backend jalankan EzemKofi.API.exe 
( pengguna hp fisik ) = buka folder backend di terminal lalu jalankan .\EzemKofi.API.exe --Urls "http://0.0.0.0:5000/"
untuk melihat dokumentasi akses di browsers dengan url = http://localhost:5000/swagger/

list file berisi base url api dan path image
api/ApiClient
adapter/CoffeeAdapter
adapter/TopAdapter
CoffeDetailActivity

pengguna emulator
ganti semua ip di url file file di atas dengan localhost ( contoh = "http://localhost:5000/" ) 

pengguna hp fisik
buka terminal lalu cek "ipconfig" dan ambil ip dari laptop/pc yang kalian pakai

ganti semua ip di url dengan ip device kalian ( contoh = "http://192.168.0.108:5000/" )
