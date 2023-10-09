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
    public Index() throws Exception{
            Files.createDirectories(Paths.get("objects"));
            index = new File("Index");
            FileUtils.createFile("Index");
    }

    public void writePair(String fileName) throws Exception{
        // HashMap<String,String> map = new HashMap<String,String>();
        Blob b = new Blob(fileName);
        StringBuilder sb = new StringBuilder();
        b.writeFile();//this can be changed with writeZip().
        if(index.exists()&&index.length()!=0){
            BufferedReader br = new BufferedReader(new FileReader(index));
            while(br.ready()){
                sb.append(br.readLine()+"\n");
            }
            br.close();
        }
            sb.append("blob:"+b.getSHAString()+":"+fileName);
            FileUtils.writeFile(index.getPath(),sb.toString());
        
        // if(index.exists()&&index.length()!=0){
        // BufferedReader br = new BufferedReader(new FileReader(index));
        // while(br.ready()){
        //     String str = br.readLine();
        //     str = str.substring(str.indexOf(":")+1);
        //     String value = str.substring(0,str.indexOf(":"));
        //     String key = str.substring(str.indexOf(":")+1);
        //     map.put(key,value);
        // }
        // br.close();
        // }
        // map.put(fileName, b.getSHAString());
        // FileWriter  fw = new FileWriter(index);
        // for( String str : map.keySet()){
        //     fw.append("blob"+":"+map.get(str)+":"+str+"\n");
        // }
        // fw.close();
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

    public void delete(String fileToDelete) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader("Index"));
        String add = "";
        while(br.ready()){
            add+=br.readLine()+"\n";
        }
        add+="*deleted* "+fileToDelete;
        br.close();
        FileUtils.writeFile("Index", add);
    }

    public void edit(String fileToEdit, String edits) throws Exception{
        FileUtils.writeFile(fileToEdit, edits);
        BufferedReader br = new BufferedReader(new FileReader("Index"));
        String add = "";
        while(br.ready()){
            add+=br.readLine()+"\n";
        }
        add+="*edited* "+fileToEdit;
        br.close();
        FileUtils.writeFile("Index", add);
    }
}
