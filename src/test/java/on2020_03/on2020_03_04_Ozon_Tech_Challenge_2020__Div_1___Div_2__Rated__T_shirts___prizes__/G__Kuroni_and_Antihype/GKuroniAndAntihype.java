package on2020_03.on2020_03_04_Ozon_Tech_Challenge_2020__Div_1___Div_2__Rated__T_shirts___prizes__.G__Kuroni_and_Antihype;



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
