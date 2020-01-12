package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class AFallingAsleep {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        String[] name = new String[n];
        int[] time = new int[n];
        for (int i = 0; i < n; i++) {
            name[i] = in.readString();
            time[i] = in.readInt();
        }
        String x = in.readString();
        int index = SequenceUtils.indexOf(name, 0, n - 1, x);
        int sum = 0;
        for(int i = index + 1; i < n; i++){
            sum += time[i];
        }
        out.println(sum);
    }
}
