package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.MaximumRepresentation;

public class U34895 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        byte[] s = new byte[(int)1e7];
        int n = in.readString(s, 0);
        int index = MaximumRepresentation.solve(i -> 1000 - s[i], n);
        out.println(index);
    }
}
