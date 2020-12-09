package on2020_11.on2020_11_29_Codeforces___Codeforces_Round__687__Div__1__based_on_Technocup_2021_Elimination_Round_2_.D__Cakes_for_Clones;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongIntervalMap;

import java.util.Arrays;

public class DCakesForClones {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        LongArrayList list = new LongArrayList(n + 1);
        list.add(0);
        Cake[] cakes = new Cake[n];
        for (int i = 0; i < n; i++) {
            cakes[i] = new Cake();
            cakes[i].t = in.ri();
            cakes[i].x = in.ri();
            list.add(cakes[i].x);
        }

        list.unique();
        for (Cake c : cakes) {
            c.compressedX = list.binarySearch(c.x);
        }

        long[] cp = list.toArray();
        int m = list.size();
        int zero = list.binarySearch(0);
        long[] pre = new long[m + 1];
        long[] next = new long[m + 1];
        //addTag(pre, zero, zero);

        Arrays.sort(cakes, (a, b) -> Integer.compare(a.t, b.t));
        int now = 0;
        int cur = zero;

        LongIntervalMap preMap = new LongIntervalMap();
        preMap.add(0, 1);
        LongIntervalMap nextMap = new LongIntervalMap();
        for (Cake c : cakes) {
            int elapse = c.t - now;
            now = c.t;
            useTag(pre);
            Arrays.fill(next, 0);
            nextMap.clear();

            boolean selfPossible = false;
            //self to self
            for (int i = 0; i < m; i++) {
                if (pre[i] == 0) {
                    continue;
                }
                selfPossible = true;
                //not add tag
                if (Math.abs(cp[cur] - c.x) <= elapse) {
                    addTag(next, i, i);
                }
            }
            //add new tag and self
            if (selfPossible) {
                for (int i = 0; i < m; i++) {
                    if (Math.abs(cp[cur] - cp[i]) + Math.abs(cp[i] - c.x) <= elapse) {
                        addTag(next, i, i);
                    }
                }
            }
            //self go to go by shadow
            if (pre[c.compressedX] > 0) {
                nextMap.add(cp[cur] - elapse, cp[cur] + elapse + 1);
            }

            for (LongIntervalMap.Interval interval : preMap) {
                long l = interval.l;
                long r = interval.r - 1;
                //shadow to self
                for (int i = 0; i < m; i++) {
                    long shortest = dist(l, r, cp[i]);
                    if (shortest + Math.abs(cp[i] - c.x) <= elapse) {
                        addTag(next, i, i);
                    }
                }
                //shadow to shadow
                {
                    long shortest = dist(l, r, c.x);
                    if (shortest <= elapse) {
                        long remain = elapse - shortest;
                        nextMap.add(c.x - remain, c.x + remain + 1);
                    }
                }
            }

            {
                long[] tmp = pre;
                pre = next;
                next = tmp;
            }
            {
                LongIntervalMap tmp = preMap;
                preMap = nextMap;
                nextMap = tmp;
            }
            cur = c.compressedX;
        }

        boolean possible = !preMap.isEmpty();
        useTag(pre);
        for (int i = 0; i < m; i++) {
            if (pre[i] > 0) {
                possible = true;
            }
        }

        out.println(possible ? "YES" : "NO");
    }

    public long dist(long l, long r, long x) {
        return x < l ? l - x : x > r ? x - r : 0;
    }

    public void addTag(long[] x, int l, int r) {
        x[l]++;
        x[r + 1]--;
    }

    public void useTag(long[] x) {
        long ps = 0;
        for (int i = 0; i < x.length; i++) {
            ps += x[i];
            x[i] = ps;
        }
    }
}

class Cake {
    int t;
    int x;
    int compressedX;
}
