package on2019_11.on2019_11_24_AtCoder_Beginner_Contest_146.B___ROT_N;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        while (in.hasMore()) {
            int c = in.readChar() - 'A';
            c = (c + n) % ('Z' - 'A' + 1) + 'A';
            out.append((char) c);
        }
    }
}
