package contest;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AliceAndBobEasy {
    public int[] make(int N) {
        if (N == 1) {
            return new int[]{1};
        }
        if (N == 2) {
            return new int[]{1, 2};
        }

        int[] ans = new int[N];
        for (int i = 0; i < N - 1; i++) {
            ans[i] = (i + 1) * 2;
            ans[N - 1] ^= ans[i];
        }
        for (int i = 0; i < N - 1; i++) {
            if (ans[i] != ans[N - 1]) {
                ans[i] ^= 1 << 20;
                ans[N - 1] ^= 1 << 20;
                break;
            }
        }
        for (int i = 0; i < N; i++) {
            ans[i] ^= 1;
        }
        if (N % 2 == 0) {
            ans[N - 1] ^= 1;
        }

        if (xor(ans) != 1) {
            throw new RuntimeException();
        }
        if (allDiffer(ans) != ans.length) {
            throw new RuntimeException();
        }
        return ans;
    }

    public int allDiffer(int[] data) {
        return Arrays.stream(data).mapToObj(Integer::new).collect(Collectors.toSet()).size();
    }

    public int xor(int[] data) {
        int ans = 0;
        for (int x : data) {
            ans ^= x;
        }
        return ans;
    }
}
