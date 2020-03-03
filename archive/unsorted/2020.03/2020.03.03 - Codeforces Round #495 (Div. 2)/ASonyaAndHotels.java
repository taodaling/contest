package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.LongList;

public class ASonyaAndHotels {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d = in.readInt();
        LongList list = new LongList();
        long[] x = new long[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.readInt();
            list.add(x[i] - d);
            list.add(x[i] + d);
        }
        list.unique();
        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            long y = list.get(i);
            long dist = d;
            for (int j = 0; j < n; j++) {
                dist = Math.min(dist, Math.abs(x[j] - y));
            }
            if (dist >= d) {
                cnt++;
            }
        }
        out.println(cnt);
    }
}
