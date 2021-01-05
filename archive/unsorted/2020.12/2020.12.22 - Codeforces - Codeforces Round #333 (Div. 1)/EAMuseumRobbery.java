package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Buffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EAMuseumRobbery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Item item = new Item(in.ri(), in.ri(), 0);
            items.add(item);
            item.id = items.size();
        }
        int q = in.ri();
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1) {
                Item item = new Item(in.ri(), in.ri(), i);
                items.add(item);
                item.id = items.size();
            } else if (t == 2) {
                int num = in.ri() - 1;
                items.get(num).die = i;
            } else {
                qs[i] = new Query();
            }
        }

        Segment seg = new Segment(0, q - 1);
        for (Item item : items) {
            if (item.die == -1) {
                item.die = q;
            }
            seg.update(item.born, item.die - 1, 0, q - 1, item);
        }
        seg.dfs(0, q - 1, new int[1001], (i, dp) -> {
            if (qs[i] == null) {
                return;
            }
            long a = (int) 1e7 + 19;
            long b = (int) 1e9 + 7;
            long sum = 0;
            for (int j = k; j >= 1; j--) {
                sum = (sum * a + dp[j]) % b;
            }
            qs[i].ans = (int) sum;
        });
        for (Query query : qs) {
            if (query == null) {
                continue;
            }
            out.println(query.ans);
        }
    }
}

class Query {
    int ans;

}

class Item {
    int id;
    int v;
    int w;
    int born;
    int die = -1;

    public Item(int v, int w, int born) {
        this.v = v;
        this.w = w;
        this.born = born;
    }
}

interface Callback {
    public void accept(int index, int[] dp);
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private List<Item> items = new ArrayList<>();

    private void modify(Item item) {
        items.add(item);
    }

    public void pushUp() {
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, Item item) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            modify(item);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, item);
        right.update(ll, rr, m + 1, r, item);
        pushUp();
    }

    static Buffer<int[]> buffer = new Buffer<>(() -> new int[1001]);

    public void dfs(int l, int r, int[] dp, Callback consumer) {
        int[] prev = buffer.alloc();
        System.arraycopy(dp, 0, prev, 0, prev.length);
        for (Item item : items) {
            for (int i = prev.length - 1; i >= 0; i--) {
                if (i + item.w < prev.length) {
                    prev[i + item.w] = Math.max(prev[i + item.w], prev[i] + item.v);
                }
            }
        }
        if (l < r) {
            int mid = DigitUtils.floorAverage(l, r);
            left.dfs(l, mid, prev, consumer);
            right.dfs(mid + 1, r, prev, consumer);
        } else {
            consumer.accept(l, prev);
        }
        buffer.release(prev);
    }

    public void query(int ll, int rr, int l, int r) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m);
        right.query(ll, rr, m + 1, r);
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append("val").append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}

