package template.datastructure;

import template.math.DigitUtils;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

/**
 * Based on
 * https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english/9513423#9513423
 */
public class SuffixTree {
    private int minCharacter;
    private int maxCharacter;
    private int alphabet;
    private Node root;
    private int n;
    private IntUnaryOperator s;

    public static class Node {
        public int begin;
        public int end;
        public int depth; // distance in characters from root to this node
        public Node parent;
        public Node[] children;
        public Node suffixLink;

        Node(int begin, int end, int depth, Node parent, int alphabet) {
            this.begin = begin;
            this.end = end;
            this.parent = parent;
            this.depth = depth;
            children = new Node[alphabet];
        }
    }

    public Node getRoot(){
        return root;
    }

    public SuffixTree(IntUnaryOperator s, int n, int minCharacter, int maxCharacter) {
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
