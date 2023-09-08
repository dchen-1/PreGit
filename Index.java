import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Index {
    HashMap map = new HashMap<String,String>();
    File index;
    public Index() throws IOException{
                    Files.createDirectories(Paths.get("objects"));
            index = new File("objects\\Index");
    }

    public void writePair(String fileName) throws IOException{
        Blob b = new Blob("objects\\Index");
        map.put(fileName, b.getSHAString());
        FileWriter fw = new FileWriter(index);
        fw.write(fileName+":"+b.getSHAString());
    }

    public String removePair(String fileName){
        String sha = (String) map.get(fileName);
        map.remove(fileName);
        return fileName+":"+sha;
    }

}
