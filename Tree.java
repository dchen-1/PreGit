import static org.apache.commons.codec.digest.MessageDigestAlgorithms.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;

public class Tree {
    ArrayList<String> trees = new ArrayList<String>();
    HashMap<String,String> blobs = new HashMap<String,String>();
    String hdigest;
    public Tree() throws RuntimeException{
        if(!Files.exists(Paths.get("./objects/"))){
            new File("./objects/").mkdirs();
        }
    }
    public void add(String add) throws IOException{
        String str = add.replaceAll("\\s", "");
        String pre = str.substring(0,str.indexOf(":"));
        if(pre.equals("tree")){
            String pos = str.substring(str.indexOf(":")+1);
            if(!trees.contains(pos)){
            trees.add(pos);
            }
        }
        else if(pre.equals("blob")){
            String blub = str.substring(str.indexOf(":")+1);
            blobs.put(blub.substring(blub.indexOf(":")+1),blub.substring(0,blub.indexOf(":")));
        }
    }
    public void remove(String remove){
        if(blobs.containsKey(remove)){
            blobs.remove(remove, blobs.get(remove));
        }
        else if(trees.contains(remove)){
            trees.remove(trees.indexOf(remove));
        }
    }

    public void writeToFile() throws IOException{
        String str = "";
        for(String s : blobs.keySet()){
            str+="blob:"+blobs.get(s)+":"+s+"\n";
        }
        for(String ss : trees){
            str+="tree:"+ss+"\n";
        }
        if (!str.isEmpty()){
            str = str.substring(0, str.length()-1);
        }
        String sha1 = new DigestUtils(SHA_1).digestAsHex(str);
        hdigest=sha1;
        FileWriter fw = new FileWriter("./objects/"+sha1);
        fw.write(str);
        fw.close();
    }
    public String getSHA1() {
        return hdigest;
    }

    public static String addDirectory(String directoryPath) throws IOException{
        File[] files = new File(directoryPath).listFiles();
        Tree t = new Tree();
        for(File f : files){
            if(f.isDirectory()){
                String sha = addDirectory(f.getPath());
                t.add("tree:"+sha+":"+f.getPath());
            }
            else{
            t.add("blob:"+ new DigestUtils(SHA_1).digestAsHex(f)+":"+f.getPath());
            }
        }
        t.writeToFile();
        return t.getSHA1();
    }

    public String addTree(String directoryPath) throws IOException{
        File[] files = new File(directoryPath).listFiles();
        for(File f : files){
            if(f.isDirectory()){
                String sha = addDirectory(f.getPath());
                this.add("tree:"+sha+":"+f.getPath());
            }
            else{
            this.add("blob:"+ new DigestUtils(SHA_1).digestAsHex(f)+":"+f.getPath());
            }
        }
        this.writeToFile();
        return this.getSHA1();
    }
}
