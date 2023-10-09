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

import Utilities.FileUtils;

public class Index {
    File index;

    public Index() throws Exception {
        Files.createDirectories(Paths.get("objects"));
        index = new File("Index");
        FileUtils.createFile("Index");
    }

    public void writePair(String fileName) throws Exception {
        Blob b = new Blob(fileName);
        if (index.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(index));
            FileWriter fw = new FileWriter(index);
            while (br.ready()) {
                String str = br.readLine();
                if (str.contains("*deleted*") || str.contains("*edited*")) {
                    fw.write(str);
                }
                str = str.substring(str.indexOf(":") + 1);
                String value = str.substring(0, str.indexOf(":"));
                String key = str.substring(str.indexOf(":") + 1);
                fw.write("blob" + ":" + value + ":" + key + "\n");
            }
            fw.write("blob" + ":" + b.getSHAString() + ":" + fileName + "\n");
            br.close();
            fw.close();
        }

    }

    public void removePair(String fileName) throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new FileReader(index));
        while (br.ready()) {
            String str = br.readLine();
            str = str.substring(str.indexOf(":") + 1);
            String value = str.substring(0, str.indexOf(":"));
            String key = str.substring(str.indexOf(":") + 1);
            map.put(key, value);
        }
        br.close();
        map.remove(fileName);
        FileWriter fw = new FileWriter(index);
        for (String str : map.keySet()) {
            fw.append(str + ":" + map.get(str) + "\n");
        }
        fw.close();
    }

    public void addDirectory(String path) throws Exception {
        Files.deleteIfExists(Paths.get("Index"));
        FileUtils.writeFile("Index", FileUtils.readFile("./objects/" + Paths.get(Tree.addDirectory(path))));
    }

    public void delete(String fileToDelete) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("Index"));
        String add = "";
        while (br.ready()) {
            add += br.readLine() + "\n";
        }
        add += "*deleted* " + fileToDelete;
        br.close();
        FileUtils.writeFile("Index", add);
    }

    public void edit(String fileToEdit, String edits) throws Exception {
        String path = "./objects/"+new DigestUtils(SHA_1).digestAsHex(edits);
        FileUtils.createFile(path);
        FileUtils.writeFile(path, edits);
        BufferedReader br = new BufferedReader(new FileReader("Index"));
        String add = "";
        while (br.ready()) {
            add += br.readLine() + "\n";
        }
        add += "*edited* " + fileToEdit;
        br.close();
        FileUtils.writeFile("Index", add);
    }

}
