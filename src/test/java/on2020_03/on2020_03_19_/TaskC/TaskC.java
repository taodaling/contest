package on2020_03.on2020_03_19_.TaskC;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        IntegerList list = new IntegerList(k);
        int threshold = n - k + 1;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            if (x >= threshold) {
                list.add(i);
                sum += x;
            }
        }

        Modular mod = new Modular(998244353);
        int way = 1;
        for (int i = 0; i < k - 1; i++) {
            int range = list.get(i + 1) - list.get(i);
            way = mod.mul(way, range);
        }

        out.append(sum).append(' ').append(way).println();
    }
}
