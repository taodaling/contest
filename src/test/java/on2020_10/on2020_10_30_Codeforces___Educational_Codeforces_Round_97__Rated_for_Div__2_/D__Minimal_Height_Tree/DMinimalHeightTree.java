package on2020_10.on2020_10_30_Codeforces___Educational_Codeforces_Round_97__Rated_for_Div__2_.D__Minimal_Height_Tree;



import template.io.FastInput;
import template.io.FastOutput;

public class DMinimalHeightTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int size = 1;
        int depth = 0;
        for (int i = 1; i < n; i++) {
            depth++;
            int j = i - 1;
            int next = 0;
            for (int k = 0; k < size && j + 1 < n; k++) {
                int begin = j;
                j++;
                while (j + 1 < n && a[j + 1] > a[j]) {
                    j++;
                }
                next += j - begin;
            }
            size = next;
            i = j;
        }

        out.println(depth);
    }
}
