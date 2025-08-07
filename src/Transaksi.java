import java.time.*;
public class Transaksi {
    private String nim;
    private String kodeBuku;
    private LocalDate tanggalPinjam = null;
    private String namaBuku;

    public Transaksi(String nim, String kodeBuku, LocalDate tanggalPinjam,String namaBuku ) {
        this.nim = nim;
        this.kodeBuku = kodeBuku;
        this.tanggalPinjam = tanggalPinjam;
        this.namaBuku = namaBuku;
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
    public String getNamaBuku() {
        return namaBuku;
    }

}

