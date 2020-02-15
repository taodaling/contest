package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class GAGameOnStrings {
    char[] s;
    int n;
    int[] prefixSum;
    int[][] prefix;
    int[][] suffix;
    int[][] nextIndex;
    int[][] lastIndex;
    int charset = 'z' - 'a' + 1;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        s = new char[(int) 1e5];
        n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }
        prefixSum = new int[n];
        prefix = new int[charset][n];
        suffix = new int[charset][n];
        nextIndex = new int[charset][n];
        lastIndex = new int[charset][n];

        SequenceUtils.deepFill(prefixSum, -1);
        SequenceUtils.deepFill(prefix, -1);
        SequenceUtils.deepFill(suffix, -1);
        SequenceUtils.deepFill(nextIndex, n);
        SequenceUtils.deepFill(lastIndex, -1);

        for (int i = 0; i < n; i++) {
            if (i > 0) {
                for (int j = 0; j < charset; j++) {
                    lastIndex[j][i] = lastIndex[j][i - 1];
                }
            }
            lastIndex[s[i]][i] = i;
        }
        for (int i = n - 1; i >= 0; i--) {
            if (i < n - 1) {
                for (int j = 0; j < charset; j++) {
                    nextIndex[j][i] = nextIndex[j][i + 1];
                }
            }
            nextIndex[s[i]][i] = i;
        }

        for (int i = 0; i < n; i++) {
            prefixSum(i);
        }

        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            int sg = queryInterval(l, r);
            out.println(sg == 0 ? "Bob" : "Alice");
        }
    }

    int[] mex = new int[32];

    public int queryInterval(int l, int r) {
        if (l > r) {
            return 0;
        }
        for (int i = 0; i < charset; i++) {
            int begin = nextIndex[i][l];
            int end = lastIndex[i][r];
            if (begin > end) {
                continue;
            }
            int xor = prefixSum(begin, end);
            if (begin > l) {
                xor ^= suffix(i, l);
            }
            if (end < r) {
                xor ^= prefix(i, r);
            }
        }
        Arrays.fill(mex, 0);
        for (int i = 0; i < charset; i++) {
            int begin = nextIndex[i][l];
            int end = lastIndex[i][r];
            if (begin > end) {
                continue;
            }
            int xor = prefixSum(begin, end);
            if (begin > l) {
                xor ^= suffix(i, l);
            }
            if (end < r) {
                xor ^= prefix(i, r);
            }
            mex[xor] = 1;
        }

        for (int i = 0; ; i++) {
            if (mex[i] == 0) {
                return i;
            }
        }
    }

    public int prefix(int c, int i) {
        if (prefix[c][i] == -1) {
            prefix[c][i] = queryInterval(lastIndex[c][i] + 1, i);
        }
        return prefix[c][i];
    }

    public int suffix(int c, int i) {
        if (suffix[c][i] == -1) {
            suffix[c][i] = queryInterval(i, nextIndex[c][i] - 1);
        }
        return suffix[c][i];
    }

    public int prefixSum(int l, int r) {
        if (l >= r) {
            return 0;
        }
        return prefixSum(l) ^ prefixSum(r);
    }

    public int strictlyLastIndex(int i) {
        return i == 0 ? -1 : lastIndex[s[i]][i - 1];
    }

    public int strictlyNextIndex(int i) {
        return i == n - 1 ? n : nextIndex[s[i]][i + 1];
    }

    public int prefixSum(int i) {
        if (prefixSum[i] == -1) {
            int last = strictlyLastIndex(i);
            if (last == -1) {
                prefixSum[i] = 0;
            } else {
                prefixSum[i] = prefixSum(last) ^
                        queryInterval(last + 1, i - 1);
            }
        }
        return prefixSum[i];
    }
}
