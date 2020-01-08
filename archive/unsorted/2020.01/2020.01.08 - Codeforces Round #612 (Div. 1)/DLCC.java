package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.Modular;
import template.math.Power;
import template.utils.SequenceUtils;

import java.lang.reflect.Array;
import java.util.Iterator;

public class DLCC {
    Modular mod = new Modular(998244353);
    Power power = new Power(mod);
    InverseNumber inverseNumber = new InverseNumber(2000000, mod);

    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        Ball[] balls = new Ball[n];
        for (int i = 0; i < n; i++) {
            balls[i] = new Ball();
            balls[i].x = in.readInt();
            balls[i].v[1] = in.readInt();
            balls[i].p[1] = mod.mul(inverseNumber.inverse(100), in.readInt());
            balls[i].v[0] = -balls[i].v[1];
            balls[i].p[0] = mod.subtract(1, balls[i].p[1]);
        }

        SpecialList<Item>[][] ans = dac(balls, 0, n - 1);

        int expect = 0;
        for (SpecialList<Item>[] lists : ans) {
            for (SpecialList<Item> list : lists) {
                for (Item item : list) {
                    if (inf.compareTo(item.fraction) == 0) {
                        continue;
                    }
                    int cost = valueOf(item.fraction);
                    expect = mod.plus(expect, mod.mul(cost, item.prob));
                }
            }
        }

        out.println(expect);
    }

    public int valueOf(Fraction fraction) {
        return mod.mul(fraction.a, inverseNumber.inverse(fraction.b));
    }

    Fraction inf = new Fraction((int) 2e9 + 10, 1);

    public Fraction met(int dist, int v1, int v2) {
        if (v1 <= v2) {
            return inf;
        }
        return new Fraction(dist, v1 - v2);
    }

    SpecialList<Item>[][] dac(Ball[] balls, int l, int r) {
        SpecialList<Item>[][] ans = new SpecialList[2][2];
        if (l == r) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    ans[i][j] = new SpecialList<>(1, Item.class);
                }
            }
            ans[0][0].add(new Item(inf, balls[l].p[0]));
            ans[1][1].add(new Item(inf, balls[l].p[1]));
            return ans;
        }
        if (r == l + 1) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    ans[i][j] = new SpecialList<>(1, Item.class);
                    Fraction time = met(balls[r].x - balls[l].x, balls[l].v[i], balls[r].v[j]);
                    int p = mod.mul(balls[l].p[i], balls[r].p[j]);
                    ans[i][j].add(new Item(time, p));
                }
            }
//            if(totalProb(ans) != 1){
//                throw new RuntimeException();
//            }
            return ans;
        }

        int m = (l + r) >>> 1;
        SpecialList<Item>[][] left = dac(balls, l, m);
        SpecialList<Item>[][] right = dac(balls, m, r);
        int[] probOfM = new int[2];
        probOfM[0] = power.inverseByFermat(balls[m].p[0]);
        probOfM[1] = power.inverseByFermat(balls[m].p[1]);
        for (int ll = 0; ll < 2; ll++) {
            for (int rr = 0; rr < 2; rr++) {
                for(int mid = 0; mid < 2; mid++) {
//                for (int lr = 0; lr < 2; lr++) {
//                    for (int rl = 0; rl < 2; rl++) {
                    //int prob = mod.mul(balls[m].p[lr], balls[m + 1].p[rl]);
                    //Fraction time = met(balls[m + 1].x - balls[m].x, balls[m].v[lr], balls[m + 1].v[rl]);
                    ans[ll][rr] = mergeSelf(merge(left[ll][mid], right[mid][rr], probOfM[mid]), ans[ll][rr]);
//                    }
//                }
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (ans[i][j].size > 4 * (r - l + 1)) {
                    throw new RuntimeException();
                }
            }
        }

//        if(totalProb(ans) != 1){
//            throw new RuntimeException();
//        }
        return ans;
    }

    public int totalProb(SpecialList<Item>[][] ans) {
        int sum = 0;
        for (SpecialList<Item>[] lists : ans) {
            for (SpecialList<Item> list : lists) {
                for (Item item : list) {
                    sum = mod.plus(item.prob, sum);
                }
            }
        }
        return sum;
    }

    public SpecialList<Item> mergeSelf(SpecialList<Item> a, SpecialList<Item> b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        SpecialList<Item> ans = new SpecialList<>(a.size + b.size, Item.class);
        int aIndex = 0;
        int bIndex = 0;
        while (aIndex < a.size || bIndex < b.size) {
            Item item;
            if (bIndex >= b.size || (aIndex < a.size && a.data[(aIndex)].fraction.compareTo(b.data[(bIndex)].fraction) <= 0)) {
                item = a.data[(aIndex++)];
            } else {
                item = b.data[(bIndex++)];
            }
            if (item.prob == 0) {
                continue;
            }
            if (ans.size > 0 && ans.data[(ans.size - 1)].fraction.compareTo(item.fraction) == 0) {
                Item tail = ans.data[(ans.size - 1)];
                tail.prob = mod.plus(tail.prob, item.prob);
            } else {
                ans.add(item);
            }
        }
        return ans;
    }

    public SpecialList<Item> merge(SpecialList<Item> left, SpecialList<Item> right, int prob) {
        int lIndex = left.size - 1;
        int rIndex = right.size - 1;
        int lTotalProb = 0;
        int rTotalProb = 0;
//        for (; lIndex >= 0 && left.data[lIndex].fraction.compareTo(max) >= 0; lIndex--) {
//            lTotalProb = mod.plus(lTotalProb, left.data[(lIndex)].prob);
//        }
//        for (; rIndex >= 0 && right.data[(rIndex)].fraction.compareTo(max) >= 0; rIndex--) {
//            rTotalProb = mod.plus(rTotalProb, right.data[(rIndex)].prob);
//        }
//        Item newItem = new Item(max, mod.mul(lTotalProb, rTotalProb));
        SpecialList<Item> ans = new SpecialList<>(lIndex + 1 + rIndex + 1, Item.class);
//        ans.add(newItem);

        while (lIndex >= 0 || rIndex >= 0) {
            Item item;
            if (rIndex < 0 || (lIndex >= 0 && left.data[(lIndex)].fraction.compareTo(right.data[(rIndex)].fraction) >= 0)) {
                item = new Item(left.data[(lIndex--)]);
                lTotalProb = mod.plus(lTotalProb, item.prob);
                item.prob = mod.mul(item.prob, rTotalProb);
            } else {
                item = new Item(right.data[(rIndex--)]);
                rTotalProb = mod.plus(rTotalProb, item.prob);
                item.prob = mod.mul(item.prob, lTotalProb);
            }
            if (ans.size > 0 && ans.data[(ans.size - 1)].fraction.compareTo(item.fraction) == 0) {
                Item tail = ans.data[(ans.size - 1)];
                tail.prob = mod.plus(tail.prob, item.prob);
            } else {
                ans.add(item);
            }
        }

        for(Item item : ans){
            item.prob = mod.mul(item.prob, prob);
        }

        ans.reverse();
        return ans;
    }
}

class Ball {
    int x;
    int[] v = new int[2];
    int[] p = new int[2];
}

class Item {
    Fraction fraction;
    int prob;

    public Item(Fraction fraction, int prob) {
        this.fraction = fraction;
        this.prob = prob;
    }

    public Item(Item item) {
        this.fraction = item.fraction;
        this.prob = item.prob;
    }

    @Override
    public String toString() {
        return String.format("P(%s)=%d", fraction.toString(), prob);
    }
}

class SpecialList<T> implements Iterable<T> {
    T[] data;
    int size;
    Object[] empty = new Object[0];

    public SpecialList(int c, Class<T> cls) {
        if (c == 0) {
            data = (T[]) empty;
        } else {
            data = (T[]) Array.newInstance(cls, c);
        }
    }

    public void add(T x) {
        data[size++] = x;
    }

    public void reverse() {
        SequenceUtils.reverse(data, 0, size - 1);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                return data[index++];
            }
        };
    }
}

class Fraction implements Comparable<Fraction> {
    public int a, b;

    public Fraction(int a, int b) {
        if (b < 0) {
            a = -a;
            b = -b;
        }
//        long g = gcd(Math.abs(a), b);
//        if (g != 0) {
//            a /= g;
//            b /= g;
//        }
        this.a = a;
        this.b = b;
    }

    static long gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    @Override
    public int compareTo(Fraction o) {
        return Long.compare((long) this.a * o.b, (long) this.b * o.a);
    }

    @Override
    public String toString() {
        return a + "/" + b;
    }

}
