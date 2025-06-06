import java.io.*;
import java.util.*;
public class OperatorMahasiswa {
    List<Admin> adminList;
    File mahasiswaFile = new File("dataMahasiswa.txt");
    public static String nimLog, namaLog;

    public boolean cekMahasiswa(String nim, String nama, String password, String prodi) throws Exception {
        try{
            FileReader fr = new FileReader(mahasiswaFile);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(";");
                if(parts[0].equals(nim) && parts[1].equals(nama) && parts[2].equals(password) && parts[3].equals(prodi)){
                    nimLog = nim;
                    namaLog = nama;
                    return true;
                }
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
        return false;
    }
    public void daftarMahasiswa(String nim, String nama, String password, String prodi) throws Exception {
        try{
            if(cekMahasiswa(nim, nama, password, prodi)){
                throw new Exception("Mahasiswa sudah terdaftar!");
            }else {
                nimLog = nim;
                namaLog = nama;
                FileWriter fw = new FileWriter(mahasiswaFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(nim + ";" + nama + ";" + password + ";" + prodi);
                bw.newLine();
                bw.close();
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
