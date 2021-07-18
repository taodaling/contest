package on2021_07.on2021_07_18_UOJ.FixedSizeSubsetTest;



import template.binary.FixedSizeSubsetGeneratorWithAttachment;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SummaryCalculator;

public class FixedSizeSubsetTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        FixedSizeSubsetGeneratorWithAttachment sg = new FixedSizeSubsetGeneratorWithAttachment();

        long[] bitWeight = in.rl(32);
        sg.setBitContrib(bitWeight);
        sg.setSummaryCalculator(new SummaryCalculator() {
            @Override
            public long add(long prev, long contrib) {
                return prev + contrib;
            }

            @Override
            public long remove(long prev, long contrib) {
                return prev - contrib;
            }
        });
        sg.init(n, k);
        while (sg.next()) {
            int mask = sg.mask();
            debug.debug("mask", mask);
            out.append(sg.mask()).append(' ').append(sg.sum()).println();
        }
    }
    Debug debug = new Debug(false);
}
