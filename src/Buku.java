

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.time.*;

public class Buku {
    private String kodeBuku;
    private String judulBuku;
    private TreeSet<String> Pengarang;
    private int jumlah;
    File bukuFile = new File("dataBuku.txt");

    public Buku(String kodeBuku ,String judulBuku, TreeSet<String> Pengarang, int jumlah) {
        this.kodeBuku = kodeBuku;
        this.judulBuku = judulBuku;
        this.Pengarang = Pengarang;
        this.jumlah = jumlah;
        try{
            FileWriter fw = new FileWriter(bukuFile,true);
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            sb.append(kodeBuku).append(";")
                    .append(judulBuku).append(";")
                    .append(getPengarang().toString().trim()).append(";")
                    .append(jumlah);

            String hasil = sb.toString();
            if (!hasil.isBlank()) {
                bw.write(hasil);
                bw.newLine();
            }
            bw.close();
            fw.close();
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
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
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

    public int getJumlah(){
        return this.jumlah;
    }

    public StringJoiner getPengarang() {
        StringJoiner pengarang = new StringJoiner(", ");
        for(String s : Pengarang) {
            pengarang.add(s);
        }
        return pengarang;
    }

    public String toString() {
        return kodeBuku + ',' + judulBuku + ',' + getPengarang() + ',' + getJumlah();
    }
}
