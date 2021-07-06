package template.string;

import template.datastructure.RMQBeta;

public class SubstringCompare {
    int[] sa;
    int[] lcp;
    int[] rank;
    RMQBeta rmq;

    public int[] lcp() {
        return lcp;
    }

    public int[] sa() {
        return sa;
    }

    public int[] rank() {
        return rank;
    }

    /**
     * empty string is considered
     */
    public long distinctSubstring() {
        long ans = 1;
        int n = rank.length;
        for (int i = 0; i < n; i++) {
            long len = n - i;
            if (rank[i] < n - 1) {
                len -= lcp[rank[i]];
            }
            ans += len;
        }
        return ans;
    }

    public int lcpBetweenSuffix(int l, int L) {
        if (l == L) {
            return sa.length - l;
        }
        int r1 = rank[l];
        int r2 = rank[L];
        if (r1 > r2) {
            int tmp = r1;
            r1 = r2;
            r2 = tmp;
        }
        int bothLcp = lcp[rmq.query(r1, r2 - 1)];
        return bothLcp;
    }

    public SubstringCompare(IntSequence seq) {
        if (seq.length() <= 1) {
            sa = new int[1];
            rank = new int[1];
            lcp = new int[0];
            return;
        }
//        SAIS sais = new SAIS(seq);
//        sa = sais.rank2Index;
//        rank = sais.index2Rank;
//        lcp = sais.lcp;
        sa = SuffixArrayDC3.suffixArray(seq);
        rank = SuffixArrayDC3.rank(sa);
        lcp = SuffixArrayDC3.lcp(sa, rank, seq);
        rmq = new RMQBeta(lcp.length, (i, j) -> Integer.compare(lcp[i], lcp[j]));
    }

    public int compare(int l, int r, int L, int R) {
        if (l == L) {
            return Integer.compare(r, R);
        }
        int len1 = r - l + 1;
        int len2 = R - L + 1;
        int bothLcp = lcpBetweenSuffix(l, L);
        if (bothLcp >= len1 || bothLcp >= len2) {
            return Integer.compare(len1, len2);
        }
        return Integer.compare(rank[l], rank[L]);
    }
}
