package on2020_11.on2020_11_13_Codeforces___Codeforces_Round__335__Div__1_.D__Board_Game;



import dp.Lis;
import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.CollectionUtils;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

public class DBoardGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Card[] cards = new Card[n];
        IntegerArrayList xs = new IntegerArrayList(n * 2 + 1);
        xs.add(0);
        int inf = (int) 1e9;
        for (int i = 0; i < n; i++) {
            cards[i] = new Card();
            cards[i].a = in.readInt();
            cards[i].b = in.readInt();
            cards[i].c = in.readInt();
            cards[i].d = in.readInt();
            cards[i].id = i;
            cards[i].dist = inf;
            xs.add(cards[i].a);
            xs.add(cards[i].c);
        }

        xs.unique();
        for (int i = 0; i < n; i++) {
            cards[i].a = xs.binarySearch(cards[i].a);
            cards[i].c = xs.binarySearch(cards[i].c);
        }
        int m = xs.size();
        Segment seg = new Segment(0, m - 1);
        List<Card>[] groups = new List[m];
        for (int i = 0; i < m; i++) {
            groups[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            groups[cards[i].a].add(cards[i]);
        }
        for (int i = 0; i < m; i++) {
            groups[i].sort(Card.sortByB);
            if (!groups[i].isEmpty()) {
                seg.update(i, i, 0, m - 1, CollectionUtils.peek(groups[i]).b);
            }
        }

        Deque<int[]> dq = new ArrayDeque<>();
        dq.addFirst(new int[]{xs.binarySearch(0), 0, 0, -1});
        while (!dq.isEmpty()) {
            int[] head = dq.removeFirst();
            while (true) {
                int index = seg.query(0, head[0], 0, m - 1, head[1]);
                if (index == -1) {
                    break;
                }
                Card c = CollectionUtils.pop(groups[index]);
                c.prev = head[3];
                c.dist = head[2] + 1;
                dq.addLast(new int[]{c.c, c.d, c.dist, c.id});
                if (groups[index].isEmpty()) {
                    seg.update(index, index, 0, m - 1, Segment.inf);
                } else {
                    seg.update(index, index, 0, m - 1, CollectionUtils.peek(groups[index]).b);
                }
            }
        }

        if (cards[n - 1].dist == inf) {
            out.println(-1);
            return;
        }
        out.println(cards[n - 1].dist);
        List<Card> ans = new ArrayList<>();
        Card cur = cards[n - 1];
        while(true){
            ans.add(cur);
            if(cur.prev == -1){
                break;
            }
            assert cards[cur.prev].dist == cur.dist - 1;
            cur = cards[cur.prev];
        }
        SequenceUtils.reverse(ans);
        for(Card c : ans){
            out.append(c.id + 1).append(' ');
        }
    }
}

class Card {
    int a, b, c, d;

    int dist;
    int prev;
    int id;
    public static Comparator<Card> sortByB = (x, y) -> -Integer.compare(x.b, y.b);
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int height;
    static int inf = (int) 2e9;

    private void modify(int x) {
        height = x;
    }

    public void pushUp() {
        height = Math.min(left.height, right.height);
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
            height = inf;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r, int threshold) {
        if (noIntersection(ll, rr, l, r) || height > threshold) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        int ans = left.query(ll, rr, l, m, threshold);
        if (ans == -1) {
            ans = right.query(ll, rr, m + 1, r, threshold);
        }
        return ans;
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
            builder.append(height).append(",");
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