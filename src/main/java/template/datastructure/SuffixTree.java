package template.datastructure;

import template.math.DigitUtils;

import java.util.Arrays;

/**
 * Based on
 * https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english/9513423#9513423
 */
public class SuffixTree {

    int minCharacter;
    int maxCharacter;
    int alphabet;
    Node root;
    int[] data;
    int len;
    Node activeNode;
    Node lastJump;
    int l = 0;

    public SuffixTree(int len, int minCharacter, int maxCharacter) {
        data = new int[len];
        alphabet = maxCharacter - minCharacter + 1;
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        root = new Node(null, alphabet);
        root.l = 0;
        root.r = -1;
        root.suffixLink = root;
        activeNode = root;
    }

    public void append(int x) {
        x -= minCharacter;
        data[len++] = x;
        lastJump = null;
        insert();
    }

    private void jump() {
        activeNode = activeNode.suffixLink;
        if (activeNode == null) {
            activeNode = root;
        }
        insert();
    }

    private Node newNode(Node parent) {
        Node node = new Node(parent, alphabet);
        if (lastJump != null) {
            lastJump.suffixLink = node;
        }
        return node;
    }

    public boolean contain(int[] seq, int l, int r) {
        Node trace = root;
        int sl = 0;
        int sr = -1;
        for (int i = l; i <= r; i++) {
            if (sl > sr) {
                trace = trace.next[seq[i] - minCharacter];
                if (trace == null) {
                    return false;
                }
                sl = trace.l;
                sr = Math.min(trace.r, len - 1);
            }
            if (data[sl] != seq[i] - minCharacter) {
                return false;
            }
            sl++;
        }
        return true;
    }

    private void insert() {
        if (l == len) {
            return;
        }
        Node node = activeNode.next[data[l]];
        if (node == null) {
            node = new Node(activeNode, alphabet);
            node.l = l;
            node.r = data.length;
            node.suffixStartIndex = l;
            activeNode.next[data[l]] = node;
            l++;
            jump();
            return;
        }
        if (data[node.l + len - 1 - l] == data[len - 1]) {
            if (node.r - node.l + 1 == len - l) {
                activeNode = node;
                l = len;
            }
            return;
        }

        Node split = newNode(activeNode);
        split.l = node.l;
        split.r = node.l + len - l - 2;
        node.l = split.r + 1;
        activeNode.next[data[l]] = split;
        split.next[data[node.l]] = node;
        node.setParent(split);

        Node inserted = new Node(split, alphabet);
        inserted.l = l + split.r - split.l + 1;
        inserted.r = data.length;
        inserted.suffixStartIndex = l;
        split.next[data[inserted.l]] = inserted;
        l++;
        jump();
    }

    public int lcs(int leftEndIndex) {
        return DigitUtils.lowBit(lcs(root, leftEndIndex));
    }

    private long lcs(Node root, int leftEndIndex) {
        int mask = 0;
        int ans = 0;
        if (root.suffixStartIndex != -1) {
            if (root.suffixStartIndex <= leftEndIndex) {
                return 1L << 32;
            } else {
                return 1L << 33;
            }
        }
        for (int i = 0; i < alphabet; i++) {
            Node node = root.next[i];
            if (node == null) {
                continue;
            }
            long sub = lcs(node, leftEndIndex);
            mask |= DigitUtils.highBit(sub);
            ans = Math.max(ans, DigitUtils.lowBit(sub));
        }
        if (mask == 3) {
            ans = Math.max(ans, root.parentDepth + root.size(len));
        }
        return DigitUtils.asLong(mask, ans);
    }
    int sequence = 0;
    private class Node {
        int id;

        public void setParent(Node parent){
            parentDepth = parent == null ? 0 : (parent.parentDepth + parent.r - parent.l + 1);
        }

        public Node(Node parent, int cap) {
            id = sequence++;
            setParent(parent);
            this.next = new Node[cap];
        }

        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOf(data, Math.min(r + 1, len)));
        }

        Node[] next;
        Node suffixLink;
        int l;
        int r;
        int suffixStartIndex = -1;
        int parentDepth;

        public int size(int len) {
            return Math.min(r, len - 1) - l + 1;
        }
    }
}
