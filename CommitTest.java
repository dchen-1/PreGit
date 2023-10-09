import static org.apache.commons.codec.digest.MessageDigestAlgorithms.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import Utilities.FileUtils;

public class CommitTest {
    protected static String c1Name;
    protected static String c2Name;
    protected String c1Author = "Victor";
    protected String c1Summary = "i did nothing";
    protected String c2Author = "Victor again";
    protected String c2Summary = "I DID EVERYTHING";
    protected static String f1Name = "1.txt";
    protected static String f2Name = "2.txt";
    protected static String f3Name = "3.txt";
    protected static String f4Name = "dir/4.txt";

    @BeforeAll
    static void createTestFiles() throws Exception{
        FileUtils.createFile(f1Name);
        FileUtils.writeFile(f1Name,"abcd");
        FileUtils.createFile(f2Name);
        FileUtils.writeFile(f2Name, "f");
        FileUtils.createFile(f3Name);
        FileUtils.writeFile(f3Name, "g");
        FileUtils.createDirectory("dir");
        FileUtils.createFile(f4Name);
        FileUtils.writeFile(f4Name, "h");
        FileUtils.createDirectory("./objects");
    }

    @AfterAll
    static void deleteTestFiles() throws Exception {
        if (FileUtils.fileExists(c1Name)) {
            FileUtils.deleteFile(c1Name);
        }
        if (FileUtils.fileExists(c2Name)) {
            FileUtils.deleteFile(c2Name);
        }
        Files.deleteIfExists(Paths.get(f1Name));
        Files.deleteIfExists(Paths.get(f2Name));
        Files.deleteIfExists(Paths.get(f3Name));
        Files.deleteIfExists(Paths.get(f4Name));
        Files.deleteIfExists(Paths.get("Index"));
    }

    @Test
    void testWrite() throws Exception {
        FileUtils.createFile("Index");
        Commit c1 = new Commit(c1Name, c1Summary);
        c1Name = c1.hash;

        // checks if the name of its file in objects is correct
        assertEquals(FileUtils.sha1(
                c1.treeHash + "\n" + c1.previousHash + "\n" + c1.author + "\n" + c1.getDate() + "\n" + c1.summary),
                c1Name);
        // checks if the file contents are correct
        assertEquals(
                c1.treeHash + "\n" + c1.previousHash + "\n" + c1.nextHash + "\n" + c1.author + "\n" + c1.getDate()
                        + "\n" + c1.summary,
                FileUtils.readFile("objects/" + c1Name));
    }

    @Test
    void testSetNext() throws Exception {
        FileUtils.createFile("Index");
        Commit c1 = new Commit(c1Name, c1Summary);
        c1Name = c1.hash;
        FileUtils.createFile("Index");
        Commit c2 = new Commit(c2Name, c2Summary, c1Name);
        c2Name = c2.hash;

        c1.setNext(c2);

        // checks if setNext changed the nextHash variable
        assertEquals(c2Name, c1.nextHash);
        // checks if setNext updated its file in objects without changing the name
        assertEquals(
                c1.treeHash + "\n" + c1.previousHash + "\n" + c1.nextHash + "\n" + c1.author + "\n" + c1.getDate()
                        + "\n" + c1.summary,
                FileUtils.readFile("objects/" + c1Name));
    }

    @Test
    void testCommit1() throws Exception{
        Index i = new Index();
        i.writePair(f1Name);
        i.writePair(f2Name);
        Commit c1 = new Commit(c1Author,c1Summary);
        BufferedReader br = new BufferedReader(new FileReader("./objects/"+c1.hash));
        i = new Index();
        i.writePair(f1Name);
        i.writePair(f2Name);
        String str = br.readLine();
        String str2 = FileUtils.readFile("Index");
        assertEquals(str,new DigestUtils(SHA_1).digestAsHex(str2));
        assertEquals(br.readLine(),c1.previousHash);
        assertEquals(br.readLine(),c1.nextHash);
        br.close();
        FileUtils.deleteDirectory("Index");
    }

    @Test
    public void testCommit2() throws Exception{
        Index i = new Index();
        i.writePair(f3Name);
        i.writePair(f4Name);
        Commit c1 = new Commit(c1Author,c1Summary);
        i = new Index();
        i.addDirectory("dir");
        i.writePair(f3Name);
        i.writePair(f4Name);
        Commit c2 = new Commit(c2Author,c2Summary,c1.getHash());
        i = new Index();
        i.addDirectory("dir");
        i.writePair(f3Name);
        i.writePair(f4Name);
        testMostRecentCommit(c2);
        FileUtils.deleteDirectory("Index");
    }

    @Test
    public void testCommit4() throws Exception{
        Index i = new Index();
        i.writePair(f1Name);
        i.writePair(f2Name);
        Commit c1 = new Commit(c1Author,c1Summary);
        i = new Index();
        i.writePair(f1Name);
        i.writePair(f2Name);
        Commit c2 = new Commit(c2Author,c2Summary,c1.getHash());
        i = new Index();
        i.addDirectory("dir");
        i.writePair(f3Name);
        i.writePair(f4Name);
        Commit c3 = new Commit("David","AAAAAAA",c2.getHash());
        i = new Index();
        i.addDirectory("dir");
        i.writePair(f3Name);
        i.writePair(f4Name);
        Commit c4 = new Commit("Davidx2","last minute changes",c3.getHash());
        i = new Index();
        i.addDirectory("dir");
        i.writePair(f3Name);
        i.writePair(f4Name);
        testCommits(c3,c4);
        testMostRecentCommit(c4);
        FileUtils.deleteDirectory("Index");
    }

    //for older commits
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

    public void testMostRecentCommit(Commit c) throws Exception{
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
        System.out.println(str);
        String str2 = FileUtils.readFile("Index");
        System.out.println(str2);
        assertEquals(new DigestUtils(SHA_1).digestAsHex(str),new DigestUtils(SHA_1).digestAsHex(str2));
        br.readLine();
        assertEquals(c.previousHash,br.readLine());
        assertEquals("",br.readLine());
        br.close();
    }

    @Test
    public void testEditDelete() throws Exception{
        Index i = new Index();
        i.writePair(f1Name);
        Commit c1 = new Commit("a","b");
        i=new Index();
        i.writePair(f2Name);
        Commit c2 = new Commit("c","d",c1.hash);
        i = new Index();
        i.writePair(f3Name);
        i.edit(f1Name,"new content!");
        Commit c3 = new Commit("e","f",c2.hash);
        i = new Index();
        i.edit(f2Name,"oogabooga");
        i.delete(f1Name);
        Commit c4 = new Commit("g","h",c3.hash);
        i = new Index();
        i.delete(f2Name);
        Commit c5 = new Commit("David", "Merge remote-tracking branch 'origin/main' into main",c4.hash);
        String str = "blob:"+new Blob(f3Name).getSHAString()+":"+f3Name;
        assertEquals(FileUtils.readFile("./objects/"+c5.treeHash),str);
    }
}
