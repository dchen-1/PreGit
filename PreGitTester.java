import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PreGitTester
{
    /*Creating a new Blob
Test/Assert that the blob has been created
Verify file contents are the same between original and Blob'd file
Calling init, add, remove
Delete object and index folder, Create and fill a few blank files
Call init + verify index and objects folder are created
Call add + verify index is updated and objects are created*/
    @Test
    @DisplayName ("Test if blob is created.")
    void testBlob() throws Exception
    {
        String str = "";
        File file = new File ("Test");
        file.createNewFile();
        PrintWriter writer = new PrintWriter (file);
        writer.write("Test");
        writer.close();
        BufferedReader br = new BufferedReader (new FileReader ("Test"));
        while (br.ready())
        {
            str += (char) br.read();
        }
        br.close();
        Path path = Paths.get("objects");
        String sha = "640ab2bae07bedc4c163f679a746f7ab7fb5d1fa";
        String contents = "";
        File shaFile = new File ("objects/" + sha);
        Blob blob = new Blob("Test");
        blob.writeFile();
        assertTrue(shaFile.exists());
        assertTrue(Files.exists(path));
        BufferedReader reader = new BufferedReader (new FileReader ("objects/" + sha));
        while (reader.ready())
        {
            contents += (char) reader.read();
        }
        File folder = new File ("objects");
        File[] files = folder.listFiles();
        if(files != null)
        { //some JVMs return null for empty dirs
        for(File f : files)
        {
            f.delete();
        }
        }
        shaFile.delete();
        file.delete();
        folder.delete();
        reader.close();
        assertEquals("Test", contents);
    }

    @Test
    @DisplayName ("Test if index and objects folder is created.")
    void testIndex() throws Exception
    {
        File file = new File ("index");
        Index index = new Index();
        Path path = Paths.get("objects");
        assertTrue(Files.exists(path));
        assertTrue(file.exists());
        File folder = new File ("objects");
        File[] files = folder.listFiles();
        for(File f : files)
        {
            f.delete();
        }
        file.delete();
        folder.delete();
    }


    @Test
    @DisplayName ("Test if index is updated and objects are created.")
    void testWritePair() throws Exception
    {
        File file = new File ("Test");
        File file2 = new File ("index");
        file.createNewFile();
        PrintWriter writer = new PrintWriter (file);
        writer.write("Test");
        writer.close();
        Path path = Paths.get("objects");
        String sha = "640ab2bae07bedc4c163f679a746f7ab7fb5d1fa";
        File shaFile = new File ("objects/" + sha);
        Index index = new Index();
        int objects = 0;
        String contents = "";
        index.writePair("Test");
        BufferedReader reader = new BufferedReader (new FileReader ("index"));
        while (reader.ready())
        {
            contents += (char)reader.read();
        }
        reader.close();
        assertTrue(shaFile.exists());
        assertEquals("Test:640ab2bae07bedc4c163f679a746f7ab7fb5d1fa", contents);
        File folder = new File ("objects");
        File[] files = folder.listFiles();
        for(File f : files)
        {
            objects++;
            f.delete();
        }
        assertTrue(objects > 0);
        shaFile.delete();
        file.delete();
        file2.delete();
        folder.delete();
    }

    @Test
    @DisplayName ("Test if index is updated and objects are created.")
    void testRemovePair() throws Exception
    {
        File file = new File ("Test");
        File file2 = new File ("index");
        file.createNewFile();
        PrintWriter writer = new PrintWriter (file);
        writer.write("Test");
        writer.close();
        Path path = Paths.get("objects");
        String sha = "640ab2bae07bedc4c163f679a746f7ab7fb5d1fa";
        File shaFile = new File ("objects/" + sha);
        Index index = new Index();
        PrintWriter indexWriter = new PrintWriter (file2);
        indexWriter.write("Test:640ab2bae07bedc4c163f679a746f7ab7fb5d1fa");
        indexWriter.close();
        int objects = 0;
        index.removePair("Test");
        BufferedReader reader = new BufferedReader (new FileReader ("index"));
        //assertTrue(!shaFile.exists());
        assertTrue(reader.readLine() == null);
        reader.close();
        File folder = new File ("objects");
        File[] files = folder.listFiles();
        for(File f : files)
        {
            objects++;
            f.delete();
        }
        assertTrue(objects == 0);
        shaFile.delete();
        file.delete();
        file2.delete();
        folder.delete();
    }
}