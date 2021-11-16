package on2021_11.on2021_11_14_Codeforces___Codeforces_Round__755__Div__1__based_on_Technocup_2022_Elimination_Round_2_.D__Strange_LCS;



import template.io.FastInput;
import template.io.FastOutput;
import template.string.FastHash;
import template.utils.CloneSupportObject;

import java.util.*;

public class DStrangeLCS {
    int charset = remake('Z') + 1;

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

        Map<E, E> map = new HashMap<>(all.length, 0.2f);
        for (int i = 0; i < all.length; i++) {
            all[i].index = i;
            all[i].score = 1;
            all[i].prev = null;
            map.put(all[i], all[i]);
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
                if(!ok){
                    continue;
                }
                E obj = map.get(buf);
                if (obj == null) {
                    continue;
                }
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
        if (i < 8) {
            return get(a, i);
        }
        i -= 8;
        return get(b, i);
    }

    public void set(int i, int v) {
        if (i < 8) {
            a = set(a, i, v);
            return;
        }
        i -= 8;
        b = set(b, i, v);
    }

    E prev;

    @Override
    public int hashCode() {
        return Long.hashCode(a) * 31 + Long.hashCode(b);
    }

    @Override
    public boolean equals(Object o) {
        E e = (E) o;
        return a == e.a && b == e.b;
    }

    @Override
    public String toString() {
        return "E{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}