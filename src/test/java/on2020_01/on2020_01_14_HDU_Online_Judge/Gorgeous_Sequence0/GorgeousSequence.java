package on2020_01.on2020_01_14_HDU_Online_Judge.Gorgeous_Sequence0;





import template.datastructure.SegmentBeat;
import template.io.FastInput;
import template.io.FastOutput;

public class GorgeousSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        SegmentBeat sb = new SegmentBeat(1, n, i -> a[i]);
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            int x = in.readInt();
            int y = in.readInt();
            if (t == 0) {
                int min = in.readInt();
                sb.updateMin(x, y, 1, n, min);
            } else if (t == 1) {
                long max = sb.queryMax(x, y, 1, n);
                out.append(max).append(System.lineSeparator());
            } else {
                long sum = sb.querySum(x, y, 1, n);
                out.append(sum).append(System.lineSeparator());
            }
        }
    }
}

