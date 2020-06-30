package on2020_07.on2020_07_01_TopCoder_SRM__757.MatchNim;



import template.algo.AscendingSequence;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MatchNim {
    int[][] dp;
    int[] piles;

    Map<AscendingSequence, Metadata> seqToData = new HashMap<>((int)1e6);
    int[][] buf1 = new int[11][];

    {
        for (int i = 0; i <= 10; i++) {
            buf1[i] = new int[i];
        }
    }

    public Metadata newData(AscendingSequence seq) {
        Metadata ans = seqToData.get(seq);
        if (ans == null) {
            seq = new AscendingSequence(seq.data.clone(), true);
            Metadata data = new Metadata();
            data.dp = -1;
            data.seq = seq;
            Arrays.fill(data.remove, -1);
            seqToData.put(seq, data);
            ans = data;
        }
        return ans;
    }

    public int remove(AscendingSequence seq, int k) {
        Metadata data = newData(seq);
        seq = data.seq;
        if (data.remove[k] == -1) {
            data.remove[k] = 0;
            int[] x = seq.data.clone();
            if (dp(seq) == 0) {
                data.remove[k] = 1;
            }
            if (k > 0) {
                for (int i = 0; i < seq.data.length; i++) {
                    int old = x[i];
                    x[i] = 0;
                    AscendingSequence next = copyBuf(x);
                    if (remove(next, k - 1) == 1) {
                        data.remove[k] = 1;
                    }
                    x[i] = old;
                }
            }
        }
        return data.remove[k];
    }

    public AscendingSequence copyBuf(int[] data) {
        int zero = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                zero++;
            }
        }
        int need = data.length - zero;
        int[] b = buf1[need];
        int wpos = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > 0) {
                b[wpos++] = data[i];
            }
        }
        return new AscendingSequence(b, false);
    }

    public int dp(AscendingSequence seq) {
        Metadata data = newData(seq);
        seq = data.seq;
        if (data.dp == -1) {
            data.dp = 0;
            //remove any
            int[] x = seq.data.clone();
            System.arraycopy(seq.data, 0, x, 0, x.length);
            for (int i = 0; i < x.length; i++) {
                int old = x[i];
                for (int j = 0; j < old; j++) {
                    x[i] = j;
                    AscendingSequence next = copyBuf(x);
                    if (remove(next, old - j) == 1) {
                        data.dp = 1;
                    }
                }
                x[i] = old;
            }
        }
        return data.dp;
    }

    public String whoWins(int[] piles) {
        this.piles = piles;
        for (int i = 0; i < piles.length; i++) {
            piles[i] = Math.min(10, piles[i]);
        }
        AscendingSequence seq = new AscendingSequence(piles, false);
        int ans = dp(seq);

        if (ans == 1) {
            return "Yvonne";
        } else {
            return "Zara";
        }
    }
}

class Metadata {
    AscendingSequence seq;
    int dp;
    int[] remove = new int[11];
}