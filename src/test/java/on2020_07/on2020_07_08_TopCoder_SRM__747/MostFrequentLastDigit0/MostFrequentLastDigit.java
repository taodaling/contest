package on2020_07.on2020_07_08_TopCoder_SRM__747.MostFrequentLastDigit0;



public class MostFrequentLastDigit {
    public int[] generate(int n, int d) {
        int[] ans = new int[n];
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                if ((i + j) % 10 == d && (i == j || 2 * i % 10 != 2 * j % 10)) {
                    for (int t = 0; t < n / 2; t++) {
                        ans[t] = i + t * 10;
                    }
                    for (int t = n / 2; t < n; t++) {
                        ans[t] = j + t * 10;
                    }
                    return ans;
                }
            }
        }
        throw new RuntimeException();
    }
}
