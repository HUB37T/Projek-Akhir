

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Mahasiswa {

    private String nim;
    private String nama;
    private String prodi;
    private long denda = 0;

    File penggunaFile = new File("dataMahasiswa.txt");
    public Mahasiswa(String nim, String nama, String prodi) {

    File penggunaFile = new File("dataUser.txt");

        this.nim = nim;
        this.nama = nama;
        this.prodi = prodi;
        try{
            FileWriter fw = new FileWriter(penggunaFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            sb.append(nim).append(";")
                    .append(nama).append(";")
                    .append(prodi);
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
    public void setDenda(long denda) {
        this.denda = denda;
    }

    public String getNim() {
        return this.nim;
    }

    public String getNama() {
        return this.nama;
    }

    public String getProdi() {
        return this.prodi;
    }


    public long getDenda() {
        return this.denda;
    }
    

    public String toString() {
        return nim + "," + nama + "," + prodi;
    }


}
