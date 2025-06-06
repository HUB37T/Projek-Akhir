

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;


public class Perpustakaan {
    ArrayList<Buku> listBuku;
    ArrayList<Mahasiswa> listMahasiswa;
    HashMap<String, String> daftarPinjam;

    private final Path fileBuku = Paths.get("dataBuku.txt");
    private final Path filePinjam = Paths.get("dataPinjam.txt");

    public Perpustakaan() {
        listBuku = new ArrayList<>();
        listMahasiswa = new ArrayList<>();
        daftarPinjam = new HashMap<>();
    }
    //Method untuk Tab Buku
    public void simpanBuku(String kode, String judul, TreeSet<String> pengarang, int i){
        listBuku.add(new Buku(kode, judul, pengarang, i));
    }

    public String cariBuku(String kode) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(fileBuku)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length > 0 && parts[0].equals(kode)) {
                    return line;
                }
            }
        }
        return null;
    }


    public void editBuku(String kode, String judul, TreeSet<String> pengarang, int jumlah) throws Exception {
        Path tempFile = Paths.get("bukuTemp.txt");
        boolean bukuDitemukan = false;

        try (BufferedReader reader = Files.newBufferedReader(fileBuku);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(";", -1);
                if (parts.length > 0 && parts[0].equals(kode)) {
                    String pengarangGabung = String.join(",", pengarang);
                    writer.write(kode + ";" + judul + ";" + pengarangGabung + ";" + jumlah);
                    bukuDitemukan = true;
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
            }
        }

        if (!bukuDitemukan) throw new Exception("Buku dengan kode " + kode + " tidak ditemukan.");

        Files.move(tempFile, fileBuku, StandardCopyOption.REPLACE_EXISTING);
    }

    public void hapusBuku(String kode) throws Exception {
        Path tempFile = Paths.get("bukuTemp.txt");
        boolean bukuDitemukan = false;

        try (BufferedReader reader = Files.newBufferedReader(fileBuku);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(";", -1);
                if (parts.length > 0 && parts[0].equals(kode)) {
                    bukuDitemukan = true;
                    continue;
                }
                writer.write(currentLine);
                writer.newLine();
            }
        }

        if (!bukuDitemukan) throw new Exception("Buku dengan kode " + kode + " tidak ditemukan.");

        Files.move(tempFile, fileBuku, StandardCopyOption.REPLACE_EXISTING);
    }

    public void kurangiStokBuku(String kodeBuku) throws Exception {
        Path tempFile = Paths.get("bukuTemp.txt");
        boolean bukuDitemukan = false;

        try (BufferedReader reader = Files.newBufferedReader(fileBuku);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(";", -1);
                if (parts.length > 3 && parts[0].equals(kodeBuku)) {
                    bukuDitemukan = true;
                    int stokLama = Integer.parseInt(parts[3]);
                    if (stokLama > 0) {
                        writer.write(parts[0] + ";" + parts[1] + ";" + parts[2] + ";" + (stokLama - 1));
                    } else {
                        throw new Exception("Stok buku '" + parts[1] + "' sudah habis.");
                    }
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
            }
        }

        if (!bukuDitemukan) throw new Exception("Buku dengan kode " + kodeBuku + " tidak ditemukan.");

        Files.move(tempFile, fileBuku, StandardCopyOption.REPLACE_EXISTING);
    }

    public void pinjamBuku(String nim, String kodeBuku, String judul, LocalDate tanggal) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePinjam.toString(), true))) {
            bw.write(nim + ";" + kodeBuku + ";" + judul + ";" + tanggal.toString());
            bw.newLine();
        } catch (IOException e) {
            throw new Exception("Gagal mencatat data peminjaman: " + e.getMessage(), e);
        }
    }

    //Sampai Sini

    //Method untuk Tab Mahasiswa
    public void simpanMahasiswa(String nim, String nama, String prodi){
        listMahasiswa.add(new Mahasiswa(nim, nama, prodi));
    }
    public String cariMahasiswa(String nim){
        for (Mahasiswa pengguna : listMahasiswa) {
            if(pengguna.getNim().equals(nim)){
                return "NIM: "+ pengguna.getNim()+" Nama: "+pengguna.getNama()+" Prodi: "+pengguna.getProdi();
            }
        }
        return null;
    }

    public void editMahasiswa(String nim, String nama, String prodi) throws Exception{
        for(Mahasiswa pengguna : listMahasiswa){
            if(pengguna.getNim().equals(nim)){
                pengguna.setNama(nama);
                pengguna.setProdi(prodi);
            }
        }
        File inputFile = new File("dataUser.txt");
        File tempFile = new File("userTemp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {

                String[] parts = currentLine.split(",");

                if (parts.length > 0 && parts[0].equals(nim)) {
                    String lineBaru = nim + ";" + nama + ";" + prodi;
                    writer.write(lineBaru);
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            throw new Exception("User gagal diedit!");
        }
    }

    public void hapusMahasiswa(String nim) throws Exception{
        for (Mahasiswa pengguna : listMahasiswa) {
            if(pengguna.getNim().equals(nim)){
                listMahasiswa.remove(pengguna);
            }
        }
        File inputFile = new File("dataUser.txt");
        File tempFile = new File("userTemp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {

                String[] parts = currentLine.split(";");
                if (parts.length > 0 && parts[0].equals(nim)) {
                    continue;
                }
                writer.write(currentLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            throw new Exception("User gagal dihapus");
        }
    }
    //Sampai Sini

}
