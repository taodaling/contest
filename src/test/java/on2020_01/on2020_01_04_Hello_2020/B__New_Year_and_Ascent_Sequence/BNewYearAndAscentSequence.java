package on2020_01.on2020_01_04_Hello_2020.B__New_Year_and_Ascent_Sequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerBIT;
import template.primitve.generated.IntegerPreSum;

public class BNewYearAndAscentSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Seq[] seqs = new Seq[n];
        for (int i = 0; i < n; i++) {
            int l = in.readInt();
            seqs[i] = new Seq();
            seqs[i].max = seqs[i].min = in.readInt();
            for (int j = 1; j < l; j++) {
                int x = in.readInt();
                if (x > seqs[i].min) {
                    seqs[i].include = true;
                }
                seqs[i].max = Math.max(seqs[i].max, x);
                seqs[i].min = Math.min(seqs[i].min, x);
            }
        }

        int limit = 1000000;
        int[] cnts = new int[limit + 1];
        int include = 0;
        for (int i = 0; i < n; i++) {
            if (!seqs[i].include) {
                cnts[seqs[i].min]++;
            } else {
                include++;
            }
        }

        IntegerPreSum ps = new IntegerPreSum(cnts);
        long ans = 0;
        for (int i = 0; i < n; i++) {
            if (!seqs[i].include) {
                ans += ps.intervalSum(0, seqs[i].max - 1) + include;
            } else {
                ans += n;
            }
        }

        out.println(ans);
    }
}

class Seq {
    int min;
    int max;
    boolean include;
}
