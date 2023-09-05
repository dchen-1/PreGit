import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.codec.digest.DigestUtils;

public class Blob{
    public Blob(String fileName) throws IOException{
         byte [] digest = new DigestUtils(SHA_224).digest(fileName);
        String hdigest = new DigestUtils(SHA_224).digestAsHex(new File("pom.xml"));
        StringBuilder bob = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while(br.ready()){
            bob.append(br.read());
                }
        
    }
}
