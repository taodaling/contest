package on2019_12.on2019_12_30_Good_Bye_2019.G__Subset_with_Zero_Sum;



import template.graph.CircularPath;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerList;
import template.primitve.generated.MultiWayIntegerStack;

public class GSubsetWithZeroSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n + 1];
        MultiWayIntegerStack edges = new MultiWayIntegerStack(n + 1, n);
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
            edges.addLast(i, i - a[i]);
        }

        CircularPath path = new CircularPath(edges);
        IntegerList circle = path.getCircular();
        out.println(circle.size());
        for (int i = 0; i < circle.size(); i++) {
            out.append(circle.get(i)).append(' ');
        }
    }
}

