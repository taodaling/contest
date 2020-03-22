package on2020_03.on2020_03_23_CodeChef___March_Cook_Off_2020_Division_2.Box_of_Chocolates;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerRMQ;
import template.utils.Debug;

public class BoxOfChocolates {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int h = n / 2;
        int[] w = new int[n + n];
        for (int i = 0; i < n; i++) {
            w[i] = w[i + n] = in.readInt();
        }
        IntegerRMQ rmq = new IntegerRMQ(w, (a, b) -> -Integer.compare(a, b));
        int max = w[rmq.query(0, n - 1)];
        int way = 0;
        for (int i = 0; i < n; i++) {
            int val = rmq.query(i, i + h - 1);
            if (w[val] < max) {
                way++;
            }
        }

        out.println(way);
    }
}
