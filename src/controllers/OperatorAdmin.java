package controllers;

import java.io.*;

public class OperatorAdmin {
    File adminFile = new File("data/dataAdmin.txt");
    public boolean cekAdmin(String email, String nama, String password) throws Exception {
        try{
            FileReader fr = new FileReader(adminFile);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(";");
                if(parts[0].equals(email) && parts[1].equals(nama) && parts[2].equals(password)){
                    return true;
                }
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
        return false;
    }
    public void daftarAdmin(String email, String nama, String password) throws Exception {
        try{
            if(cekAdmin(email, nama, password)){
                throw new Exception("model.Admin sudah terdaftar!");
            }else {
                FileWriter fw = new FileWriter(adminFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(email + ";" + nama + ";" + password);
                bw.newLine();
                bw.close();
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
