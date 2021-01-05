package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.*;

public class DLizardEraBeginning {

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] task = new int[n][3];
        for (int i = 0; i < n; i++) {
            in.populate(task[i]);
        }

        int half = (n + 1) / 2;
        work = Arrays.copyOfRange(task, half, n);
        dfs(0, 0, 0, 0, 0);
        work = Arrays.copyOfRange(task, 0, half);
        dfs2(0, 0, 0, 0, 0);
        if (sum == -inf) {
            out.println("Impossible");
            return;
        }
        long ans = (op << (n - half) * 2) | follow;
        List<String> sol = new ArrayList<>();
        int a = 0;
        int b = 0;
        int c = 0;
        for (int i = n - 1; i >= 0; i--) {
            long tail = ans & 3;
            ans >>= 2;

            a += task[i][0];
            b += task[i][1];
            c += task[i][2];
            if (tail == 0) {
                sol.add("MW");
                a -= task[i][0];
            } else if (tail == 1) {
                sol.add("LW");
                b -= task[i][1];
            } else {
                sol.add("LM");
                c -= task[i][2];
            }
        }
        assert a == b && b == c;
        Collections.reverse(sol);
        for (String s : sol) {
            out.println(s);
        }

        debug.debug("sum", sum);
    }


    Map<Key, Sol> map = new HashMap<>((int) 2e6);
    int[][] work;

    int inf = (int)1e9;
    int sum = -inf;
    long op = -1;
    long follow = -1;

    public void update(int s, int o, int f) {
        if (sum < s) {
            sum = s;
            op = o;
            follow = f;
        }
    }

    public void dfs2(int i, int seq, int a, int b, int c) {
        if (i == work.length) {
            Key key = new Key(a, b, c);
            key.inverse();
            Sol sol = map.get(key);
            if (sol != null) {
                update(a + b + c + sol.sum, seq, sol.way);
            }
            return;
        }
        dfs2(i + 1, (seq << 2) | 0, a, b + work[i][1], c + work[i][2]);
        dfs2(i + 1, (seq << 2) | 1, a + work[i][0], b, c + work[i][2]);
        dfs2(i + 1, (seq << 2) | 2, a + work[i][0], b + work[i][1], c);
    }

    public void dfs(int i, int seq, int a, int b, int c) {
        if (i == work.length) {
            Key key = new Key(a, b, c);
            Sol sol = map.get(key);
            if (sol == null) {
                sol = new Sol(-inf, -1);
                map.put(key, sol);
            }
            sol.update(a + b + c, seq);
            return;
        }
        dfs(i + 1, (seq << 2) | 0, a, b + work[i][1], c + work[i][2]);
        dfs(i + 1, (seq << 2) | 1, a + work[i][0], b, c + work[i][2]);
        dfs(i + 1, (seq << 2) | 2, a + work[i][0], b + work[i][1], c);
    }
}

class Sol {
    int sum;
    int way;

    public Sol(int sum, int way) {
        this.sum = sum;
        this.way = way;
    }

    public void update(int s, int w) {
        if (s > sum) {
            sum = s;
            way = w;
        }
    }
}

class Key {
    int a;
    int b;
    int c;

    public Key(int a, int b, int c) {
        int min = Math.min(a, b);
        min = Math.min(min, c);
        this.a = a - min;
        this.b = b - min;
        this.c = c - min;
    }

    public void inverse() {
        int max = Math.max(a, b);
        max = Math.max(max, c);
        a = max - a;
        b = max - b;
        c = max - c;
    }

    @Override
    public int hashCode() {
        return (a * 31 + b) * 31 + c;
    }

    @Override
    public boolean equals(Object obj) {
        Key other = (Key) obj;
        return other.a == a && other.b == b && other.c == c;
    }
}