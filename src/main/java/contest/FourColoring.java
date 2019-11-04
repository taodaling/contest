package contest;

import java.io.PrintWriter;

import template.FastInput;

public class FourColoring {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int h = in.readInt();
        int w = in.readInt();
        int d = in.readInt();

        char[] colors = new char[] {'R', 'Y', 'G', 'B'};
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int ti = i + j;
                int tj = i - j + w;

                int val = 2 * (ti / d % 2) + tj / d % 2;
                out.append(colors[val]);
            }
            out.append('\n');
        }
    }
}
