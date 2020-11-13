package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.MaximumRepresentation;

public class MinimalRotation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);
        int start = MaximumRepresentation.solve(i -> -s[i], n);
        for (int i = 0; i < n; i++) {
            int pos = start + i;
            if(pos >= n){
                pos -= n;
            }
            out.append(s[pos]);
        }
    }
}
