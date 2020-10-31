package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.io.PrintWriter;
import java.util.Arrays;

public class RestaurantCustomers {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];

        IntegerArrayList list = new IntegerArrayList();

        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            b[i] = in.readInt();
            list.add(a[i]);
            list.add(b[i]);
        }
        list.unique();
        for (int i = 0; i < n; i++) {
            a[i] = list.binarySearch(a[i]);
            b[i] = list.binarySearch(b[i]);
        }
        int[] cnts = new int[list.size()];
        for (int i = 0; i < n; i++) {
            cnts[a[i]]++;
            cnts[b[i]]--;
        }
        for (int i = 1; i < list.size(); i++) {
            cnts[i] += cnts[i - 1];
        }
        int ans = Arrays.stream(cnts).max().orElse(-1);
        out.println(ans);
    }
}
