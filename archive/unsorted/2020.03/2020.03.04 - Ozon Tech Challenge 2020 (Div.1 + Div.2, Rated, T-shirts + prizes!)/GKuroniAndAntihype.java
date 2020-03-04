package contest;

import template.algo.SubsetGenerator;
import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class GKuroniAndAntihype {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] cnts = new int[1 << 18];
        cnts[0]++;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            sum -= x;
            cnts[x]++;
        }
        DSU dsu = new DSU(1 << 18);
        SubsetGenerator sg = new SubsetGenerator();
        for (int i = (1 << 18) - 1; i >= 0; i--) {
            sg.reset(i);
            while (sg.hasNext()) {
                int a = sg.next();
                int b = i - a;
                if (a < b) {
                    break;
                }
                if (cnts[a] != 0 && cnts[b] != 0 && dsu.find(a) != dsu.find(b)) {
                    sum += (long) (cnts[a] + cnts[b] - 1) * i;
                    cnts[a] = cnts[b] = 1;
                    dsu.merge(a, b);
                }
            }
        }


        out.println(sum);
    }
}
