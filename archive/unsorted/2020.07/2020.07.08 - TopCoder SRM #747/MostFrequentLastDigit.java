package contest;

public class MostFrequentLastDigit {
    public int[] generate(int n, int d) {
        int[] ans = new int[n];
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                if ((i + j) % 10 == d) {
                    for (int t = 0; t < n / 2; t++) {
                        ans[t] = i;
                    }
                    for (int t = n / 2; t < n; t++) {
                        ans[t] = j;
                    }
                    return ans;
                }
            }
        }
        throw new RuntimeException();
    }
}
