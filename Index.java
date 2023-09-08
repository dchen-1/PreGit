import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Index {
    File index;
    public Index() throws IOException{
            Files.createDirectories(Paths.get("objects"));
            index = new File("Index");
    }

    public void writePair(String fileName) throws IOException{
        HashMap<String,String> map = new HashMap<String,String>();
        Blob b = new Blob(fileName);
        if(index.exists()){
        BufferedReader br = new BufferedReader(new FileReader(index));
        while(br.ready()){
            String str = br.readLine();
            String key = str.substring(0,str.indexOf(":"));
            String value = str.substring(str.indexOf(":")+1);
            map.put(key,value);
        }
        br.close();
        }
        b.writeFile();//this can be changed with writeZip().
        map.put(fileName, b.getSHAString());
        FileWriter  fw = new FileWriter(index);
        for( String str : map.keySet()){
            fw.append(str+":"+map.get(str)+"\n");
        }
        fw.close();
    }

    public void removePair(String fileName) throws IOException{
        HashMap<String,String> map = new HashMap<String,String>();
        BufferedReader br = new BufferedReader(new FileReader(index));
        while(br.ready()){
            String str = br.readLine();
            String key = str.substring(0,str.indexOf(":"));
            String value = str.substring(str.indexOf(":")+1);
            map.put(key,value);
        }
        br.close();
        map.remove(fileName);
        FileWriter  fw = new FileWriter(index);
        for( String str : map.keySet()){
            fw.append(str+":"+map.get(str)+"\n");
        }
        fw.close();
    }

}
