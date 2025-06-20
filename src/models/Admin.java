package models;

import java.io.*;

public class Admin {
    private String email;
    private String nama;
    private String password;
    File penggunaFile = new File("data/dataAdmin.txt");

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
                    .append(password).append("\n");
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
    public void setEmail(String email) {
        this.email = email;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNama() {
        return this.nama;
    }

    public String getPassword() {
        return this.password;
    }

    public String toString() {
        return email + "," + nama + "," + password;
    }
}