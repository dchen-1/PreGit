import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTester
{

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        /*
         * Utils.writeStringToFile("junit_example_file_data.txt", "test file contents");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    /*@Test
    @DisplayName("[8] Test if index and objects are created correctly")
    void testInitialize() throws Exception {

        // Run the person's code
        // TestHelper.runTestSuiteMethods("testInitialize");

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {

        try {

            // Manually create the files and folders before the 'testAddFile'
            // MyGitProject myGitClassInstance = new MyGitProject();
            // myGitClassInstance.init();

            // TestHelper.runTestSuiteMethods("testCreateBlob", file1.getName());

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        // Check blob exists in the objects folder
        File file_junit1 = new File("objects/" + file1.methodToGetSha1());
        assertTrue("Blob file to add not found", file_junit1.exists());

        // Read file contents
        String indexFileContents = MyUtilityClass.readAFileToAString("objects/" + file1.methodToGetSha1());
        assertEquals("File contents of Blob don't match file contents pre-blob creation", indexFileContents,
                file1.getContents());
    }*/

    @Test
    @DisplayName ("Test if tree is created.")
    void testTree() throws IOException
    {
        Tree tree = new Tree();
        File file = new File ("Tree");
        assertTrue(file.exists());
    }

    @Test
    @DisplayName ("Test if string is added to Tree")
    void testAdd() throws IOException
    {
        File file2 = new File ("Tree");// maybe not necessary to delete tree twice. check later
        file2.delete();
        Tree tree = new Tree();
        String line = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        tree.add(line);
        tree.add(line);
        line = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt";
        tree.add(line);
        BufferedReader reader = new BufferedReader(new FileReader("Tree"));
        String content = "";
        while(reader.ready())
        {
            content += (char)reader.read();
        }
        content = content.substring(0, content.length());
        reader.close();
        assertEquals("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt\n" + "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b", content);
        File file = new File ("Tree");
        file.delete();
    }

    @Test
    @DisplayName ("Test if string is removed from Tree")
    void testRemove() throws IOException
    {
        File file2 = new File ("Tree");// maybe not necessary to delete tree twice. check later
        file2.delete();
        Tree tree = new Tree();
        String line = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        tree.add(line);
        tree.add(line);
        line = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt";
        tree.add(line);
        tree.remove("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        BufferedReader reader = new BufferedReader(new FileReader("Tree"));
        String content = "";
        while(reader.ready())
        {
            content += (char)reader.read();
        }
        content = content.substring(0, content.length());
        reader.close();
        assertEquals("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt", content);
        File file = new File ("Tree");
        file.delete();
    }

    @Test
    @DisplayName ("Test if tree is saved to objects folder")
    void testSave() throws IOException
    {
        File file2 = new File ("Tree");
        file2.delete();
        Tree tree = new Tree();
        String line = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        tree.add(line);
        tree.save();
        BufferedReader reader = new BufferedReader(new FileReader("ee8612eaba3e603c9cb58e1d26a0b95ee3477652"));
        String content = "";
        while(reader.ready())
        {
            content += (char)reader.read();
        }
        content = content.substring(0, content.length());
        reader.close();
        Path path = Paths.get("objects");
        File file = new File ("ee8612eaba3e603c9cb58e1d26a0b95ee3477652");
        assertEquals("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b", content);
        assertTrue(file.exists());
        assertTrue(Files.exists(path));
        File file3 = new File ("Tree");
        file3.delete();
        file.delete();
        File folder = new File ("objects");
        File[] files = folder.listFiles();
        if(files != null)
        { //some JVMs return null for empty dirs
        for(File f : files)
        {
            f.delete();
        }
        }
        folder.delete();
    }
}
