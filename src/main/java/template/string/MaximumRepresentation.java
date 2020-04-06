package template.string;

import template.primitve.generated.datastructure.IntToIntFunction;

/**
 * Used to search the maximum substring in circular string.
 */
public class MaximumRepresentation {
    public static int solve(IntToIntFunction func, int n) {
        int i = 0;
        int j = i + 1;
        while (j < n) {
            int k = 0;
            while (k < n && func.apply((i + k) % n) == func.apply((j + k) % n)) {
                k++;
            }
            if (func.apply((i + k) % n) >= func.apply((j + k) % n)) {
                j = j + k + 1;
            } else {
                int next = j;
                j = Math.max(j + 1, i + k + 1);
                i = next;
            }
        }
        return i;
    }
}
