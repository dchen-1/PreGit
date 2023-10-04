import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import Utilities.FileUtils;

public class Commit {
    protected String summary;
    protected String date;
    protected String author;
    protected String hash;
    protected String previousHash;
    protected String nextHash = "";
    protected Tree tree;
    protected String treeHash;
    protected String content;

    public Commit(String author, String summary, String previousHash) throws Exception {
        this.author = author;
        this.summary = summary;
        this.previousHash = previousHash;
        this.date = new Date().toString();
        this.date = date.substring(0, date.length() - 18) + date.substring(date.length() - 5, date.length());
        this.treeHash = createTree();
        updateContent();
        this.hash = getHash();
        write();
    }

    public Commit(String author, String summary) throws Exception {
        this(author, summary, "");
    }

    public String getDate() {
        return date;

    }

    public String getSha(String other) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(other));
        String str = br.readLine();
        br.close();
        return str;
    }

    public void write() throws Exception {
        if (!FileUtils.fileExists("objects")) {
            FileUtils.createDirectory("objects");
        }

        FileUtils.createFile("objects/" + hash);
        FileUtils.writeFile("objects/" + hash, content);
    }

    public void setNext(Commit other) throws Exception {
        this.nextHash = other.hash;
        updateContent();
        write();
    }

    private void updateContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(treeHash + "\n");
        sb.append(previousHash + "\n");
        sb.append(nextHash + "\n");
        sb.append(author + "\n");
        sb.append(date + "\n");
        sb.append(summary);
        content = sb.toString();
    }

    public String getHash() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(treeHash + "\n");
        sb.append(previousHash + "\n");
        sb.append(author + "\n");
        sb.append(date + "\n");
        sb.append(summary);
        return FileUtils.sha1(sb.toString());
    }

    private String createTree() throws Exception {
        this.tree = new Tree();
        BufferedReader br = new BufferedReader(new FileReader("Index"));
        while(br.ready()){
            tree.add(br.readLine());
        }
        br.close();
        if(previousHash!=""){
        BufferedReader br2 = new BufferedReader(new FileReader("./objects/"+previousHash));
        tree.add("tree:"+br2.readLine());
        br2.close();
        }
        tree.writeToFile();
        Files.deleteIfExists(Paths.get("Index"));
        return tree.getSHA1();
    }

}
