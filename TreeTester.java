import static org.apache.commons.codec.digest.MessageDigestAlgorithms.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTester
{
    public String sha1Hash(String str){
        return new DigestUtils(SHA_1).digestAsHex(str);
    }
        
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File file = new File("asdf.txt");
        FileWriter fw = new FileWriter(file);
        fw.write("asdf");
        fw.close();
        File file2 = new File("ghjk.txt");
        FileWriter fw2 = new FileWriter(file2);
        fw2.write("ghjk");
        fw2.close();
        new File("./test1").mkdir();
        new File("./test1/1.txt");
        new File("./test1/2.txt");
        new File("./test1/3.txt");
        new File("./test2").mkdir();
        new File("./test2/empty").mkdir();
        new File("./test2/dir").mkdir();
        new File("./test2/dir/f.txt");
        new File("./test2/g.txt");
        new File("./test2/h.txt");
        new File("./test2/i.txt");

    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        Files.deleteIfExists(Paths.get("asdf.txt"));
        Files.deleteIfExists(Paths.get("ghjk.txt"));
        for(File f : new File("./objects").listFiles()){f.delete();}
        Files.deleteIfExists(Paths.get("./objects/"));
        for(File ff : new File("./test1").listFiles()){ff.delete();}
        Files.deleteIfExists(Paths.get("./test1"));

    }

    @Test
    @DisplayName("tests tree add")
    public void testAdd() throws Exception{
        Tree tree = new Tree();
        String str = "blob : "+sha1Hash("asdf")+" : asdf.txt";
        tree.add(str);
        tree.writeToFile();
        // tests if the tree file exists
        assertTrue(Files.exists(Paths.get("./objects/"+sha1Hash(str))));
        BufferedReader br = new BufferedReader(new FileReader("./objects/"+sha1Hash(str)));
        String str2 = br.readLine();
        br.close();
        // tests if the tree added the file
        assertEquals(str,str2);
    }

    @Test
    @DisplayName("tests tree remove")
    public void testRemove() throws IOException{
        Tree tree = new Tree();
        String str1 = "blob : "+sha1Hash("asdf")+" : asdf.txt";
        String str2 = "blob : "+sha1Hash("ghjk")+" : ghjk.txt";
        tree.add(str1);
        tree.add(str2);
        tree.remove("ghjk.txt");
        tree.writeToFile();
        // tests if the tree file exists
        assertTrue(Files.exists(Paths.get("./objects/"+sha1Hash(str1))));
        BufferedReader br = new BufferedReader(new FileReader("./objects/"+sha1Hash(str1)));
        String str3 = br.readLine();
        br.close();
        // tests if the tree removed the file
        assertEquals(str1,str3);
    }

    @Test
    @DisplayName("Tests easier directory case")
    public void testAddDirEasy() throws IOException{
        File[] files = new File("./test1").listFiles();
        String str = Tree.addDirectory("./test1");
        Tree tree = new Tree();
        for(File f : files){
            tree.add(f.getPath());
        }
        tree.writeToFile();
        assertEquals(tree.getSHA1(),str);
    }
}
