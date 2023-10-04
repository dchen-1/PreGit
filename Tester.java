import static org.apache.commons.codec.digest.MessageDigestAlgorithms.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import Utilities.FileUtils;

public class Tester {
    protected static String c1Author = "Victor";
    protected static String c1Summary = "i did nothing";
    protected static String c2Author = "Victor again";
    protected static String c2Summary = "I DID EVERYTHING";
    public static String sha1Hash(String str){
        return new DigestUtils(SHA_1).digestAsHex(str);
    }
    public static void main(String[] args) throws Exception {
        Index i = new Index();
        i.addDirectory("Tests");
        Commit c1 = new Commit(c1Author,c1Summary);
        i = new Index();
        i.addDirectory("Tests");
        Commit c2 = new Commit(c2Author,c2Summary,c1.getHash());
        i = new Index();
        i.addDirectory("Tests");
        Commit c3 = new Commit("David","AAAAAAA",c2.getHash());
        i = new Index();
        i.addDirectory("Tests");
        BufferedReader br = new BufferedReader(new FileReader("./objects/"+c3.hash));
        String str = "";
        BufferedReader br2 = new BufferedReader(new FileReader("./objects/"+c3.treeHash));
        ArrayList<String> list = new ArrayList<String>();
        while(br2.ready()){
            list.add(br2.readLine());
        }
        list.remove(list.size()-1);
        br2.close();
        for(String s : list){str+=s+"\n";}
        str = str.substring(0, str.length()-1);
        String str2 = FileUtils.readFile("Index");
        System.out.println(str);
        System.out.println(str2);
        assertEquals(new DigestUtils(SHA_1).digestAsHex(str),new DigestUtils(SHA_1).digestAsHex(str2));
        br.close();
    }
}
