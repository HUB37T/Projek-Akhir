package models;

import java.io.*;

public class Mahasiswa {

    private String nim;
    private String nama;
    private String prodi;
    private String passsword;

    File penggunaFile = new File("data/dataMahasiswa.txt");
    public Mahasiswa(String nim, String nama, String prodi, String passsword) {
        this.nim = nim;
        this.nama = nama;
        this.prodi = prodi;
        this.passsword = passsword;
        try{
            FileWriter fw = new FileWriter(penggunaFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            sb.append(nim).append(";")
                    .append(nama).append(";")
                    .append(prodi).append(";")
                    .append(passsword);
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
    public void setNim(String nim) {
        this.nim = nim;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setProdi(String prodi) {
        this.prodi = prodi;
    }
    public void setPasssword(String passsword) {this.passsword = passsword;}

    public String getNim() {
        return this.nim;
    }

    public String getNama() {
        return this.nama;
    }

    public String getProdi() {
        return this.prodi;
    }
    public String getPasssword() { return this.passsword; }
    public String toString() {
        return nim + "," + nama + "," + prodi;
    }
}
