package on2019_12.on2019_12_23_Codeforces_Round__542__Alex_Lopashev_Thanks_Round___Div__1_.D__Isolation;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitBase;
import template.math.DigitUtils;
import template.math.Modular;
import template.primitve.generated.IntegerHashMap;
import template.primitve.generated.LongHashMap;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

import java.util.Comparator;
import java.util.Random;

public class DIsolation {
    static Modular mod = new Modular(998244353);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        int[] registries = new int[n + 1];
        int[] last = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            last[i] = registries[a[i]];
            registries[a[i]] = i;
        }

        int[] dp = new int[n + 1];
        BlockManager bm = new BlockManager(n, k);
        bm.append(0, new Element(0, 1));

        for (int i = 1; i <= n; i++) {
            if (last[i] == 0) {
                bm.add(0, i - 1, 1);
            } else {
                int l = last[last[i]] + 1;
                int m = last[i];
                bm.add(l - 1, m - 1, -1);
                bm.add(m, i - 1, 1);
            }
            dp[i] = mod.valueOf(bm.sumOf());
            bm.append(i, new Element(0, dp[i]));
        }

        out.println(dp[n]);
    }
}

class Element {
    int k;
    int val;

    public Element(int k, int val) {
        this.k = k;
        this.val = val;
    }

    static Comparator<Element> sortByK = (a, b) -> a.k - b.k;
}

class BlockManager {
    Block[] blocks;
    int bSize;

    public BlockManager(int n, int k) {
        bSize = (int) Math.ceil(Math.sqrt(n + 1));
        int m = DigitUtils.ceilDiv(n + 1, bSize);
        blocks = new Block[m];
        for (int i = 0; i < m; i++) {
            blocks[i] = new Block(bSize, k);
        }
    }

    public void append(int i, Element e) {
        blocks[i / bSize].append(e);
    }

    public void add(int ll, int rr, int x) {
        for (int i = 0; i < blocks.length; i++) {
            int l = i * bSize;
            int r = l + bSize - 1;
            if (r < ll || l > rr) {
                continue;
            }
            if (ll <= l && r <= rr) {
                blocks[i].add(x);
            } else {
                blocks[i].add(Math.max(ll, l) - l, Math.min(rr, r) - l, x);
            }
        }
    }

    public long sumOf() {
        long ans = 0;
        for (Block b : blocks) {
            ans += b.sum;
        }
        return ans;
    }
}

class Block {
    Element[] elements;
    LongHashMap map;

    int size;
    int add;
    long sum;
    int k;

    public Block(int n, int k) {
        elements = new Element[n];
        this.k = k;
        map = new LongHashMap(n, true);
    }

    public void add(int x) {
        if (x > 0) {
            sum -= map.getOrDefault(k - add, 0);
            add++;
        } else {
            add--;
            sum += map.getOrDefault(k - add, 0);
        }
    }

    public void add(int l, int r, int x) {
        pushDown();
        for (int i = l; i <= r; i++) {
            map.put(elements[i].k, map.getOrDefault(elements[i].k, 0) - elements[i].val);
            if (x > 0) {
                if (elements[i].k == k) {
                    sum -= elements[i].val;
                }
                elements[i].k++;
            } else {
                elements[i].k--;
                if (elements[i].k == k) {
                    sum += elements[i].val;
                }
            }
            map.put(elements[i].k, map.getOrDefault(elements[i].k, 0) + elements[i].val);
        }
    }

    private void pushDown() {
        if (add != 0) {
            map.clear();
            for (int i = 0; i < size; i++) {
                elements[i].k += add;
                map.put(elements[i].k, map.getOrDefault(elements[i].k, 0L) + elements[i].val);
            }
            add = 0;
        }
    }


    public void append(Element e) {
        pushDown();
        elements[size++] = e;
        map.put(e.k, map.getOrDefault(e.k, 0) + e.val);
        if (e.k <= k) {
            sum += e.val;
        }
    }
}