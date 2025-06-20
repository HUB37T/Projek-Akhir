package main;
import models.Buku;

import java.io.*;
import java.time.*;
import java.util.*;
import java.nio.file.*;

public class Perpustakaan {
    ArrayList<Buku> listBuku;
    HashMap<String, String> daftarPinjam;

    private final Path fileBuku = Paths.get("data/dataBuku.txt");
    private final Path filePinjam = Paths.get("data/dataPinjam.txt");

    public Perpustakaan() {
        listBuku = new ArrayList<>();
        daftarPinjam = new HashMap<>();
    }

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
        Path tempFile = Paths.get("data/bukuTemp.txt");
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

        if (!bukuDitemukan) throw new Exception("model.Buku dengan kode " + kode + " tidak ditemukan.");

        Files.move(tempFile, fileBuku, StandardCopyOption.REPLACE_EXISTING);
    }

    public void hapusBuku(String kode) throws Exception {
        Path tempFile = Paths.get("data/bukuTemp.txt");
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

        if (!bukuDitemukan) throw new Exception("model.Buku dengan kode " + kode + " tidak ditemukan.");

        Files.move(tempFile, fileBuku, StandardCopyOption.REPLACE_EXISTING);
    }

    //Method untuk Pinjam model.Buku
    public void kurangiStokBuku(String kodeBuku) throws Exception {
        Path tempFile = Paths.get("data/bukuTemp.txt");
        boolean bukuDitemukan = false;
        boolean stokHabis = false;

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
                        stokHabis = true;
                        writer.write(currentLine); // Tulis kembali baris asli jika stok habis
                    }
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
            }
        }

        Files.move(tempFile, fileBuku, StandardCopyOption.REPLACE_EXISTING);

        if (!bukuDitemukan) throw new Exception("model.Buku dengan kode " + kodeBuku + " tidak ditemukan.");
        if (stokHabis) throw new Exception("Stok buku sudah habis.");
    }

    public void pinjamBuku(String nim, String kodeBuku, String judul, LocalDate tanggal) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePinjam.toString(), true))) {
            bw.write(nim + ";" + kodeBuku + ";" + judul + ";" + tanggal.toString());
            bw.newLine();
        } catch (IOException e) {
            throw new Exception("Gagal mencatat data peminjaman: " + e.getMessage(), e);
        }
    }
    
    //Method untuk kembaliin buku
    public void tambahStokBuku(String kodeBuku) throws Exception {
        Path tempFile = Paths.get("data/bukuTemp.txt");
        boolean bukuDitemukan = false;

        try (BufferedReader reader = Files.newBufferedReader(fileBuku);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(";", -1);
                if (parts.length > 3 && parts[0].equals(kodeBuku)) {
                    bukuDitemukan = true;
                    int stokLama = Integer.parseInt(parts[3]);
                    writer.write(parts[0] + ";" + parts[1] + ";" + parts[2] + ";" + (stokLama + 1));
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
            }
        }

        if (!bukuDitemukan) {
            Files.deleteIfExists(tempFile);
            throw new Exception("model.Buku dengan kode " + kodeBuku + " tidak ditemukan untuk penambahan stok.");
        }
        Files.move(tempFile, fileBuku, StandardCopyOption.REPLACE_EXISTING);
    }

    public void kembalikanBuku(String nim, String kodeBuku) throws Exception {
        Path tempFile = Paths.get("data/pinjamTemp.txt");
        boolean bukuDipinjamDitemukan = false;

        try (BufferedReader reader = Files.newBufferedReader(filePinjam);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(";", -1);
                // Cek apakah NIM dan Kode Buku cocok, jika ya, jangan tulis baris ini (efeknya menghapus)
                if (parts.length > 1 && parts[0].equals(nim) && parts[1].equals(kodeBuku) && !bukuDipinjamDitemukan) {
                    bukuDipinjamDitemukan = true;

                    continue;
                }
                writer.write(currentLine);
                writer.newLine();
            }
            writer.close();
        }

        if (!bukuDipinjamDitemukan) {
            Files.deleteIfExists(tempFile);
            throw new Exception("Anda tidak sedang meminjam buku dengan kode " + kodeBuku + ".");
        }
        Files.move(tempFile, filePinjam, StandardCopyOption.REPLACE_EXISTING);
    }
}
