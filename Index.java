import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import Utilities.FileUtils;

public class Index {
    File index;
    public Index() throws IOException{
            Files.createDirectories(Paths.get("objects"));
            index = new File("Index");
    }

    public void writePair(String fileName) throws Exception{
        HashMap<String,String> map = new HashMap<String,String>();
        Blob b = new Blob(fileName);
        if(index.exists()&&index.length()!=0){
        BufferedReader br = new BufferedReader(new FileReader(index));
        while(br.ready()){
            String str = br.readLine();
            str = str.substring(str.indexOf(":")+1);
            String value = str.substring(0,str.indexOf(":"));
            String key = str.substring(str.indexOf(":")+1);
            map.put(key,value);
        }
        br.close();
        }
        b.writeFile();//this can be changed with writeZip().
        map.put(fileName, b.getSHAString());
        FileWriter  fw = new FileWriter(index);
        for( String str : map.keySet()){
            fw.append("blob"+":"+map.get(str)+":"+str+"\n");
        }
        fw.close();
    }

    public void removePair(String fileName) throws IOException{
        HashMap<String,String> map = new HashMap<String,String>();
        BufferedReader br = new BufferedReader(new FileReader(index));
        while(br.ready()){
            String str = br.readLine();
            str = str.substring(str.indexOf(":")+1);
            String value = str.substring(0,str.indexOf(":"));
            String key = str.substring(str.indexOf(":")+1);
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

    public void addDirectory(String path) throws Exception{
        Files.deleteIfExists(Paths.get("Index"));
        FileUtils.writeFile("Index", FileUtils.readFile("./objects/"+Paths.get(Tree.addDirectory(path))));
    }

}
