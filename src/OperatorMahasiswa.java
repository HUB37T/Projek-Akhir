import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class OperatorMahasiswa {
    File mahasiswaFile = new File("./data/dataMahasiswa.txt");
    public static String nimLog, namaLog;

    public boolean cekMahasiswa(String nim, String nama, String password, String prodi) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(mahasiswaFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", -1); // Gunakan -1 untuk handle baris kosong
                if (parts.length == 4 && parts[0].equals(nim) && parts[1].equals(nama) && parts[2].equals(password) && parts[3].equals(prodi)) {
                    nimLog = nim;
                    namaLog = nama;
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }

    public void daftarMahasiswa(String nim, String nama, String password, String prodi) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(mahasiswaFile))) {
            String line;
            while((line = br.readLine()) != null) {
                if(line.startsWith(nim + ";")) {
                    throw new Exception("NIM " + nim + " sudah terdaftar!");
                }
            }
        } catch (FileNotFoundException e) {
        }

        nimLog = nim;
        namaLog = nama;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(mahasiswaFile, true))) {
            bw.write(nim + ";" + nama + ";" + password + ";" + prodi);
            bw.newLine();
        }
    }

    public String[] getMahasiswaData(String nim) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(mahasiswaFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(nim + ";")) {
                    return line.split(";", -1);
                }
            }
        }
        return null;
    }

    public void editMahasiswa(String oldNim, String newNama, String newPassword, String newProdi) throws IOException {
        Path source = Paths.get("data/dataMahasiswa.txt");
        Path temp = Paths.get("data/mahasiswaTemp.txt");
        
        try (BufferedReader reader = Files.newBufferedReader(source);
             BufferedWriter writer = Files.newBufferedWriter(temp)) {
            String currentLine;
            boolean found = false;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith(oldNim + ";")) {
                    writer.write(oldNim + ";" + newNama + ";" + newPassword + ";" + newProdi + System.lineSeparator());
                    found = true;
                } else {
                    writer.write(currentLine + System.lineSeparator());
                }
            }
            if (!found) {
                throw new IOException("Mahasiswa dengan NIM " + oldNim + " tidak ditemukan.");
            }
        }
        Files.move(temp, source, StandardCopyOption.REPLACE_EXISTING);
    }

    public void hapusMahasiswa(String nim) throws IOException {
        Path source = Paths.get("data/dataMahasiswa.txt");
        Path temp = Paths.get("data/mahasiswaTemp.txt");
        
        try (BufferedReader reader = Files.newBufferedReader(source);
             BufferedWriter writer = Files.newBufferedWriter(temp)) {
            String currentLine;
            boolean found = false;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith(nim + ";")) {
                    found = true;
                    continue;
                }
                writer.write(currentLine + System.lineSeparator());
            }
            if (!found) {
                throw new IOException("Mahasiswa dengan NIM " + nim + " tidak ditemukan.");
            }
        }
        Files.move(temp, source, StandardCopyOption.REPLACE_EXISTING);
    }
}