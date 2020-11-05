package on2020_11.on2020_11_05_Codeforces___Codeforces_Round__339__Div__1_.B__Skills;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.Randomized;

import java.util.Arrays;

public class BSkills {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long A = in.readLong();
        long x = in.readLong();
        long y = in.readLong();
        long m = in.readLong();

        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].a = in.readInt();
        }
        Item[] snapshot = items.clone();
        Arrays.sort(items, (a, b) -> Long.compare(a.a, b.a));

        LongPreSum lps = new LongPreSum(i -> items[i].a, n);
        if (A * n - lps.prefix(n) <= m) {
            out.println(n * x + A * y);
            for(int i = 0; i < n; i++){
                out.append(A).append(' ');
            }
            return;
        }

        int r = n - 1;
        long[] summary = new long[3];
        long ans = -1;
        for (int i = n; i >= 0; i--) {
            long cost = A * (n - i) - lps.post(i);
            long remain = m - cost;
            while (r >= 0 && items[r].a * (r + 1) - lps.prefix(r) > remain || r >= i) {
                r--;
            }
            if (r < 0) {
                break;
            }
            long local = (items[r].a + (remain - (items[r].a * (r + 1) - lps.prefix(r))) / (r + 1)) * y + (n - i) * x;
            if (local > ans) {
                ans = local;
                summary[0] = r;
                summary[1] = i;
                summary[2] = (items[r].a + (remain - (items[r].a * (r + 1) - lps.prefix(r))) / (r + 1));
            }
        }
        out.println(ans);
        for (int i = 0; i < n; i++) {
            if (i <= summary[0]) {
                items[i].a = Math.max(items[i].a, summary[2]);
            } else if (i >= summary[1]) {
                items[i].a = A;
            } else {
            }
        }

        for(Item s : snapshot){
            out.append(s.a).append(' ');
        }
    }
}

class Item {
    long a;
}