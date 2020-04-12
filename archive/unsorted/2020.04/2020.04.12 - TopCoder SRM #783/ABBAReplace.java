package contest;

public class ABBAReplace {
    public int countSteps(String Sprefix, int N, int seed, int threshold) {
        int aCnt = 0;
        int ans = 0;
        long state = seed;
        long mask = (1L << 31) - 1;
        for (char c : Sprefix.toCharArray()) {
            if (c == 'A') {
                aCnt++;
            } else {
                if (aCnt > 0) {
                    ans = Math.max(ans + 1, aCnt);
                }

            }
        }

        int length = Sprefix.length();
        while (length < N) {
            state = (state * 1103515245 + 12345) & mask;
            if (state < threshold) {
                aCnt++;
            } else {
                if (aCnt > 0) {
                    ans = Math.max(ans + 1, aCnt);
                }
            }
            length++;
        }
        return ans;
    }
}
