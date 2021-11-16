package on2021_11.on2021_11_09_AtCoder___AtCoder_Beginner_Contest_226.D___Teleportation;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.primitve.generated.datastructure.LongHashSet;
import template.string.FastHash;

public class DTeleportation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] pts = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = in.ri();
            }
        }
        FastHash fh = new FastHash();
        LongHashSet set = new LongHashSet(n, false);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                int dx = pts[i][0] - pts[j][0];
                int dy = pts[i][1] - pts[j][1];
                int g = GCDs.gcd(Math.abs(dx), Math.abs(dy));
                dx /= g;
                dy /= g;
                set.add(fh.hash(dx, dy));
            }
        }
        out.println(set.size());
    }
}
