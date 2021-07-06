package on2021_06.on2021_06_26_AtCoder___AtCoder_Beginner_Contest_206_Sponsored_by_Panasonic_.F___Interval_Game_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.utils.SequenceUtils;

public class FIntervalGame2 {
    int[][] sg;
    int[][] intervals;
    IntegerVersionArray iva = new IntegerVersionArray(1 << 8);

    public int sg(int l, int r) {
        if (l > r) {
            return 0;
        }
        if (sg[l][r] == -1) {
            for (int[] i : intervals) {
                if (i[0] >= l && i[1] <= r) {
                    sg(l, i[0] - 1);
                    sg(i[1] + 1, r);
                }
            }
            iva.clear();
            for (int[] i : intervals) {
                if (i[0] >= l && i[1] <= r) {
                    iva.set(sg(l, i[0] - 1) ^ sg(i[1] + 1, r), 1);
                }
            }
            int ans = 0;
            while (iva.get(ans) == 1) {
                ans++;
            }
            sg[l][r] = ans;
        }
        return sg[l][r];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        intervals = new int[n][2];
        sg = new int[100][100];
        SequenceUtils.deepFill(sg, -1);
        for (int i = 0; i < n; i++) {
            intervals[i][0] = in.ri();
            intervals[i][1] = in.ri() - 1;
        }
        int sg = sg(1, 99);
        out.println(sg == 0 ? "Bob" : "Alice");
    }
}
