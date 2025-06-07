

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


public class Perpustakaan {
    ArrayList<Buku> listBuku;
    ArrayList<Mahasiswa> listMahasiswa;
    HashMap<String, String> daftarPinjam;
    File file = new File("dataBuku.txt");

    public Perpustakaan() {
        listBuku = new ArrayList<>();
        listMahasiswa = new ArrayList<>();
        daftarPinjam = new HashMap<>();
    }

    //Method untuk Tab Buku
    public void simpanBuku(String kode, String judul, TreeSet<String> pengarang, int i){
        listBuku.add(new Buku(kode, judul, pengarang, i));
    }

    public String cariBuku(String kode){
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(";");
                if(parts[0].equals(kode)){
                    return parts[0] + ";"+ parts[1]+ ";"+ parts[2]+";"+parts[3];
                }
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void editBuku(String kode, String judul, TreeSet<String> pengarang, int jumlah) throws Exception {

        Path inputFile = Paths.get("dataBuku.txt");
        Path tempFile = Paths.get("bukuTemp.txt");

        try (BufferedReader reader = Files.newBufferedReader(inputFile);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            String currentLine;
            boolean bukuDitemukan = false;

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
            reader.close();
            writer.close();

            if (!bukuDitemukan) {
                throw new Exception("Buku dengan kode " + kode + " tidak ditemukan.");
            }
        } catch (IOException e) {
            throw new Exception("Terjadi kesalahan saat membaca/menulis file: " + e.getMessage());
        }

        try {
            Files.move(tempFile, inputFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new Exception("Gagal menyimpan perubahan ke file utama: " + e.getMessage());
        }
    }

    public void hapusBuku(String kode) throws Exception{
        File inputFile = new File("dataBuku.txt");
        File tempFile = new File("bukuTemp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {

                String[] parts = currentLine.split(";");
                if (parts.length > 0 && parts[0].equals(kode)) {
                    continue;
                }
                writer.write(currentLine);
                writer.newLine();
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            throw new Exception("Buku gagal dihapus!");
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
