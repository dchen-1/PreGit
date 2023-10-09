import static org.apache.commons.codec.digest.MessageDigestAlgorithms.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import Utilities.FileUtils;

public class Tester {
    protected static String c1Name;
    protected static String c2Name;
    protected static String c1Author = "Victor";
    protected static String c1Summary = "i did nothing";
    protected static String c2Author = "Victor again";
    protected static String c2Summary = "I DID EVERYTHING";
    protected static String f1Name = "1.txt";
    protected static String f2Name = "2.txt";
    protected static String f3Name = "3.txt";
    protected static String f4Name = "dir/4.txt";
    public static String sha1Hash(String str){
        return new DigestUtils(SHA_1).digestAsHex(str);
    }

    public static void setup() throws Exception{
        FileUtils.createFile(f1Name);
        FileUtils.writeFile(f1Name,"acbed");
        FileUtils.createFile(f2Name);
        FileUtils.writeFile(f2Name, "f");
        FileUtils.createFile(f3Name);
        FileUtils.writeFile(f3Name, "g");
        FileUtils.createDirectory("dir");
        FileUtils.createFile(f4Name);
        FileUtils.writeFile(f4Name, "h");
    }

    public static void clean() throws Exception{
        //         if (FileUtils.fileExists(c1Name)) {
        //     FileUtils.deleteFile(c1Name);
        // }
        // if (FileUtils.fileExists(c2Name)) {
        //     FileUtils.deleteFile(c2Name);
        // }
        Files.deleteIfExists(Paths.get(f1Name));
        Files.deleteIfExists(Paths.get(f2Name));
        Files.deleteIfExists(Paths.get(f3Name));
        Files.deleteIfExists(Paths.get(f4Name));
        Files.deleteIfExists(Paths.get("Index"));
        FileUtils.deleteDirectory("./dir/");
    }
    public static void main(String[] args) throws Exception {
        setup();
        Index i = new Index();
        i.writePair(f1Name);
        Commit c1 = new Commit("a","b");
        i=new Index();
        i.writePair(f2Name);
        Commit c2 = new Commit("c","d",c1.hash);
        i = new Index();
        i.edit(f1Name,"new content!");
        i.writePair(f3Name);
        Commit c3 = new Commit("e","f",c2.hash);
        // i = new Index();
        // i.edit(f2Name,"oogabooga");
        // i.delete(f1Name);
        // Commit c4 = new Commit("g","h",c3.hash);
        // i = new Index();
        // i.delete(f2Name);
        // Commit c5 = new Commit("David", "Merge remote-tracking branch 'origin/main' into main",c4.hash);
        // String str = "blob:"+new Blob(f3Name).getSHAString()+":"+f3Name;
        // assertEquals(FileUtils.readFile("./objects/"+c5.treeHash),str);
        clean();
    }

        public static void testCommits(Commit c1, Commit c2) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader("./objects/"+c1.hash));
        String str = "";
        BufferedReader br2 = new BufferedReader(new FileReader("./objects/"+c1.treeHash));
        ArrayList<String> list = new ArrayList<String>();
        while(br2.ready()){
            list.add(br2.readLine());
        }
        list.remove(list.size()-1);
        br2.close();
        for(String s : list){str+=s+"\n";}
        str = str.substring(0, str.length()-1);
        String str2 = FileUtils.readFile("Index");
        assertEquals(new DigestUtils(SHA_1).digestAsHex(str),new DigestUtils(SHA_1).digestAsHex(str2));
        br.readLine();
        assertEquals(c1.previousHash,br.readLine());
        assertEquals(c2.hash,br.readLine());
        br.close();
    }

    public static void testMostRecentCommit(Commit c) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader("./objects/"+c.hash));
        String str = "";
        BufferedReader br2 = new BufferedReader(new FileReader("./objects/"+c.treeHash));
        ArrayList<String> list = new ArrayList<String>();
        while(br2.ready()){
            list.add(br2.readLine());
        }
        list.remove(list.size()-1);
        br2.close();
        for(String s : list){str+=s+"\n";}
        str = str.substring(0, str.length()-1);
        String str2 = FileUtils.readFile("Index");
        assertEquals(new DigestUtils(SHA_1).digestAsHex(str),new DigestUtils(SHA_1).digestAsHex(str2));
        br.readLine();
        assertEquals(c.previousHash,br.readLine());
        assertEquals("",br.readLine());
        br.close();
    }
}
