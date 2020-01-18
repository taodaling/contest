package on2020_01.on2020_01_18_Manthan__Codefest_18__rated__Div__1___Div__2_.H__Security;



import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import template.datastructure.Array2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;

public class HSecurity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        int n = s.length();
        SuffixTree st = new SuffixTree(i -> i < n ? s.charAt(i) : 'a' - 1, n + 1, 'a' - 1, 'z');
        st.dfs(st.getRoot());
        int q = in.readInt();
        Query[] queries = new Query[q];
        for (int i = 0; i < q; i++) {
            queries[i] = new Query();
            queries[i].l = in.readInt() - 1;
            queries[i].r = in.readInt() - 1;
            queries[i].x = in.readString();
        }
        Query[] sorted = queries.clone();
        Arrays.sort(sorted, (a, b) -> a.l - b.l);
        SimplifiedDeque<Query> deque = new Array2DequeAdapter<>(sorted);
        for (int i = s.length() - 1; i >= 0; i--) {
            st.enable(i);
            while (!deque.isEmpty() && deque.peekLast().l == i) {
                Query query = deque.removeLast();
                int m = query.x.length();
                query.ans = st.search(j -> j < m ? query.x.charAt(j) : 'a' - 1, m + 1, query.r);
            }
        }

        for (Query query : queries) {
            if (query.ans == -1) {
                out.println(query.ans);
                continue;
            }
            for (int j = query.ans; ; j++) {
                out.append(s.charAt(j));
                if (query.x.length() <= j - query.ans || s.charAt(j) != query.x.charAt(j - query.ans)) {
                    break;
                }
            }
            out.println();
        }
    }
}

class Query {
    int l;
    int r;
    String x;
    int ans;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static int inf = (int) 1e9;
    private int min = inf;

    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    public void pushDown() {
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

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            min = Math.min(min, x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
        }
        if (covered(ll, rr, l, r)) {
            return min;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.min(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
    }
}


/**
 * Based on
 * https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english/9513423#9513423
 */
class SuffixTree {
    private int minCharacter;
    private int maxCharacter;
    private int alphabet;
    private Node root;
    private int n;
    private IntUnaryOperator s;
    private Node[] suffixIndex2Node;
    private Segment segment;

    public static class Node {
        public int begin;
        public int end;
        public int depth; // distance in characters from root to this node
        public Node parent;
        public Node[] children;
        public Node suffixLink;
        int l;
        int r;

        Node(int begin, int end, int depth, Node parent, int alphabet) {
            this.begin = begin;
            this.end = end;
            this.parent = parent;
            this.depth = depth;
            children = new Node[alphabet];
        }
    }

    public void enable(int suffix) {
        segment.update(suffixIndex2Node[suffix].l, suffixIndex2Node[suffix].l, 1, n, suffix);
    }

    int idAlloc = 0;

    public void dfs(Node root) {
        if (root.end == n) {
            root.l = root.r = ++idAlloc;
            suffixIndex2Node[root.begin - root.depth] = root;
            return;
        }
        root.l = Integer.MAX_VALUE;
        root.r = Integer.MIN_VALUE;
        for (Node node : root.children) {
            if (node == null) {
                continue;
            }
            dfs(node);
            root.l = Math.min(root.l, node.l);
            root.r = Math.max(root.r, node.r);
        }
    }

    public int find(Node node) {
        return segment.query(node.l, node.r, 1, n);
    }

    public int search(IntUnaryOperator x, int m, int r) {
        Node trace = root;
        int sl = trace.begin;
        int sr = trace.end - 1;

        Node startPoint = null;
        int startIndex = -1;

        int i;
        for (i = 0; i < m; i++) {
            int val = x.applyAsInt(i);
            if (sl > sr) {
                if (trace.children[val - minCharacter] == null) {
                    startPoint = trace;
                    startIndex = val - minCharacter + 1;
                    break;
                }
                trace = trace.children[val - minCharacter];
                sl = trace.begin;
                sr = trace.end - 1;
            }
            if (s.applyAsInt(sl) != val) {
                startPoint = trace.parent;
                startIndex = SequenceUtils.indexOf(startPoint.children, 0, alphabet - 1, trace) + 1;
                if (s.applyAsInt(sl) > val) {
                    int suffix = find(trace);
                    if (r - suffix + 1 > i) {
                        return suffix;
                    }
                }
                break;
            }
            sl++;
            if (i == m - 1) {
                if (sl <= sr) {
                    throw new RuntimeException();
                }
                startPoint = trace.parent;
                startIndex = SequenceUtils.indexOf(startPoint.children, 0, alphabet - 1, trace) + 1;
            }
        }

        while (startPoint != null) {
            if (startIndex >= alphabet) {
                Node last = startPoint;
                startPoint = startPoint.parent;
                if (startPoint == null) {
                    break;
                }
                startIndex = x.applyAsInt(last.depth) - minCharacter + 1;
                continue;
            }
            if (startPoint.children[startIndex] != null) {
                int suffix = find(startPoint.children[startIndex]);
                if (r - suffix + 1 > startPoint.children[startIndex].depth) {
                    return suffix;
                }
            }
            startIndex++;
        }
        return -1;
    }

    public int search(Node node, int r) {
        if (node.parent == null) {
            return -1;
        }
        Node parent = node.parent;
        int index = SequenceUtils.indexOf(parent.children, 0, alphabet - 1, node);
        for (int i = index + 1; i < alphabet; i++) {
            if (parent.children[i] == null) {
                continue;
            }
            int suffix = find(parent.children[i]);
            if (r - suffix + 1 > parent.children[i].depth) {
                return suffix;
            }
        }
        return -1;
    }

    public Node getRoot() {
        return root;
    }

    public SuffixTree(IntUnaryOperator s, int n, int minCharacter, int maxCharacter) {
        suffixIndex2Node = new Node[n];
        segment = new Segment(1, n);
        this.n = n;
        this.s = s;
        int[] a = new int[n];
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        alphabet = maxCharacter - minCharacter + 1;
        for (int i = 0; i < n; i++) a[i] = s.applyAsInt(i) - minCharacter;
        root = new Node(0, 0, 0, null, alphabet);
        Node node = root;
        for (int i = 0, tail = 0; i < n; i++, tail++) {
            Node last = null;
            while (tail >= 0) {
                Node ch = node.children[a[i - tail]];
                while (ch != null && tail >= ch.end - ch.begin) {
                    tail -= ch.end - ch.begin;
                    node = ch;
                    ch = ch.children[a[i - tail]];
                }
                if (ch == null) {
                    node.children[a[i]] = new Node(i, n, node.depth + node.end - node.begin, node, alphabet);
                    if (last != null) last.suffixLink = node;
                    last = null;
                } else {
                    int afterTail = a[ch.begin + tail];
                    if (afterTail == a[i]) {
                        if (last != null) last.suffixLink = node;
                        break;
                    } else {
                        Node splitNode = new Node(ch.begin, ch.begin + tail, node.depth + node.end - node.begin, node, alphabet);
                        splitNode.children[a[i]] = new Node(i, n, ch.depth + tail, splitNode, alphabet);
                        splitNode.children[afterTail] = ch;
                        ch.begin += tail;
                        ch.depth += tail;
                        ch.parent = splitNode;
                        node.children[a[i - tail]] = splitNode;
                        if (last != null) last.suffixLink = splitNode;
                        last = splitNode;
                    }
                }
                if (node == root) {
                    --tail;
                } else {
                    node = node.suffixLink;
                }
            }
        }
    }

    public boolean contain(int[] seq, int l, int r) {
        Node node = root;
        int sl = 0;
        int sr = -1;
        for (int i = l; i <= r; i++) {
            if (sl > sr) {
                node = node.children[seq[i] - minCharacter];
                if (node == null) {
                    return false;
                }
                sl = node.begin;
                sr = node.end - 1;
            }
            if (seq[i] != s.applyAsInt(sl)) {
                return false;
            }
            sl++;
        }
        return true;
    }

    public int lcs(int leftEndIndex) {
        return DigitUtils.lowBit(lcs(root, leftEndIndex));
    }

    private long lcs(Node root, int leftEndIndex) {
        int mask = 0;
        int ans = 0;
        if (root.end == n) {
            if (root.begin - root.depth <= leftEndIndex) {
                return 1L << 32;
            } else {
                return 1L << 33;
            }
        }
        for (int i = 0; i < alphabet; i++) {
            Node node = root.children[i];
            if (node == null) {
                continue;
            }
            long sub = lcs(node, leftEndIndex);
            mask |= DigitUtils.highBit(sub);
            ans = Math.max(ans, DigitUtils.lowBit(sub));
        }
        if (mask == 3) {
            ans = Math.max(ans, root.depth + root.end - root.begin);
        }
        return DigitUtils.asLong(mask, ans);
    }
}
