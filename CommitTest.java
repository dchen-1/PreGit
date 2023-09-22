import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import Utilities.FileUtils;

public class CommitTest {
    protected static String c1Name;
    protected static String c2Name;
    protected String c1Author = "Victor";
    protected String c1Summary = "i did nothing";
    protected String c2Author = "Victor again";
    protected String c2Summary = "I DID EVERYTHING";

    @AfterAll
    static void deleteTestFiles() throws Exception {
        FileUtils.deleteFile(c1Name);
        FileUtils.deleteFile(c2Name);
    }

    @Test
    void testWrite() throws Exception {
        Commit c1 = new Commit(c1Name, c1Summary);
        c1Name = c1.hash;

        // checks if the name of its file in objects is correct
        assertEquals(FileUtils.sha1(
                c1.treeHash + "\n" + c1.previousHash + "\n" + c1.author + "\n" + c1.getDate() + "\n" + c1.summary),
                c1Name);
        // checks if the file contents are correct
        assertEquals(c1.treeHash + c1.previousHash + c1.nextHash + c1.author + c1.getDate() + c1.summary,
                FileUtils.readFile("objects/" + c1Name));
    }

    @Test
    void testSetNext() throws Exception {
        Commit c1 = new Commit(c1Name, c1Summary);
        c1Name = c1.hash;

        Commit c2 = new Commit(c2Name, c2Summary, c1Name);
        c2Name = c2.hash;

        c1.setNext(c2);

        // checks if setNext changed the nextHash variable
        assertEquals(c2Name, c1.nextHash);
        // checks if setNext updated its file in objects without changing the name
        assertEquals(c1.treeHash + c1.previousHash + c1.nextHash + c1.author + c1.getDate() + c1.summary,
                FileUtils.readFile("objects/" + c1Name));
    }

}
