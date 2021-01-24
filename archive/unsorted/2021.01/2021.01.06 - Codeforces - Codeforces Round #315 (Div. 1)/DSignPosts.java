package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.RandomWrapper;
import template.utils.Pair;

import java.util.*;

public class DSignPosts {
    int mod = (int) 1e9 + 7;
    int x1 = 31;
    int x2 = 13;

    long hash(long a, long b, long c, long d) {
        if (b < 0) {
            a = -a;
            b = -b;
        }
        if (d < 0) {
            c = -c;
            d = -d;
        }
        long g1 = GCDs.gcd(Math.abs(a), Math.abs(b));
        long g2 = GCDs.gcd(Math.abs(c), Math.abs(d));
        if (g1 != 0) {
            a /= g1;
            b /= g1;
        }
        if (g2 != 0) {
            c /= g2;
            d /= g2;
        }


        long a1 = DigitUtils.highBit(a);
        long a2 = DigitUtils.lowBit(a);
        long b1 = DigitUtils.highBit(b);
        long b2 = DigitUtils.lowBit(b);
        long c1 = DigitUtils.highBit(c);
        long c2 = DigitUtils.lowBit(c);
        long d1 = DigitUtils.highBit(d);
        long d2 = DigitUtils.lowBit(d);
        long h1 = (((((((a1 * x1 + a2) * x1 + b1) * x1 + b2) * x1 + c1) % mod * x1 + c2) * x1 + d1) * x1 + d2) % mod;
        long h2 = (((((((a1 * x2 + a2) * x2 + b1) * x2 + b2) * x2 + c1) % mod * x2 + c2) * x2 + d1) * x2 + d2) % mod;
        return DigitUtils.asLong((int) h1, (int) h2);
    }

    long solve(Line a, Line b) {
        long A = a.a;
        long B = a.b;
        long C = a.c;
        long T = b.a;
        long K = b.b;
        long D = b.c;
        return hash(B * D - K * C,
                B * T - K * A,
                A * D - T * C,
                A * K - B * T);
    }

    LongHashMap[] map = new LongHashMap[6];
    List<Line>[] lists = new List[6];
    long[][] hashes = new long[6][];
    long termTime;

    {
        for (int i = 0; i < map.length; i++) {
            map[i] = new LongHashMap((int) 1e5, false);
            lists[i] = new ArrayList((int) 1e5);
            hashes[i] = new long[(int) 1e5];
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        Line[] lines = new Line[n];
        for (int i = 0; i < n; i++) {
            lines[i] = new Line(i, in.ri(), in.ri(), in.ri());
        }
        lists[k].addAll(Arrays.asList(lines));

        termTime = System.currentTimeMillis() + 1500;
        if (!search(k)) {
            out.println("NO");
            return;
        }

        out.println("YES");
        out.println(dq.size());
        for (Pair<Line, Line> p : dq) {
            int v = p.a.id + 1;
            int u = p.b == null ? -1 : p.b.id + 1;
            out.append(v).append(' ').append(u).println();
        }
    }


    public boolean intersect(Line a, Line b) {
        return a.fa != b.fa || a.fb != b.fb;
    }

    List<Pair<Line, Line>> dq = new ArrayList<>();

    RandomWrapper rw = new RandomWrapper();

    public boolean search(int index) {
        int size = lists[index].size();
        if (size == 0) {
            return true;
        }
        if (index == 0) {
            return false;
        }
        int req = DigitUtils.ceilDiv(size, index);
        for (int chance = 0; chance < 3 * index && System.currentTimeMillis() < termTime; chance++) {
            Line peek = lists[index].get(rw.nextInt(size));
            map[index].clear();
            for (int j = 0; j < size; j++) {
                Line line = lists[index].get(j);
                if (line == peek) {
                    continue;
                }
                if (intersect(line, peek)) {
                    hashes[index][j] = solve(line, peek);
                    map[index].put(hashes[index][j], map[index].getOrDefault(hashes[index][j], 1) + 1);
                }
            }
            if (!map[index].containKey(0)) {
                map[index].put(0, 1);
            }
            for (LongEntryIterator iterator = map[index].iterator(); iterator.hasNext(); ) {
                iterator.next();
                if (iterator.getEntryValue() < req) {
                    continue;
                }
                long hash = iterator.getEntryKey();
                lists[index - 1].clear();
                Line any = null;
                for (int j = 0; j < size; j++) {
                    Line line = lists[index].get(j);
                    if (line == peek) {
                        continue;
                    }
                    if (intersect(line, peek) && hashes[index][j] == hash) {
                        any = line;
                        continue;
                    }
                    lists[index - 1].add(line);
                }
                if (!search(index - 1)) {
                    continue;
                }
                dq.add(new Pair<>(peek, any));
                return true;
            }
        }

        return false;
    }
}

class Line {
    int id;
    long a;
    long b;
    long c;
    long fa;
    long fb;

    public Line(int id, long a, long b, long c) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;

        fa = a;
        fb = b;
        if (fb < 0) {
            fa = -a;
            fb = -b;
        }
        if (fb == 0) {
            fa = Math.abs(fa);
        }
        long g = GCDs.gcd(fa, fb);
        if (g != 0) {
            fa /= g;
            fb /= g;
        }
    }
}