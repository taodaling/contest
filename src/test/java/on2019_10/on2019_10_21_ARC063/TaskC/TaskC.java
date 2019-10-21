package on2019_10.on2019_10_21_ARC063.TaskC;



import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        int d = 0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) != s.charAt(i - 1)) {
                d++;
            }
        }

        out.println(d);
    }
}
