package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.Randomized;
import template.utils.CompareUtils;

import java.util.Arrays;

public class Problem1CowPhotography {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        order = new int[5][n];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < n; j++) {
                order[i][j] = in.ri();
            }
        }
        IntegerArrayList list = new IntegerArrayList(order[0]);
        list.unique();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < n; j++) {
                order[i][j] = list.binarySearch(order[i][j]);
            }
        }
        invOrder = new int[5][n];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < n; j++) {
                invOrder[i][order[i][j]] = j;
            }
        }

        int[] elements = order[0].clone();
        Randomized.shuffle(elements);
        CompareUtils.quickSort(elements, this::compare, 0, n);
        for (int e : elements) {
            out.println(list.get(e));
        }
    }

    int[][] order;
    int[][] invOrder;

    public int compare(int a, int b) {
        int sign = 0;
        for (int i = 0; i < 5; i++) {
            sign += Integer.compare(invOrder[i][a], invOrder[i][b]);
        }
        return sign;
    }
}
