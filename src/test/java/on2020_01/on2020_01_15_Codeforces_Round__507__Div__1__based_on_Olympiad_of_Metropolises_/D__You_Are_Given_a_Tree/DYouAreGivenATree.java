package on2020_01.on2020_01_15_Codeforces_Round__507__Div__1__based_on_Olympiad_of_Metropolises_.D__You_Are_Given_a_Tree;




import template.datastructure.HashLinkedListBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.IntUnaryOperator;

public class DYouAreGivenATree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[1], null, 0);
        dfsForAllocID(nodes[1], null);
        Node[] segId2Node = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            segId2Node[nodes[i].segId] = nodes[i];
        }


        allIntervals.sort((a, b) -> a.size - b.size);
        Deque<Interval> deque = new ArrayDeque<>(allIntervals);
        SequenceUtils.reverse(allIntervals);
        Deque<Interval> revDeq = new ArrayDeque<>(allIntervals);
        Segment segment = new Segment(1, n, i -> segId2Node[i].depth);


        final int inf = (int) 1e8;
        State state = new State();
        for (int i = 1; i <= n; i++) {
            while (deque.peekFirst().size < i) {
                Interval x = deque.removeFirst();
                revDeq.removeLast();

                state.reset();
                segment.query(x.l, x.r, 1, n, inf, state);
                int newVal = state.b - segId2Node[x.parentIndex].depth;
                segId2Node[x.parentIndex].pair.update(newVal);
                segment.update(x.parentIndex, x.parentIndex, 1, n, segId2Node[x.parentIndex].pair);
            }
            int total = 0;
            for (Interval x : deque) {
                int cnt = 0;
                int l = x.l;
                int r = x.r;
                while (l <= r) {
                    state.reset();
                    int rightMost = segment.query(l, r, 1, n, i, state);
                    if (rightMost == -1) {
                        break;
                    }
                    r = rightMost - 1;
                    cnt++;
                }
                if (l <= r && x.parentIndex != 0) {
                    state.reset();
                    segment.query(l, r, 1, n, inf, state);
                    int newVal = state.b - segId2Node[x.parentIndex].depth;
                    x.replace = true;
                    x.originVal = segId2Node[x.parentIndex].pair.clone();
                    segId2Node[x.parentIndex].pair.update(newVal);
                    segment.update(x.parentIndex, x.parentIndex, 1, n, segId2Node[x.parentIndex].pair);
                }

                total += cnt;
            }

            for (Interval x : revDeq) {
                if (x.replace) {
                    x.replace = false;
                    segment.update(x.parentIndex, x.parentIndex, 1, n, x.originVal);
                    segId2Node[x.parentIndex].pair = x.originVal;
                }
            }

            out.println(total);
        }
    }


    public void dfs(Node root, Node p, int depth) {
        root.next.remove(p);
        root.size = 1;
        root.heavy = null;
        root.depth = depth;
        root.deepest = depth;
        for (Node node : root.next) {
            dfs(node, root, depth + 1);
            root.size += node.size;
            if (root.heavy == null || root.heavy.size < node.size) {
                root.heavy = node;
            }
            root.deepest = Math.max(root.deepest, node.deepest);
        }
    }

    int alloc = 1;
    List<Interval> allIntervals = new ArrayList<>(100000);

    public Interval dfsForAllocID(Node root, Interval interval) {
        root.segId = alloc++;
        if (interval == null) {
            interval = new Interval();
            interval.l = root.segId;
            interval.size = root.size;
            allIntervals.add(interval);
        }
        interval.r = Math.max(interval.r, root.segId);
        if (root.heavy != null) {
            dfsForAllocID(root.heavy, interval);
        }
        for (Node node : root.next) {
            if (node == root.heavy) {
                continue;
            }
            Interval child = dfsForAllocID(node, null);
            child.parentIndex = root.segId;
        }
        return interval;
    }


}

class Interval {
    int parentIndex;
    Pair originVal;
    boolean replace;
    int l;
    int r;
    int size;
}

class Node {
    List<Node> next = new ArrayList<>();
    Node heavy;
    int depth;
    int deepest;
    int segId;
    int size;
    int id;
    Pair pair = new Pair();

    @Override
    public String toString() {
        return "" + id;
    }
}

class State {
    int a;
    int b;
    int c;

    public void reset() {
        a = b = c = (int) -1e8;
    }

    public void mergeInto(State left, State right) {
        int a = getA(left, right);
        int b = Math.max(left.b, right.b);
        int c = Math.max(left.c, right.c);

        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static int getA(State left, State right) {
        int ans = Math.max(left.a, right.a);
        ans = Math.max(ans, left.c + right.b + 1);
        return ans;
    }
}

class Pair implements Cloneable{
    int first;
    int second;

    @Override
    public Pair clone() {
        try {
            return (Pair) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(int x) {
        boolean ans = false;
        if (x > second) {
            second = x;
            ans = true;
        }
        if (second > first) {
            int tmp = first;
            first = second;
            second = tmp;
        }
        return ans;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    State state = new State();
    private int d;


    public void pushUp() {
        state.mergeInto(left.state, right.state);
    }

    public void pushDown() {
    }

    public void setVal(Pair val) {
        state.a = val.first + val.second + 1;
        state.b = val.first + d;
        state.c = val.first - d;
    }

    public Segment(int l, int r, IntUnaryOperator depthFunc) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m, depthFunc);
            right = new Segment(m + 1, r, depthFunc);
            pushUp();
        } else {
            d = depthFunc.applyAsInt(l);
            setVal(new Pair());
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, Pair x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            setVal(x);
            return ;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
        return;
    }

    public int query(int ll, int rr, int l, int r, int k, State state) {
        if (noIntersection(ll, rr, l, r)) {
            return -1;
        }
        if (covered(ll, rr, l, r) && State.getA(this.state, state) < k) {
            state.mergeInto(this.state, state);
            return -1;
        }
        if (l == r) {
            return l;
        }
        pushDown();
        int m = (l + r) >> 1;

        int index = right.query(ll, rr, m + 1, r, k, state);
        if (index == -1) {
            index = left.query(ll, rr, l, m, k, state);
        }
        return index;
    }
}
