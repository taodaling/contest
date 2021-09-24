package on2021_08.on2021_08_14_AtCoder___AtCoder_Beginner_Contest_213.F___Common_Prefixes;



import template.io.FastInput;
import template.io.FastOutput;
import template.string.IntFunctionIntSequenceAdapter;
import template.string.IntSequence;
import template.string.SuffixArrayDC3;

import java.util.ArrayDeque;
import java.util.Deque;

public class FCommonPrefixes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s);
        IntSequence seq = new IntFunctionIntSequenceAdapter(i -> s[i], 0, n - 1);
        int[] sa = SuffixArrayDC3.suffixArray(seq);
        int[] rank = SuffixArrayDC3.rank(sa);
        int[] lcp = SuffixArrayDC3.lcp(sa, rank, seq);
        long[] ans = new long[n];
        dq = new ArrayDeque<>(n);
        init();
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                compress(lcp[i - 1]);
            }
            push(new Item(length(sa[i], n), 1));
            ans[sa[i]] += sum;
        }
        init();
        for (int i = n - 1; i >= 0; i--) {
            if (i + 1 < n) {
                compress(lcp[i]);
            }
            push(new Item(length(sa[i], n), 1));
            ans[sa[i]] += sum;
        }
        for(int i = 0; i < n; i++){
            ans[i] -= length(i, n);
        }
        for(long x : ans){
            out.println(x);
        }
    }

    public int length(int start, int n) {
        return n - start;
    }

    Deque<Item> dq;
    long sum = 0;

    public void init() {
        dq.clear();
        sum = 0;
    }

    public Item pop() {
        Item tail = dq.removeLast();
        sum -= (long) tail.val * tail.cnt;
        return tail;
    }

    public void push(Item item) {
        if (item.cnt == 0) {
            return;
        }
        if (!dq.isEmpty() && dq.peekLast().val == item.val) {
            item.cnt += pop().cnt;
        }
        dq.addLast(item);
        sum += (long) item.val * item.cnt;
    }

    public void compress(int t) {
        int cnt = 0;
        while (!dq.isEmpty() && dq.peekLast().val > t) {
            Item tail = pop();
            cnt += tail.cnt;
        }
        push(new Item(t, cnt));
    }
}

class Item {
    int val;
    int cnt;

    public Item(int val, int cnt) {
        this.val = val;
        this.cnt = cnt;
    }
}
