import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    protected String prevTreeHash;

    public Commit(String author, String summary, String previousHash) throws Exception {
        this.author = author;
        this.summary = summary;
        this.previousHash = previousHash;
        this.date = new Date().toString();
        this.date = date.substring(0, date.length() - 18) + date.substring(date.length() - 5, date.length());
        this.treeHash = createTree();
        updateContent();
        this.hash = getHash();
        if (!previousHash.equals(""))
            updatePrevious();
        write();
    }

    public Commit(String author, String summary) throws Exception {
        this(author, summary, "");
    }

    public String getDate() {
        return date;

    }

    public void updatePrevious() throws Exception {
        String str = "";
        BufferedReader br = new BufferedReader(new FileReader("./objects/" + previousHash));
        str += br.readLine() + "\n";
        str += br.readLine() + "\n";
        str += hash + "\n";
        br.readLine();
        str += br.readLine() + "\n";
        str += br.readLine() + "\n";
        str += br.readLine() + "\n";
        br.close();
        FileUtils.writeFile("./objects/" + previousHash, str);
    }

    public String getSha(String other) throws Exception {
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
        ArrayList<String> deletes = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("Index"));
        while (br.ready()) {
            String str = br.readLine();
            if (str.contains("*deleted*")) {
                deletes.add(str.substring(str.lastIndexOf("*") + 2));
            } else if (str.contains("*edited*")) {
                deletes.add(str.substring(str.lastIndexOf("*") + 2));
                if (Files.isDirectory(Paths.get(str.substring(str.lastIndexOf("*") + 2))))
                    tree.addTree(str);
                else {
                    tree.add("blob:" + new Blob(str.substring(str.lastIndexOf("*") + 2)).getSHAString() + ":"
                        + str.substring(str.lastIndexOf("*") + 2));
                }
            } else {
                tree.add(str);
            }
        }
        br.close();
        if (!previousHash.equals("")) {
            BufferedReader br2 = new BufferedReader(new FileReader("./objects/" + previousHash));
            prevTreeHash = br2.readLine();
            br2.close();
        }
        else{prevTreeHash = "";}
        if(deletes.size()>0)delete(prevTreeHash, deletes);
        else{
            if(prevTreeHash!=""){tree.add("tree:"+prevTreeHash);}
        }
        tree.writeToFile();
        Files.deleteIfExists(Paths.get("Index"));
        return tree.getSHA1();
    }

    public void delete(String tree, ArrayList<String> deletes) throws Exception {
        this.prevTreeHash = tree;
        while (tree != "") {
            BufferedReader br = new BufferedReader(new FileReader("./objects/" + tree));
            while (br.ready()) {
                String str = br.readLine();
                if (!deletes.contains(str.substring(str.lastIndexOf(":")+1))&& str.lastIndexOf(":") > 15) {
                    this.tree.add(str);
                }
                else{
                    this.prevTreeHash = "";
                }
                if (str.contains("tree:") && str.lastIndexOf(":") <= 15) {// the hash is at least 15 characters long.
                    tree = str.substring(str.indexOf(":")+1);
                    if(this.prevTreeHash.equals(""))this.prevTreeHash = tree;
                }
                else{
                    tree = "";
                }

            }
            br.close();
        }
        if(!this.prevTreeHash.equals(""))this.tree.add("tree:"+this.prevTreeHash);
    }

}
