package on2019_11.on2019_11_01_Daily_training.Nod51_1073;



import template.FastInput;
import template.FastOutput;
import template.JosephusCircle;

public class Nod511073 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int ans = JosephusCircle.survivor(n, k);
        out.println(ans + 1);
    }
}
