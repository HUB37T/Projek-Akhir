import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;


public class Perpustakaan {
    ArrayList<Buku> listBuku;
    ArrayList<Mahasiswa> listMahasiswa;
    HashMap<String, String> daftarPinjam;

    public Perpustakaan() {
        listBuku = new ArrayList<>();
        listMahasiswa = new ArrayList<>();
        daftarPinjam = new HashMap<>();
    }

    //Method untuk Tab Buku
    public void simpanBuku(String kode, String judul, TreeSet<String> pengarang) throws Exception{
        for(Buku list : listBuku){
            if(list.getID().equals(kode)){
                throw new Exception("Kode Buku Sudah Ada!");
            }
        }
        listBuku.add(new Buku(kode, judul, pengarang));
    }

    public String cariBukuKode(String kode){
        try{
            File file = new File("dataBuku.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(";");
                if(parts[0].trim().equals(kode)){
                    return "Kode Buku: "+ parts[0] + " Judul: "+ parts[1]+ " Pengarang: "+ parts[2];
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String cariBukuJudul(String judul){
        try{
            File file = new File("dataBuku.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(";");
                if(parts[1].trim().equals(judul)){
                    return "Kode Buku: "+ parts[0] + " Judul: "+ parts[1]+ " Pengarang: "+ parts[2];
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public String cariBukuPengarang(String pengarang){
        try{
            File file = new File("dataBuku.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                if(line.contains(pengarang)){
                    String[] parts = line.split(";");
                    return "Kode Buku: "+ parts[0] + " Judul: "+ parts[1]+ " Pengarang: "+ parts[2];
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void editBuku(String kode, String judul, TreeSet<String> pengarang) throws Exception{
        for(Buku buku : listBuku){
            if(buku.getID().equals(kode)){
                buku.setJudul(judul);
                buku.setPengarang(pengarang);
            }
        }
        File inputFile = new File("dataBuku.txt");
        File tempFile = new File("bukuTemp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {

                String[] parts = currentLine.split(",");

                if (parts.length > 0 && parts[0].equals(kode)) {
                    String pengarangGabung = String.join(",", pengarang);
                    String lineBaru = kode + ";" + judul + ";" + pengarangGabung;
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
            throw new Exception("Buku gagal diedit!");
        }
    }

    public void hapusBuku(String kode) throws Exception{
        Iterator<Buku> iterator = listBuku.iterator();
        while (iterator.hasNext()) {
            Buku buku = iterator.next();
            if (buku.getID().equals(kode)) {
                iterator.remove();
            }
        }
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
        } catch (IOException e) {
            throw new Exception("Buku gagal dihapus!");
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            throw new Exception("Buku gagal dihapus!");
        }
    }
    //Sampai Sini

    //Method untuk Tab Pengguna
    public void simpanPengguna(String nim, String nama, String prodi){
        listMahasiswa.add(new Mahasiswa(nim, nama, prodi));
    }
    public String cariPengguna(String nim){
        for (Mahasiswa mahasiswa : listMahasiswa) {
            if(mahasiswa.getNim().equals(nim)){
                return "NIM: "+ mahasiswa.getNim()+" Nama: "+ mahasiswa.getNama()+" Prodi: "+ mahasiswa.getProdi();
            }
        }
        return null;
    }

    public void editPengguna(String nim, String nama, String prodi) throws Exception{
        for(Mahasiswa mahasiswa : listMahasiswa){
            if(mahasiswa.getNim().equals(nim)){
                mahasiswa.setNama(nama);
                mahasiswa.setProdi(prodi);
            }
        }
        File inputFile = new File("dataMahasiswa.txt");
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

    public void hapusPengguna(String nim) throws Exception{
        Iterator<Mahasiswa> iterator = listMahasiswa.iterator();
        while (iterator.hasNext()) {
            Mahasiswa mahasiswa = iterator.next();
            if (mahasiswa.getNim().equals(nim)) {
                iterator.remove();
            }
        }
        File inputFile = new File("dataMahasiswa.txt");
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
            throw new Exception("User gagal dihapus!");
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            throw new Exception("User gagal dihapus");
        }
    }
    //Sampai Sini


}
