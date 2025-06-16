import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Admin {
    private String email;
    private String nama;
    private String password;
    File penggunaFile = new File("dataAdmin.txt");

    public Admin(String email, String nama, String password) {
        this.email = email;
        this.nama = nama;
        this.password = password;
        try{
            FileWriter fw = new FileWriter(penggunaFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            sb.append(email).append(";")
                    .append(nama).append(";")
                    .append(password);
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
    public String toString() {
        return email + "," + nama + "," + password;
    }
}
