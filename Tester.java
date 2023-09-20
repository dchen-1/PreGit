import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Tester {
    public static void main(String[] args) throws Exception {
        // Index i = new Index();
        // BufferedWriter bw = new BufferedWriter(new FileWriter("txt.txt"));
        // bw.write("derpderpderp");
        // bw.close();
        // BufferedWriter bw2 = new BufferedWriter(new FileWriter("xtx.txt"));
        // bw2.write("xnopyt");
        // bw2.close();
        // i.writePair("txt.txt");
        // i.writePair("xtx.txt");
        // i.removePair("txt.txt");

        Commit c1 = new Commit("Author", "Summary");

        // System.out.println(c.getDate());
        // c1.write();

        Commit c2 = new Commit("Best Author", "Best Summary", c1.hash);

        c1.setNext(c2);
        c1.write();
        c2.write();
    }
}
