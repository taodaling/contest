package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;

public class SalaryQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] salary = new int[n];
        char[] type = new char[q];
        int[] left = new int[q];
        int[] right = new int[q];
        in.populate(salary);
        for (int i = 0; i < q; i++) {
            type[i] = in.readChar();
            left[i] = in.readInt();
            right[i] = in.readInt();
        }
        IntegerArrayList list = new IntegerArrayList(n + q * 2);
        list.addAll(salary);
        list.addAll(left);
        list.addAll(right);
        list.unique();
        for (int i = 0; i < n; i++) {
            salary[i] = list.binarySearch(salary[i]);
        }
        IntegerBIT bit = new IntegerBIT(list.size());
        for (int i = 0; i < n; i++) {
            bit.update(salary[i] + 1, 1);
        }
        for (int i = 0; i < q; i++) {
            if (type[i] == '!') {
                int k = left[i] - 1;
                int x = list.binarySearch(right[i]);
                bit.update(salary[k] + 1, -1);
                salary[k] = x;
                bit.update(salary[k] + 1, 1);
            } else {
                int l = list.binarySearch(left[i]);
                int r = list.binarySearch(right[i]);
                int ans = bit.query(l + 1, r + 1);
                out.println(ans);
            }
        }
    }
}
