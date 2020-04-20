package contest;

import template.math.Modular;
import template.primitve.generated.datastructure.IntegerHashSet;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShortestMissingSubsequences {
    Modular mod = new Modular(1e9 + 7);

    public int[] count(int G, int N, int[] Aprefix) {
        int[] A = Arrays.copyOf(Aprefix, N);
        long state = Aprefix[Aprefix.length - 1];
        for (int i = Aprefix.length; i < N; i++) {
            state = (state * 1103515245 + 12345) % (1L << 31);
            A[i] = (int) (state % G);
        }

        if (G > N) {
            IntegerHashSet set = new IntegerHashSet(N, false);
            for (int a : A) {
                set.add(a);
            }
            return new int[]{1, G - set.size()};
        }

        Machine machine = new Machine(G);
        List<int[]> intervals = new ArrayList<>(N);
        machine.clear();
        int l = 0;
        boolean[] leftist = new boolean[N];
        for (int i = 0; i < N; i++) {
            if (machine.set(A[i])) {
                leftist[i] = true;
            }

            if (machine.one == G) {
                intervals.add(new int[]{l, i});
                l = i + 1;
                machine.clear();
            }
        }

        int len = intervals.size() + 1;
        int[] last = new int[G];
        int[] next = new int[G];
        for (int i = 0; i < G; i++) {
            if (!machine.exists[i]) {
                last[i] = 1;
            }
        }

        for (int i = intervals.size() - 1; i >= 0; i--) {
            int[] lr = intervals.get(i);
            int sum = 0;
            for (int v : last) {
                sum = mod.plus(sum, v);
            }
            for (int j = lr[1]; j >= lr[0]; j--) {
                if (leftist[j]) {
                    next[A[j]] = sum;
                }
                sum = mod.subtract(sum, last[A[j]]);
                last[A[j]] = 0;
            }

            int[] tmp = last;
            last = next;
            next = tmp;
        }

        int ans = 0;
        for (int v : last) {
            ans = mod.plus(ans, v);
        }
        return new int[]{len, ans};
    }
}

class Machine {
    boolean[] exists;
    int one;

    public Machine(int n) {
        exists = new boolean[n];
    }

    public void clear() {
        Arrays.fill(exists, false);
        one = 0;
    }

    public boolean set(int i) {
        if (exists[i]) {
            return false;
        }
        exists[i] = true;
        one++;
        return true;
    }
}