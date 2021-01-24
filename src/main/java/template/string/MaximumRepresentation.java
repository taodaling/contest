package template.string;

import template.primitve.generated.datastructure.IntToIntFunction;

/**
 * Used to search the maximum substring in circular string.
 */
public class MaximumRepresentation {
    /**
     * For circular string func[0..n-1],
     * get the start point of maximum representation in O(n)
     */
    public static int maximumRepresentation(IntToIntFunction func, int n) {
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

    /**
     * For circular string func[0..n-1],
     * get the start point of maximum representation in O(n)
     */
    public static int minimumRepresentation(IntToIntFunction func, int n) {
        return maximumRepresentation(i -> -func.apply(i), n);
    }
}
