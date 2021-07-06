package platform.leetcode;


import java.util.*;

class Solution {
    public static void main(String[] args) {
        new Solution().longestCommonSubpath(5,
                new int[][]{{0, 1, 2, 3, 4}, {4, 3, 2, 1, 0}});
    }

    public int mirror(int n, int i) {
        return n - 1 - i;
    }

    public int longestCommonSubpath(int m, int[][] paths) {
        int n = paths.length;
        Arrays.sort(paths, Comparator.comparingInt(x -> x.length));
        SparseSuffixAutomaton sa = new SparseSuffixAutomaton();
        for (int e : paths[0]) {
            sa.build(e);
        }
        sa.topoSort();
        for (SparseSuffixAutomaton.SANode node : sa.all) {
            node.min = node.maxlen;
        }
        for (int i = 1; i < paths.length; i++) {
            sa.beginMatch();
            for (int e : paths[i]) {
                sa.match(e);
                sa.matchLast.localMax = Math.max(sa.matchLast.localMax, sa.matchLength);
            }
            for (SparseSuffixAutomaton.SANode node : sa.all) {
                if (node.fail != null && node.fail.localMax > 0) {
                    node.fail.localMax = node.fail.maxlen;
                }
                node.min = Math.min(node.min, node.localMax);
                node.localMax = 0;
            }
        }

        int best = 0;
        for (SparseSuffixAutomaton.SANode node : sa.all) {
            best = Math.max(best, node.min);
        }

        return best;
    }
}

class SparseSuffixAutomaton {
    public SANode root;
    public SANode buildLast;
    public SANode matchLast;
    public int matchLength;
    public List<SANode> all = new ArrayList<>();
    public boolean sorted = true;
    public long distinctSubstr = -1;

    public void enableDistinctSubstr() {
        distinctSubstr = 0;
    }

    public SparseSuffixAutomaton() {
        buildLast = root = newNode();
        root.fail = null;
    }

    private SANode newNode() {
        SANode ans = new SANode();
        all.add(ans);
        return ans;
    }

    private SANode cloneNode(SANode x) {
        SANode ans = x.clone();
        all.add(ans);
        return ans;
    }

    public void beginMatch() {
        matchLast = root;
        matchLength = 0;
    }

    public void match(int c) {
        int index = c;
        if (matchLast.next.containsKey(index)) {
            matchLast = matchLast.next.get(index);
            matchLength = matchLength + 1;
            return;
        }
        while (matchLast != null && !matchLast.next.containsKey(index)) {
            matchLast = matchLast.fail;
        }
        if (matchLast == null) {
            matchLast = root;
            matchLength = 0;
        } else {
            matchLength = matchLast.maxlen + 1;
            matchLast = matchLast.next.get(index);
        }
    }

    public void build(int c) {
        sorted = false;
        int index = c;
        SANode now = newNode();
        now.maxlen = buildLast.maxlen + 1;

        SANode p = visit(index, buildLast, null, now);
        if (p == null) {
            now.fail = root;
        } else {
            SANode q = p.next.get(index);
            if (q.maxlen == p.maxlen + 1) {
                now.fail = q;
            } else {
                SANode clone = cloneNode(q);
                clone.maxlen = p.maxlen + 1;
                now.fail = q.fail = clone;
                if (distinctSubstr != -1) {
                    distinctSubstr -= q.maxlen - clone.fail.maxlen;
                    distinctSubstr += q.maxlen - q.fail.maxlen;
                    distinctSubstr += clone.maxlen - clone.fail.maxlen;
                }
                visit(index, p, q, clone);
            }
        }
        if (distinctSubstr != -1) {
            distinctSubstr += now.maxlen - now.fail.maxlen;
        }
        buildLast = now;
    }

    public SANode visit(int index, SANode trace, SANode target, SANode replacement) {
        while (trace != null && trace.next.get(index) == target) {
            trace.next.put(index, replacement);
            trace = trace.fail;
        }
        return trace;
    }

    public void topoSort() {
        if (sorted) {
            return;
        }
        sorted = true;
        Deque<SANode> dq = new ArrayDeque<>(all.size());
        for (SANode node : all) {
            if (node.fail != null) {
                node.fail.indeg++;
            }
        }
        for (SANode node : all) {
            if (node.indeg == 0) {
                dq.addLast(node);
            }
        }
        all.clear();
        while (!dq.isEmpty()) {
            SANode head = dq.removeFirst();
            all.add(head);
            if (head.fail != null) {
                head.fail.indeg--;
                if (head.fail.indeg == 0) {
                    dq.addLast(head.fail);
                }
            }
        }
    }


    public static class SANode implements Cloneable {
        public TreeMap<Integer, SANode> next = new TreeMap<>();
        public SANode fail;
        public int maxlen;
        public int right;
        public int indeg;
        public int localMax;
        public int min;

        public int minLength() {
            return fail == null ? 0 : fail.maxlen + 1;
        }

        public SANode() {
        }

        @Override
        public SANode clone() {
            try {
                SANode res = (SANode) super.clone();
                res.next = new TreeMap<>(res.next);
                return res;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}