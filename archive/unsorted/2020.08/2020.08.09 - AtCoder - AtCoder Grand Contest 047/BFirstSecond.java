package contest;

import com.topcoder.netCommon.io.ObjectReader;
import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BFirstSecond {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        PartialString[] ps = new PartialString[n];
        int[] s = new int[(int) 1e6];
        int[] suf = new int[(int) 1e6];
        Node root = new Node();
        long ans = 0;
        int wpos = 0;
        for (int i = 0; i < n; i++) {
            int len = in.readString(s, wpos);
            ps[i] = new PartialString(s, suf, wpos, wpos + len - 1);
            for (int j = 0; j < len; j++) {
                s[wpos + j] -= 'a';
            }
            SequenceUtils.reverse(s, wpos, wpos + len - 1);
            suf[wpos + len - 1] = 1 << s[wpos + len - 1];
            for (int j = len - 2; j >= 0; j--) {
                suf[wpos + j] = suf[wpos + j + 1] | (1 << s[wpos + j]);
            }
            wpos += len;
        }

        Arrays.sort(ps, (a, b) -> Integer.compare(a.length(), b.length()));
        for (PartialString string : ps) {
            int contrib = root.count(string);
            ans += contrib;
            root.add(string);
        }
        out.println(ans);
    }
}

class PartialString {
    int[] s;
    int[] suf;
    int l;
    int r;

    public PartialString(int[] s, int[] suf, int l, int r) {
        this.s = s;
        this.suf = suf;
        this.l = l;
        this.r = r;
    }

    public int get(int i) {
        return s[l + i];
    }

    public int getSuf(int i) {
        return suf[l + i];
    }

    public int length() {
        return r - l + 1;
    }
}

class Node {
    static int charset = 'z' - 'a' + 1;
    Node[] next = new Node[charset];
    int cnt;

    public Node get(int i) {
        if (next[i] == null) {
            next[i] = new Node();
        }
        return next[i];
    }

    public int count(PartialString seq) {
        int ans = 0;
        int i = 0;
        for(Node node = this; node != null; node = node.next[seq.get(i)], i++) {
            if (i == seq.length()) {
                break;
            }
            int suf = seq.getSuf(i);
            for (int j = 0; j < charset; j++) {
                if (node.next[j] != null && Bits.get(suf, j) == 1) {
                    ans += node.next[j].cnt;
                }
            }
        }
        return ans;
    }

    public void add(PartialString seq) {
        int i = 0;
        for(Node node = this; node != null; node = node.get(seq.get(i)), i++) {
            if (i == seq.length()) {
                node.cnt++;
                break;
            }
        }
    }
}