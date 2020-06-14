package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.primitve.generated.datastructure.IntegerComparator;
import template.utils.CompareUtils;

import java.math.BigInteger;
import java.util.*;

public class EExpectationOfDivision {
    Modular mod = new Modular(1e9 + 7);
    Map<Seq, Integer> seqToId = new HashMap<>((int) 3e5);
    List<Seq> seqs = new ArrayList<>((int) 3e5);
    int[][] next = new int[50][];
    Power pow = new Power(mod);

    {
        for (int i = 0; i < 50; i++) {
            next[i] = new int[i];
        }
    }

    private int seqId(Seq seq) {
        Integer id = seqToId.get(seq);
        if (id == null) {
            seqs.add(seq);
            id = seqs.size() - 1;
            seqToId.put(seq, id);

            int m = seq.data.length;
            int[] transform = new int[m];
            for (int i = 0; i < m; i++) {
                transform[i] = subtract(seq, i);
            }
            seq.transform = transform;
            seq.prefix = new int[m];
            Arrays.fill(seq.prefix, -1);
        }

        return id;
    }

    public Integer subtract(Seq seq, int i) {
        int m = seq.data.length;
        int[] next = this.next[m];
        System.arraycopy(seq.data, 0, next, 0, m);
        next[i]--;
        Seq nextSeq = new Seq(next);
        return seqId(nextSeq);
    }

    public int prefix(long key) {
        int seqId = DigitUtils.highBit(key);
        int fix = DigitUtils.lowBit(key);
        Seq seq = seqs.get(seqId);
        if (fix >= seq.data.length) {
            return dp(seqId);
        }
        if (seq.prefix[fix] == -1) {
            int ans = 0;
            ans = mod.plus(ans, prefix(DigitUtils.asLong(seq.transform[fix], fix)));
            ans = mod.plus(ans, prefix(DigitUtils.asLong(seqId, fix + 1)));
            seq.prefix[fix] = ans;
        }
        return seq.prefix[fix];
    }

    public int dp(int seqId) {
        Seq seq = seqs.get(seqId);
        if (seq.dp == -1) {
            int m = seq.data.length;
            if (m == 0) {
                return seq.dp = 0;
            }
            int prod = 1;
            for (int i = 0; i < m; i++) {
                prod = mod.mul(prod, 1 + seq.data[i]);
            }
            int prob = pow.inverse(prod);
            int factor = pow.inverse(mod.subtract(1, prob));
            int sum = 0;
            for (int i = 0; i < m; i++) {
                sum = mod.plus(sum, prefix(DigitUtils.asLong(seq.transform[i], i)));
            }
            seq.dp = mod.plus(mod.mul(sum, prob), 1);
            seq.dp = mod.mul(seq.dp, factor);
        }
        return seq.dp;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        out.printf("Case #%d: ", testNumber);
        BigInteger n = new BigInteger(in.readString());
        int m = in.readInt();
        int[] data = this.next[m];
        Arrays.fill(data, 0);
        for (int i = 0; i < m; i++) {
            BigInteger p = BigInteger.valueOf(in.readInt());
            while (true) {
                BigInteger[] dar = n.divideAndRemainder(p);
                if (!dar[1].equals(BigInteger.ZERO)) {
                    break;
                }
                n = dar[0];
                data[i]++;
            }
        }

        int seqId = seqId(new Seq(data));
        int dp = dp(seqId);
        out.println(dp);
    }
}

class Seq {
    int[] data;
    int[] transform;
    int[] prefix;
    int dp = -1;

    public Seq(int[] seq) {
        CompareUtils.insertSort(seq, IntegerComparator.REVERSE_ORDER, 0, seq.length - 1);
        int suffix = seq.length;
        while (suffix > 0 && seq[suffix - 1] == 0) {
            suffix--;
        }
        data = Arrays.copyOfRange(seq, 0, suffix);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        return Arrays.equals(data, ((Seq) obj).data);
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
