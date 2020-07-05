package on2020_07.on2020_07_05_Codeforces___Codeforces_Round__395__Div__1_.B__Timofey_and_rectangles;



import template.io.FastInput;
import template.io.FastOutput;

public class BTimofeyAndRectangles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        out.println("YES");
        for (int i = 0; i < n; i++) {
            int l = in.readInt() & 1;
            int b = in.readInt() & 1;
            int r = in.readInt() & 1;
            int t = in.readInt() & 1;

            int c = l * 2 + b + 1;
            out.println(c);
        }
    }
}
