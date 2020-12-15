package on2020_12.on2020_12_14_HDOJ.Partition;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.problem.PartitionNumber;
import template.problem.WayToSplitNumber;

public class Partition {
    int mod = (int)1e9 + 7;
    IntPoly poly = new IntPolyFFT(mod);
    PartitionNumber wayToSplitNumber = new PartitionNumber((int)1e5, mod, poly);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        out.println(wayToSplitNumber.query(n));
    }
}
