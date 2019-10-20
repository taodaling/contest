package on2019_10.on2019_10_19_AGC006A.TaskA;



import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        String s = in.readString();
        String t = in.readString();

        int same = n;
        while (same > 0) {
            if (s.substring(n - same, n).equals(t.substring(0, same))) {
                break;
            }
            same--;
        }

        out.println(n + n - same);
    }
}
