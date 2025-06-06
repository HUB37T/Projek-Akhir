import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;
import java.util.TreeSet;

public class Buku {
    private String kodeBuku;
    private String judulBuku;
    private TreeSet<String> Pengarang;
    private boolean dipinjam = false;
    File bukuFile = new File("dataBuku.txt");


    public Buku(String kodeBuku ,String judulBuku, TreeSet<String> Pengarang) {
        this.kodeBuku = kodeBuku;
        this.judulBuku = judulBuku;
        this.Pengarang = Pengarang;
        try{
            FileWriter fw = new FileWriter(bukuFile,true);
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            sb.append(kodeBuku).append(";")
                    .append(judulBuku).append(";")
                    .append(getPengarang().toString().trim());

            String hasil = sb.toString();
            if (!hasil.isBlank()) {
                bw.write(hasil);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setID(String id) {
        this.kodeBuku = id;
    }
    public void setJudul(String judul) {
        this.judulBuku = judul;
    }
    public void setStatus(boolean status) {
        this.dipinjam = status;
    }
    public void setPengarang(TreeSet<String> Pengarang) {
        this.Pengarang = Pengarang;
    }

    public String getID() {
        return this.kodeBuku;
    }

    public String getJudul() {
        return this.judulBuku;
    }

    public boolean getStatus() {
        return this.dipinjam;
    }

    public StringJoiner getPengarang() {
        StringJoiner pengarang = new StringJoiner(", ");
        for(String s : Pengarang) {
            pengarang.add(s);
        }
        return pengarang;
    }

    public String toString() {
        return kodeBuku + ',' + judulBuku + ',' + getPengarang();
    }


}
