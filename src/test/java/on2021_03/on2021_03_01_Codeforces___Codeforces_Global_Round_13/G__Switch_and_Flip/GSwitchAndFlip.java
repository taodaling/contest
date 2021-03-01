package on2021_03.on2021_03_01_Codeforces___Codeforces_Global_Round_13.G__Switch_and_Flip;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Permutation;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.CollectionUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GSwitchAndFlip {
    int[] a;
    int[] inv;
    List<int[]> ops = new ArrayList<>();

    public void swap(int i, int j) {
        i = inv[i];
        j = inv[j];
        int tmp = a[i];
        a[i] = -a[j];
        a[j] = -tmp;
        inv[Math.abs(a[i])] = i;
        inv[Math.abs(a[j])] = j;
        ops.add(new int[]{i, j});
    }

    boolean check(IntegerArrayList list) {
        for (int x : list.toArray()) {
            if (a[x] != x) {
                return false;
            }
        }
        return true;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        Deque<int[]> sol = new ArrayDeque<>();

        int n = in.ri();
        a = new int[n + 1];
        inv = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.ri();
            inv[a[i]] = i;
        }
        Permutation permutation = new Permutation(a);
        List<IntegerArrayList> circles = permutation.extractCircles(2);
        while (circles.size() >= 2) {
            IntegerArrayList a = CollectionUtils.pop(circles);
            IntegerArrayList b = CollectionUtils.pop(circles);
            swap(a.first(), b.first());
            for (int i = 1; i < a.size(); i++) {
                swap(a.get(i - 1), a.get(i));
            }
            for (int i = 1; i < b.size(); i++) {
                swap(b.get(i - 1), b.get(i));
            }
            swap(a.tail(), b.tail());
            assert check(a);
            assert check(b);
        }
        if (circles.size() == 1) {
            IntegerArrayList only = CollectionUtils.pop(circles);
            if (only.size() == 2) {
                int proper = -1;
                for (int i = 1; i <= n; i++) {
                    if (a[i] == i) {
                        proper = i;
                        break;
                    }
                }
                assert proper != -1;
                swap(proper, only.first());
                swap(only.first(), only.tail());
                swap(only.tail(), proper);
            } else {
                swap(only.get(0), only.get(1));
                for (int i = 2; i + 1 < only.size(); i++) {
                    swap(only.get(i - 1), only.get(i));
                }

                //cool
                int a2 = only.get(0);
                int a3 = only.get(only.size() - 2);
                int a1 = only.get(only.size() - 1);
                swap(a1, a2);
                swap(a2, a3);
                swap(a1, a2);
            }
            assert check(only);
        }
        for (int i = 1; i <= n; i++) {
            assert a[i] == i;
        }
        out.println(ops.size());
        for (int[] x : ops) {
            out.append(x[0]).append(' ').append(x[1]).println();
        }
    }
}
