import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class MatchNim {
    int[] piles;
    Map<AscendingSequence, Metadata> seqToData = new HashMap<>((int) 1e6);
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

class CompareUtils {
    private CompareUtils() {
    }

    public static void insertSort(int[] data, IntegerComparator cmp, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            int j = i;
            int val = data[i];
            while (j > l && cmp.compare(data[j - 1], val) > 0) {
                data[j] = data[j - 1];
                j--;
            }
            data[j] = val;
        }
    }

}

interface IntegerComparator {
    public static final IntegerComparator NATURE_ORDER = (a, b) -> Integer.compare(a, b);

    public int compare(int a, int b);

}

final class AscendingSequence {
    public int[] data;

    public AscendingSequence(int[] data, boolean sorted) {
        if (!sorted) {
            CompareUtils.insertSort(data, IntegerComparator.NATURE_ORDER, 0, data.length - 1);
        }
        this.data = data;
    }

    public int hashCode() {
        return Arrays.hashCode(data);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AscendingSequence)) {
            return false;
        }
        AscendingSequence as = (AscendingSequence) obj;
        return Arrays.equals(data, as.data);
    }

}
