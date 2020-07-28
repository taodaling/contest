package on2020_07.on2020_07_24_AtCoder___AtCoder_Grand_Contest_026.E___Synchronized_Subsequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

public class ESynchronizedSubsequence {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] s = new int[n * 2];
        for (int i = 0; i < 2 * n; i++) {
            s[i] = in.readChar() - 'a';
        }
        List<Block> blocks = new ArrayList<>(n);
        int cur = 0;
        int left = 0;
        for (int i = 0; i < 2 * n; i++) {
            if (s[i] == 0) {
                cur--;
            } else {
                cur++;
            }
            if (cur == 0) {
                Block b = new Block();
                b.start = s[left];
                b.data = Arrays.copyOfRange(s, left, i + 1);
                left = i + 1;
                if (b.start == 0) {
                    extractType0(b);
                } else {
                    extractType1(b);
                }
                blocks.add(b);
            }
        }
        debug.debug("blocks", blocks);

        List<Block> type1 = blocks.stream().filter(x -> x.start == 1).collect(Collectors.toList());
        for (int i = 0; i < type1.size(); i++) {
            type1.get(i).index = i;
        }
        type1.sort((a, b) -> {
            int ans = -a.seq.compareTo(b.seq);
            if (ans == 0) {
                ans = a.index - b.index;
            }
            return ans;
        });

        StringBuilder ans = new StringBuilder();
        int now = 0;
        for (Block b : type1) {
            if (b.index < now) {
                continue;
            }
            ans.append(b.seq);
            now = b.index + 1;
        }
        int tail = blocks.size();
        while (tail > 0 && blocks.get(tail - 1).start == 0) {
            tail--;
        }
        for (int i = tail; i < blocks.size(); i++) {
            ans.append(blocks.get(i).seq);
        }
        for (int i = 0; i < ans.length(); i++) {
            ans.setCharAt(i, (char) (ans.charAt(i) - '0' + 'a'));
        }
        out.println(ans.toString());
    }

    public String build(int[] data, boolean[] added) {
        StringBuilder builder = new StringBuilder(data.length);
        for (int i = 0; i < data.length; i++) {
            if (added[i]) {
                builder.append(data[i]);
            }
        }
        return builder.toString();
    }

    public int[][] getIndices(int[] data) {
        int[][] indices = new int[2][data.length / 2];
        int[] wpos = new int[2];
        for (int i = 0; i < data.length; i++) {
            indices[data[i]][wpos[data[i]]++] = i;
        }
        return indices;
    }

    public void extractType0(Block b) {
        int[] data = b.data;
        int[][] indices = getIndices(data);
        StringBuilder ans = new StringBuilder(data.length);
        int cur = 0;
        for (int i = 0; i < data.length / 2; i++) {
            if (cur <= indices[0][i]) {
                ans.append("01");
                cur = indices[1][i] + 1;
            }
        }
        b.seq = ans.toString();
    }

    public String max(String a, String b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public void extractType1(Block b) {
        int[] data = b.data;
        int[][] indices = getIndices(data);
        boolean[] added = new boolean[data.length];
        Arrays.fill(added, true);
        String s = build(data, added);
        for (int i = 0; i < data.length / 2; i++) {
            added[indices[0][i]] = added[indices[1][i]] = false;
            String cur = build(data, added);
            s = max(s, cur);
        }
        b.seq = s;
    }
}

class Block {
    int start;
    int[] data;
    int index;
    String seq;

    @Override
    public String toString() {
        return seq;
    }
}