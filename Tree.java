import static org.apache.commons.codec.digest.MessageDigestAlgorithms.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;

public class Tree {
    File tree;
    private String hdigest;
    private String content;

    public Tree() throws IOException {
        tree = new File("Tree");
        tree.createNewFile();
    }

    public void add(String line) throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        if (tree.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(tree));
            while (br.ready()) {
                String str = br.readLine();
                String key = str.substring(0, str.indexOf(":"));
                String value = str.substring(str.indexOf(":") + 1);
                map.put(key, value);
            }
            br.close();
        }
        String key = line.substring(0, line.indexOf(":"));
        String value = line.substring(line.indexOf(":") + 1);
        map.put(key, value);
        FileWriter fw = new FileWriter(tree);
        int count = 0;
        for (String str : map.keySet()) {
            count++;
            if (count != map.size()) {
                fw.append(str + ":" + map.get(str) + "\n");
            } else {
                fw.append(str + ":" + map.get(str));
            }
        }
        // fw.append(line + "\n");
        fw.close();
    }

    public void remove(String line) throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new FileReader(tree));
        while (br.ready()) {
            String str = br.readLine();
            String key = str.substring(0, str.indexOf(":"));
            String value = str.substring(str.indexOf(":") + 1);
            map.put(key, value);
        }
        br.close();
        map.remove(line.substring(0, line.indexOf(":")));
        FileWriter fw = new FileWriter(tree);
        int count = 0;
        for (String str : map.keySet()) {
            count++;
            if (count != map.size()) {
                fw.append(str + ":" + map.get(str) + "\n");
            } else {
                fw.append(str + ":" + map.get(str));
            }
        }
        fw.close();
    }

    public void save() throws IOException {
        Files.createDirectories(Paths.get("./objects/"));
        BufferedReader br = new BufferedReader(new FileReader(tree));
        StringBuilder bob = new StringBuilder();
        while (br.ready()) {
            bob.append((char) br.read());
        }
        content = new String(bob);
        hdigest = new DigestUtils(SHA_1).digestAsHex(content);
        br.close();
        File file = new File("./objects/", hdigest);
        file.createNewFile();
        FileWriter writer = new FileWriter(hdigest);
        writer.append(content);
        writer.close();
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
        t.save();
        return t.getSHA1();
    }
}
