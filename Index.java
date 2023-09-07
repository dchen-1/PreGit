import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Index {
    HashMap map = new HashMap<String,String>();
    public Index() throws IOException{
            File index = new File("objects\\Index");
            Files.createDirectories(Paths.get("objects"));
    }

    public void writePair(String fileName) throws IOException{
        Blob b = new Blob(fileName);
        map.put(fileName, b.getSHAString());
        FileWriter fw = new FileWriter(fileName);
        fw.write(fileName+":"+b.getSHAString());
    }

    public String removePair(String fileName){
        String sha = (String) map.get(fileName);
        map.remove(fileName);
        return fileName+":"+sha;
    }

}
