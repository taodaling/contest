package contest;

import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Map;

public class ANeverendingCompetitions {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        String src = in.readString();
        int indeg = 0;
        int outdeg = 0;
        for (int i = 0; i < n; i++) {
            in.skipBlank();
            String line = in.readLine().trim();
            String[] blocks = line.split("->");
            if (blocks[0].equals(src)) {
                outdeg++;
            }
            if (blocks[1].equals(src)) {
                indeg++;
            }
        }

        out.println(indeg == outdeg ? "home" : "contest");
    }
}