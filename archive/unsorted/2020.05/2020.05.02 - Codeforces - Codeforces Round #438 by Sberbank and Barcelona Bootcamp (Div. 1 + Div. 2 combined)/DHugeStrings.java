package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class DHugeStrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<StringHolder> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(build(in.readString()));
        }

        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            StringHolder ans = concatenate(list.get(a), list.get(b));
            out.println(ans.report());
            list.add(ans);
        }
    }

    int limit = 12;
    int limitLength = 1 << limit;

    public void analyse(BitSet[] bs, String s) {
        for (int i = 1; i <= limit; i++) {
            int mask = (1 << i) - 1;
            int cur = 0;
            for (int j = 0; j < s.length(); j++) {
                int c = s.charAt(j) - '0';
                cur = (cur << 1) | c;
                if (j >= i - 1) {
                    bs[i].set(cur & mask);
                }
            }
        }
    }

    public void trim(StringHolder holder) {
        if (holder.prefix.length() > limitLength) {
            holder.prefix = holder.prefix.substring(0, limitLength);
        }
        if (holder.suffix.length() > limitLength) {
            holder.suffix = holder.suffix.substring(holder.suffix.length() - limitLength);
        }
    }

    public StringHolder build(String s) {
        StringHolder holder = new StringHolder(limit);
        holder.prefix = s;
        holder.suffix = s;
        holder.length = s.length();
        analyse(holder.bits, s);
        trim(holder);
        return holder;
    }

    long inf = (long) 1e18;

    public StringHolder concatenate(StringHolder a, StringHolder b) {
        StringHolder holder = new StringHolder(limit);
        holder.prefix = a.prefix;
        holder.suffix = b.suffix;
        holder.length = Math.min(inf, a.length + b.length);
        for (int i = 1; i <= limit; i++) {
            holder.bits[i].or(a.bits[i]);
            holder.bits[i].or(b.bits[i]);
        }
        String middle = a.suffix + b.prefix;
        analyse(holder.bits, middle);
        if (a.length < limitLength) {
            holder.prefix = a.prefix + b.prefix;
        }
        if (b.length < limitLength) {
            holder.suffix = middle;
        }
        trim(holder);
        return holder;
    }
}

class StringHolder {
    long length;
    String prefix;
    String suffix;
    BitSet[] bits;

    public StringHolder(int n) {
        bits = new BitSet[n + 1];
        for (int i = 1; i <= n; i++) {
            bits[i] = new BitSet(1 << i);
        }
    }

    public int report() {
        int ans = 0;
        while (ans + 1 < bits.length && bits[ans + 1].cardinality() == (1 << (ans + 1))) {
            ans++;
        }
        return ans;
    }

    @Override
    public String toString() {
        return "" + report();
    }
}