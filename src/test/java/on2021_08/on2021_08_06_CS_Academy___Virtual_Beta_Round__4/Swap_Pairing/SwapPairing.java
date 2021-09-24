package on2021_08.on2021_08_06_CS_Academy___Virtual_Beta_Round__4.Swap_Pairing;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

public class SwapPairing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        IntegerArrayList list = new IntegerArrayList(a);
        list.unique();
        for (int i = 0; i < n; i++) {
            a[i] = list.binarySearch(a[i]);
        }
        IntegerArrayList[] items = new IntegerArrayList[n];
        for (int i = 0; i < n; i++) {
            items[i] = new IntegerArrayList(2);
        }
        for (int i = 0; i < n; i++) {
            items[a[i]].add(i);
        }
        int wpos = 0;
        debug.debug("items", items);
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            if (items[a[i]].isEmpty()) {
                continue;
            }
            indices[wpos++] = items[a[i]].get(0);
            indices[wpos++] = items[a[i]].get(1);
            items[a[i]].clear();
        }
        debug.debug("wpos", wpos);
        debug.debugArray("indices", indices);
        IntegerBIT bit = new IntegerBIT(n);
        long ans = 0;
        for (int i = n - 1; i >= 0; i--) {
            ans += bit.query(indices[i] + 1);
            bit.update(indices[i] + 1, 1);
        }
        out.println(ans);
    }
    Debug debug = new Debug(false);
}
