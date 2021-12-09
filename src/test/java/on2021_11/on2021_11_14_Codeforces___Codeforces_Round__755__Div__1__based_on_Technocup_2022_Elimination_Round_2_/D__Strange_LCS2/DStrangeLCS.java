package on2021_11.on2021_11_14_Codeforces___Codeforces_Round__755__Div__1__based_on_Technocup_2022_Elimination_Round_2_.D__Strange_LCS2;




import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.FastUniversalHashFunction0;
import template.rand.UniversalHashFunction;
import template.string.FastLongHash;
import template.utils.CloneSupportObject;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DStrangeLCS {
    int charset = remake('Z') + 1;
    Debug debug = new Debug(false);
    LongHashMap map = new LongHashMap((int) 2e6, false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        s = new char[n][];
        next = new int[n][][];
        for (int i = 0; i < n; i++) {
            s[i] = in.rs().toCharArray();
            for (int j = 0; j < s[i].length; j++) {
                s[i][j] = (char) remake(s[i][j]);
            }
            next[i] = next(s[i]);
        }

        allList.clear();
        for (int i = 0; i < charset; i++) {
            buf.init();
            dfs(0, n, i);
        }
        allList.sort(comp);
        E[] all = allList.toArray(new E[0]);

        map.clear();
        for (int i = 0; i < all.length; i++) {
            all[i].index = i;
            all[i].score = 1;
            all[i].prev = null;

            map.put(all[i].hash(), i);
        }


        for (E e : all) {
            for (int j = 0; j < charset; j++) {
                buf.init();
                boolean ok = true;
                for (int k = 0; k < n && ok; k++) {
                    int to = next[k][e.get(k) + 1][j];
                    ok = to < s[k].length;
                    buf.set(k, to);
                }
                if (!ok) {
                    continue;
                }
                E obj = all[(int) map.get(buf.hash())];
                if (obj.score < e.score + 1) {
                    obj.score = e.score + 1;
                    obj.prev = e;
                }
            }
        }

        if (all.length == 0) {
            out.println(0);
            out.println();
            return;
        }
        E best = null;
        for (E e : all) {
            if (best == null || e.score > best.score) {
                best = e;
            }
        }
        StringBuilder sb = new StringBuilder(charset * 2);
        while (best != null) {
            int v = s[0][best.get(0)];
            sb.append(goback(v));
            best = best.prev;
        }
        sb = sb.reverse();
        out.println(sb.length());
        out.println(sb);

        debug.debug("secondLevel", FastUniversalHashFunction0.numberOfInstance);
        debug.debug("numberOfHighestLevel", UniversalHashFunction.numberOfInstance);
    }

    Comparator<E> comp = Comparator.<E>comparingLong(x -> x.a).thenComparing(x -> x.b);
    E buf = new E();
    List<E> allList = new ArrayList<>((int) 1e5);
    int[][][] next;
    char[][] s;

    public void dfs(int cur, int n, int c) {
        if (cur == n) {
            allList.add(buf.clone());
            return;
        }
        for (int iter = next[cur][0][c]; iter < s[cur].length; iter = next[cur][iter + 1][c]) {
            buf.set(cur, iter);
            dfs(cur + 1, n, c);
        }
    }

    public int[][] next(char[] s) {
        int n = s.length;
        int[][] next = new int[n + 1][];
        int[] prev = new int[charset];
        Arrays.fill(prev, n);
        next[n] = prev.clone();
        for (int i = n - 1; i >= 0; i--) {
            prev[s[i]] = i;
            next[i] = prev.clone();
        }
        return next;
    }

    public int remake(int x) {
        if ('a' <= x && x <= 'z') {
            return x - 'a';
        }
        return x - 'A' + 'z' - 'a' + 1;
    }

    public char goback(int x) {
        if (x < 'z' - 'a' + 1) {
            return (char) (x + 'a');
        } else {
            return (char) (x + 'A' - 'z' + 'a' - 1);
        }
    }
}

class E extends CloneSupportObject<E> {
    long a;
    long b;
    int index;
    int score;

    void init() {
        a = b = 0;
    }

    void copy(E e) {
        this.a = e.a;
        this.b = e.b;
    }

    private int get(long x, int i) {
        return (int) ((x >>> (i << 3)) & 255);
    }

    private long set(long x, int i, long v) {
        i <<= 3;
        v &= 255;
        return (x & ~(255L << i)) | (v << i);
    }

    public int get(int i) {
        if (i < 6) {
            return get(a, i);
        }
        i -= 6;
        return get(b, i);
    }

    public void set(int i, int v) {
        if (i < 6) {
            a = set(a, i, v);
            return;
        }
        i -= 6;
        b = set(b, i, v);
    }

    E prev;

    static FastLongHash fh = new FastLongHash();

    long hash() {
        return fh.hash(a, b);
    }
}