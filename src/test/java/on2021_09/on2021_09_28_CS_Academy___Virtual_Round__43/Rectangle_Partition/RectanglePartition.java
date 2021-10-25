package on2021_09.on2021_09_28_CS_Academy___Virtual_Round__43.Rectangle_Partition;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashMap;

public class RectanglePartition {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        int w = in.ri();
        int n = in.ri();
        int m = in.ri();
        IntegerArrayList xs = new IntegerArrayList(n + 2);
        IntegerArrayList ys = new IntegerArrayList(m + 2);
        xs.add(0);
        xs.add(h);
        ys.add(0);
        ys.add(w);
        for(int i = 0; i < n; i++){
            xs.add(in.ri());
        }
        for(int i = 0; i < m; i++){
            ys.add(in.ri());
        }
        xs.sort();
        ys.sort();
        int[] cnt = new int[(int)2e5];
        for(int i = 1; i < xs.size(); i++){
            cnt[xs.get(i) - xs.get(i - 1)]++;
        }
        long ans = 0;
        for(int i = 1; i < ys.size(); i++){
            ans += cnt[ys.get(i) - ys.get(i - 1)];
        }
        out.println(ans);
    }
}
