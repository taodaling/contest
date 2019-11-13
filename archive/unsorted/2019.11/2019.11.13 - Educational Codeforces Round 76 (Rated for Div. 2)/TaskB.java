package contest;

import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int y = in.readInt();

        if(x == 1){
            out.println(y <= 1 ? "YES" : "NO");
            return;
        }

        if(x <= 3){
            out.println(y <= 3 ? "YES" : "NO");
            return;
        }

        out.println("YES");
    }
}
