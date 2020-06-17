package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerList;

import java.util.Arrays;

public class Wallet {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[k];
        in.populate(a);

        boolean[] handled = new boolean[n + 1];
        IntegerList list = new IntegerList(n);
        for (int x : a) {
            if (handled[x]) {
                continue;
            }
            handled[x] = true;
            list.add(x);
        }

        for (int i = 1; i <= n; i++) {
            if (!handled[i]) {
                list.add(i);
            }
        }

        for (int i = 0; i < n; i++) {
            out.append(list.get(i)).append(' ');
        }

        out.println();
        IntegerBIT bit = new IntegerBIT(k + 1);
        int[] prev = new int[k];
        int[] next = new int[k];
        int[] registries = new int[n + 1];
        Arrays.fill(registries, -1);
        for (int i = 0; i < k; i++) {
            prev[i] = registries[a[i]];
            registries[a[i]] = i;
        }
        Arrays.fill(registries, k);
        for (int i = k - 1; i >= 0; i--) {
            next[i] = registries[a[i]];
            registries[a[i]] = i;
        }

        int[] distinct = new int[k];
        for (int i = 0; i < k; i++) {
            int l = prev[i];
            bit.update(prev[i] + 1, 1);
            distinct[i] = i - l - bit.query(l + 1, i + 1);
        }

        for (int i = 0; i < k; i++) {
            if (next[i] >= k) {
                out.println(n - 1);
                continue;
            }
            out.println(distinct[next[i]]);
        }
    }
}

