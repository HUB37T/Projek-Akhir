

import java.time.*;
import java.util.*;
public class Transaksi {
    private String nim;
    private String kodeBuku;
    private LocalDate tanggalPinjam = null;
    private LocalTime jamPinjam = null;
    private HashMap<String, String> daftarPinjaman;

    public Transaksi(String nim, String kodeBuku, LocalDate tanggalPinjam,LocalTime jamPinjam, HashMap<String, String> daftarPinjaman) {
        this.nim = nim;
        this.kodeBuku = kodeBuku;
        this.tanggalPinjam = tanggalPinjam;
        this.jamPinjam = jamPinjam;
        this.daftarPinjaman = daftarPinjaman;
    }
    public String getNim() {
        return nim;
    }
    public String getKodeBuku() {
        return kodeBuku;
    }
    public LocalDate getTanggalPinjam() {
        return tanggalPinjam;
    }
    public LocalTime getJamPinjam() {
        return jamPinjam;
    }
    public HashMap<String, String> getDaftarPinjaman() {
        return daftarPinjaman;
    }

}

