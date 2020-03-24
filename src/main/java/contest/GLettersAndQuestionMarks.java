package contest;


import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMultiWayDeque;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.*;

public class GLettersAndQuestionMarks {
    Debug debug = new Debug(true);
    char[] buf = new char[1000000];
    int charset = 'n' - 'a' + 1;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        debug.debug("charset", charset);
        int k = in.readInt();
        ACAutomaton ac = new ACAutomaton('a', 'n');
        for (int i = 0; i < k; i++) {
            int n = in.readString(buf, 0);
            ac.beginBuilding();
            for (int j = 0; j < n; j++) {
                ac.build(buf[j]);
            }
            ac.getBuildLast().weight += in.readInt();
        }
        ac.endBuilding();
        ACAutomaton.Node[] nodes = ac.getAllNodes().toArray(new ACAutomaton.Node[0]);
        int m = nodes.length;
        int n = in.readString(buf, 0);

        int mask = (1 << charset) - 1;
        long[][] cur = new long[m][1 + mask];
        long[][] last = new long[m][1 + mask];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(last, -inf);
        last[0][0] = 0;

        int[] transfer = new int[m];
        long[] collect = new long[m];
        List<Integer>[] dq = new List[charset + 1];
        for (int i = 0; i <= charset; i++) {
            dq[i] = new ArrayList<>();
        }
        for (int i = 0; i <= mask; i++) {
            dq[Integer.bitCount(i)].add(i);
        }

        int now = 0;
        for (int i = 0; i < n; i++) {
            //debug.debug("last", last);
            int l = i;
            int r = nextQuest(buf, i, n);
            debug.debug("l", l);
            debug.debug("r", r);
            debug.debug("now", now);
            i = r;

            for (int j = 0; j < m; j++) {
                transfer[j] = j;
                collect[j] = 0;
            }
            for (int j = l; j < r; j++) {
                int offset = buf[j] - 'a';
                for (int t = 0; t < m; t++) {
                    transfer[t] = nodes[transfer[t]].next[offset].id;
                    collect[t] += nodes[transfer[t]].weight;
                }
            }

            if (r != n) {
                for (int x : dq[now + 1]) {
                    for (int j = 0; j < m; j++) {
                        cur[j][x] = -inf;
                    }
                }
                for (int t : dq[now]) {
                    for (int z = 0; z < charset; z++) {
                        if (Bits.bitAt(t, z) == 1) {
                            continue;
                        }
                        for (int j = 0; j < m; j++) {
                            int nid = nodes[transfer[j]].next[z].id;
                            int bit = Bits.setBit(t, z, true);
                            cur[nid][bit] = Math.max(cur[nid][bit], last[j][t] + collect[j] + nodes[nid].weight);
                        }
                    }
                }

                now++;
            } else {
                for (int x : dq[now]) {
                    for (int j = 0; j < m; j++) {
                        cur[j][x] = -inf;
                    }
                }
                for (int t : dq[now]) {
                    for (int j = 0; j < m; j++) {
                        cur[transfer[j]][t] = Math.max(cur[transfer[j]][t], last[j][t] + collect[j]);
                    }
                }
            }
            long[][] tmp = cur;
            cur = last;
            last = tmp;
        }

        long max = -inf;
        for (int j : dq[now]) {
            for (int i = 0; i < m; i++) {
                max = Math.max(max, last[i][j]);
            }
        }

        out.println(max);
    }

    public int nextQuest(char[] buf, int i, int n) {
        for (int j = i; j < n; j++) {
            if (buf[j] == '?') {
                return j;
            }
        }
        return n;
    }
}


class ACAutomaton {
    private final int MIN_CHARACTER;
    private final int MAX_CHARACTER;
    private final int RANGE;
    private Node root;
    private Node buildLast;
    private Node matchLast;
    private List<Node> allNodes = new ArrayList();

    public Node getBuildLast() {
        return buildLast;
    }

    public Node getMatchLast() {
        return matchLast;
    }

    public List<Node> getAllNodes() {
        return allNodes;
    }

    private Node addNode() {
        Node node = new Node(RANGE);
        node.id = allNodes.size();
        allNodes.add(node);
        return node;
    }

    public ACAutomaton(int minCharacter, int maxCharacter) {
        MIN_CHARACTER = minCharacter;
        MAX_CHARACTER = maxCharacter;
        RANGE = maxCharacter - minCharacter + 1;
        root = addNode();
    }

    public void beginBuilding() {
        buildLast = root;
    }

    public void endBuilding() {
        Deque<Node> deque = new ArrayDeque(allNodes.size());
        for (int i = 0; i < RANGE; i++) {
            if (root.next[i] != null) {
                deque.addLast(root.next[i]);
            }
        }

        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            Node fail = visit(head.father.fail, head.index);
            if (fail == null) {
                head.fail = root;
            } else {
                head.fail = fail.next[head.index];
            }
            head.weight += head.fail.weight;
            head.preSum = head.cnt + head.fail.preSum;
            for (int i = 0; i < RANGE; i++) {
                if (head.next[i] != null) {
                    deque.addLast(head.next[i]);
                }
            }
        }

        for (int i = 0; i < RANGE; i++) {
            if (root.next[i] != null) {
                deque.addLast(root.next[i]);
            } else {
                root.next[i] = root;
            }
        }
        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            for (int i = 0; i < RANGE; i++) {
                if (head.next[i] != null) {
                    deque.addLast(head.next[i]);
                } else {
                    head.next[i] = head.fail.next[i];
                }
            }
        }
    }

    public Node visit(Node trace, int index) {
        while (trace != null && trace.next[index] == null) {
            trace = trace.fail;
        }
        return trace;
    }

    public void build(char c) {
        int index = c - MIN_CHARACTER;
        if (buildLast.next[index] == null) {
            Node node = addNode();
            node.father = buildLast;
            node.index = index;
            buildLast.next[index] = node;
        }
        buildLast = buildLast.next[index];
    }

    public void beginMatching() {
        matchLast = root;
    }

    public void match(char c) {
        int index = c - MIN_CHARACTER;
        matchLast = matchLast.next[index];
    }

    public static class Node {
        public Node[] next;
        Node fail;
        Node father;
        int index;
        int id;
        int cnt;
        int preSum;
        long weight;

        public int getId() {
            return id;
        }

        public int getCnt() {
            return cnt;
        }

        public void decreaseCnt() {
            cnt--;
        }

        public void increaseCnt() {
            cnt++;
        }

        public int getPreSum() {
            return preSum;
        }

        public Node(int range) {
            next = new Node[range];
        }

//        @Override
//        public String toString() {
//            return father == null ? "" : (father.toString() + (char) (MIN_CHARACTER + index));
//        }
    }
}