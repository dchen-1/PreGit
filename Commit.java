import java.util.Date;

import Utilities.FileUtils;

public class Commit {
    protected String summary;
    protected String date;
    protected String author;
    protected String hash;
    protected String previousHash;
    protected String nextHash;
    protected Tree tree;
    protected String treeHash;
    protected String content;

    public Commit(String author, String summary, String previousHash) throws Exception {
        this.author = author;
        this.summary = summary;
        this.previousHash = previousHash;
        this.date = new Date().toString();
        this.date = date.substring(0, date.length() - 18);
        this.tree = new Tree();
        this.treeHash = tree.getSHA1();
        this.content = updateContent();
        this.hash = getHash();
    }

    public Commit(String author, String summary) throws Exception {
        this(author, summary, "");
    }

    public String getDate() {
        return date;

    }

    private String updateContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(treeHash + "\n");
        sb.append(previousHash + "\n");
        sb.append(nextHash + "\n");
        sb.append(author + "\n");
        sb.append(date + "\n");
        sb.append(summary);
        return sb.toString();
    }

    private String getHash() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(treeHash + "\n");
        sb.append(previousHash + "\n");
        sb.append(author + "\n");
        sb.append(date + "\n");
        sb.append(summary);
        return FileUtils.sha1(sb.toString());
    }

}
