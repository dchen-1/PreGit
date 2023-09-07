import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.digest.DigestUtils;

public class Blob{
    private String hdigest;
    private String content;
    public Blob(String fileName) throws IOException{
        hdigest = new DigestUtils(SHA_1).digestAsHex(fileName);
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder bob = new StringBuilder();
        while(br.ready()){
            bob.append(br.read());
                }
        content = new String(bob);
        br.close();
    }

    public void writeFile() throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter(hdigest));
        bw.write(content);
        bw.close();
    }

    public String getSHAString(){
        return hdigest;
    }


}
