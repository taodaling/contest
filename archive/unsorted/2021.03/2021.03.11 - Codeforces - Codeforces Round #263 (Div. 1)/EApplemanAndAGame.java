package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.*;
import java.util.function.LongPredicate;

public class EApplemanAndAGame {
    long linf = (long) 1e18 + 10;

    private long[][] mul(long[][] a, long[][] b) {
        long[][] ans = new long[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ans[i][j] = linf;
                for (int k = 0; k < 4; k++) {
                    ans[i][j] = Math.min(a[i][k] + b[k][j], ans[i][j]);
                }
            }
        }
        return ans;
    }

    private long[][] pow(long[][] x, long k) {
        if (k == 0) {
            long[][] ans = new long[4][4];
            SequenceUtils.deepFill(ans, linf);
            for(int i = 0; i < 4; i++){
                ans[i][i] = 0;
            }
            return ans;
        }
        long[][] ans = pow(x, k / 2);
        ans = mul(ans, ans);
        if (k % 2 == 1) {
            ans = mul(ans, x);
        }
        return ans;
    }


    public long atLeast(long[][] dp, long k) {
        long[][] res = pow(dp, k);
        long ans = linf;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ans = Math.min(ans, res[i][j]);
            }
        }
        return ans;
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        char[] s = new char[(int) 1e5];
        int m = in.rs(s);
        SuffixAutomaton sa = new SuffixAutomaton('A', 'D');
        sa.prepareBuild();
        for (int i = 0; i < m; i++) {
            sa.build(s[i]);
        }
        SuffixAutomaton.SANode root = sa.root;
        long[][] dp = new long[4][4];
        for (int i = 0; i < 4; i++) {
            dp[i] = dp(root.next[i]);
        }
//        long[][] before = new long[4][4];
//        for(int i = 0; i <= 4; i++){
//            debug.debug("i", i);
//            debug.debug("x^i", before);
//            before = mul(before, dp);
//        }
//        debug.debug("dp", dp);
//        debug.debug("check", atLeast(dp, 4));
        LongPredicate predicate = mid -> {
            return atLeast(dp, mid) < n;
        };
        long ans = BinarySearch.lastTrue(predicate, 0, n) + 1;
        out.println(ans);
    }

    int inf = (int) 1e8;

    public long[] dp(SuffixAutomaton.SANode root) {
        if (root.nearest == null) {
            root.nearest = new long[4];
            Arrays.fill(root.nearest, inf);
            for (int i = 0; i < 4; i++) {
                if (root.next[i] == null) {
                    root.nearest[i] = 1;
                    continue;
                }
                long[] res = dp(root.next[i]);
                for (int j = 0; j < 4; j++) {
                    root.nearest[j] = Math.min(root.nearest[j], 1 + res[j]);
                }
            }
        }
        return root.nearest;
    }
}

class SuffixAutomaton {
    final int minCharacter;
    final int maxCharacter;
    final int alphabet;
    public SANode root;
    public SANode buildLast;
    public SANode matchLast;
    public int matchLength;
    public List<SANode> all;
    public boolean sorted = true;

    public long realTimeDistinctSubstr = -1;

    public void enableDistinctSubstr() {
        realTimeDistinctSubstr = 0;
    }

    public void prepareBuild() {
        buildLast = root;
    }

    public SuffixAutomaton(int minCharacter, int maxCharacter) {
        this(minCharacter, maxCharacter, 0);
    }

    public SuffixAutomaton(int minCharacter, int maxCharacter, int cap) {
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        all = new ArrayList<>(cap * 2 + 1);
        alphabet = maxCharacter - minCharacter + 1;
        buildLast = root = newNode();
        root.fail = null;
    }

    private SANode newNode() {
        SANode ans = new SANode(alphabet);
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
        int index = c - minCharacter;
        if (matchLast.next[index] != null) {
            matchLast = matchLast.next[index];
            matchLength = matchLength + 1;
        } else {
            while (matchLast != null && matchLast.next[index] == null) {
                matchLast = matchLast.fail;
            }
            if (matchLast == null) {
                matchLast = root;
                matchLength = 0;
            } else {
                matchLength = matchLast.maxlen + 1;
                matchLast = matchLast.next[index];
            }
        }
    }

    public void build(int c) {
        sorted = false;
        int index = c - minCharacter;
        SANode now = newNode();
        now.maxlen = buildLast.maxlen + 1;

        SANode p = visit(index, buildLast, null, now);
        if (p == null) {
            now.fail = root;
        } else {
            SANode q = p.next[index];
            if (q.maxlen == p.maxlen + 1) {
                now.fail = q;
            } else {
                SANode clone = cloneNode(q);
                clone.maxlen = p.maxlen + 1;
                now.fail = q.fail = clone;
                if (realTimeDistinctSubstr != -1) {
                    realTimeDistinctSubstr -= q.maxlen - clone.fail.maxlen;
                    realTimeDistinctSubstr += q.maxlen - q.fail.maxlen;
                    realTimeDistinctSubstr += clone.maxlen - clone.fail.maxlen;
                }
                visit(index, p, q, clone);
            }
        }
        if (realTimeDistinctSubstr != -1) {
            realTimeDistinctSubstr += now.maxlen - now.fail.maxlen;
        }
        buildLast = now;
    }

    public SANode visit(int index, SANode trace, SANode target, SANode replacement) {
        while (trace != null && trace.next[index] == target) {
            trace.next[index] = replacement;
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

    public void calcRight(IntToIntegerFunction func, int n) {
        topoSort();
        beginMatch();
        for (int i = 0; i < n; i++) {
            match(func.apply(i));
            matchLast.right++;
        }
        for (SANode node : all) {
            if (node.fail != null) {
                node.fail.right += node.right;
            }
        }
    }


    public static class SANode implements Cloneable {
        public SANode[] next;
        public long[] nearest;
        /**
         * right最小的一个顶点，且满足fail.right是right的真超集
         */
        public SANode fail;
        /**
         * 对于每个right集合中的元素r，以及minLength()<=i<=maxlen, S[r-i+1,r]都会转移到这个状态
         */
        public int maxlen;
        /**
         * right表示这个子串在S中出现的右端点位置数目
         */
        public int right;
        public int indeg;

        public SANode(int alphabet) {
            next = new SANode[alphabet];
        }

        public int minLength() {
            return fail == null ? 0 : fail.maxlen + 1;
        }

        @Override
        public SANode clone() {
            try {
                SANode res = (SANode) super.clone();
                res.next = res.next.clone();
                return res;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}