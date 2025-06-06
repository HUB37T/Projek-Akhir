import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
public class OperatorAdmin {
    List<Admin> adminList;
    File adminFile = new File("dataAdmin.txt");
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
}
