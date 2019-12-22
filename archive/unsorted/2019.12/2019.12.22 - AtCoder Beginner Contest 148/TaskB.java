package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = in.readString().toCharArray();
        char[] t = in.readString().toCharArray();
        for(int i = 0; i < n; i++){
            out.append(s[i]).append(t[i]);
        }
    }
}
