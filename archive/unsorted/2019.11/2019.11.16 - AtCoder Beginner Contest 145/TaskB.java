package contest;

import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        String s = in.readString();
        if (n % 2 == 0 && s.substring(0, n / 2).equals(s.substring(n / 2, n)))
        {
            out.println("Yes");
        }
        else{
            out.println("No");
        }
    }
}
