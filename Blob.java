import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.digest.DigestUtils;

public class Blob{
    private String hdigest;
    private String content;
    byte[] digest;
    public Blob(String fileName) throws IOException{
        digest = new DigestUtils(SHA_1).digest(fileName);
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
        BufferedWriter bw = new BufferedWriter(new FileWriter("objects/"+hdigest));
        bw.write(content);
        bw.close();
    }

    public String getSHAString(){
        return hdigest;
    }

    public void writeZip() throws IOException{
        byte[] data = content.getBytes();
        File f = new File("objects/"+digest.toString());
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
        ZipEntry e = new ZipEntry(digest.toString());
        out.putNextEntry(e);
        out.write(data,0,data.length);
        out.closeEntry();
        out.close();
    }

}
