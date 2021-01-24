package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.MaximumRepresentation;

public class P1709USACO55HiddenPassword {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        int read = 0;
        while (read < n) {
            read += in.rs(s, read);
        }
        int index = MaximumRepresentation.minimumRepresentation(i -> s[i], n);
        out.println(index);
    }
}
