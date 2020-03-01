package on2020_03.on2020_03_01_Codeforces_Round__625__Div__1__based_on_Technocup_2020_Final_Round_.C__World_of_Darkraft__Battle_for_Azathoth0;




import template.datastructure.Range2DequeAdapter;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CWorldOfDarkraftBattleForAzathoth {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int p = in.readInt();
        weapon = new Item[n];
        armor = new Item[m + 1];
        armor[m] = new Item();
        armor[m].w = (long) 1e15;
        armor[m].price = (long) 1e15;
        for (int i = 0; i < n; i++) {
            weapon[i] = new Item();
            weapon[i].w = in.readInt();
            weapon[i].price = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            armor[i] = new Item();
            armor[i].w = in.readInt();
            armor[i].price = in.readInt();
        }
        weapon = trim(weapon);
        armor = trim(armor);
        Monster[] monsters = new Monster[p];
        for (int i = 0; i < p; i++) {
            monsters[i] = new Monster();
            monsters[i].x = in.readInt();
            monsters[i].y = in.readInt();
            monsters[i].z = in.readInt();
        }
        Arrays.sort(monsters, (a, b) -> Integer.compare(a.x, b.x));
        Range2DequeAdapter<Monster> dq = new Range2DequeAdapter<>(i -> monsters[i],
                0, p - 1);

        Segment segment = new Segment(0, armor.length - 1);
        for (int i = 0; i < armor.length; i++) {
            segment.updateSet(i, i, 0, armor.length - 1, -armor[i].price);
        }
        long ans = -weapon[0].price - armor[0].price;
        for (Item w : weapon) {
            while (!dq.isEmpty() && dq.peekFirst().x < w.w) {
                Monster monster = dq.removeFirst();
                int index = binarySearch(armor, monster.y + 1);
                segment.update(index, armor.length - 1, 0, armor.length - 1, monster.z);
            }
            ans = Math.max(ans, segment.query(0, armor.length - 1, 0, armor.length - 1) - w.price);
        }
        out.println(ans);
    }

    public int binarySearch(Item[] items, int x) {
        int l = 0;
        int r = items.length - 1;
        while (l < r) {
            int m = (l + r) >> 1;
            if (items[m].w >= x) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        return l;
    }

    Item[] weapon;
    Item[] armor;

    public Item[] trim(Item[] items) {
        int n = items.length;
        int len = 1;
        Arrays.sort(items, (a, b) -> Long.compare(a.w, b.w));
        for (int i = 1; i < n; i++) {
            if (items[i].w == items[len - 1].w) {
                if (items[i].price < items[len - 1].price) {
                    SequenceUtils.swap(items, i, len - 1);
                }
                continue;
            }
            while (len > 0 && items[len - 1].price >= items[i].price) {
                len--;
            }
            SequenceUtils.swap(items, i, len);
            len++;
        }
        return Arrays.copyOf(items, len);
    }
}

class Monster {
    int x;
    int y;
    int z;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long max;
    private long dirty;

    public void modify(long x) {
        dirty += x;
        max += x;
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void updateSet(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            max = x;
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.updateSet(ll, rr, l, m, x);
        right.updateSet(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return (long) -1e18;
        }
        if (covered(ll, rr, l, r)) {
            return max;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.max(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
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

class Item {
    long w;
    long price;
}
