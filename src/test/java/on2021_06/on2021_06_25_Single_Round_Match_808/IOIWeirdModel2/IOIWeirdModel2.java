package on2021_06.on2021_06_25_Single_Round_Match_808.IOIWeirdModel2;



import java.util.ArrayList;
import java.util.List;

public class IOIWeirdModel2 {
    public int pow(int x, int n) {
        int ans = 1;
        for (int i = 0; i < n; i++) {
            ans *= x;
        }
        return ans;
    }

    public int[] program(int L) {
        List<Integer> ans = new ArrayList<>();
        ans.add(11 * 7);
        ans.add(3 * 7);
        ans.add(1);
        ans.add(7);
        for (int i = 20; i >= 1; i--) {
            if (i < 20) {
                ans.add(pow(3, i));
            } else {
                ans.add(3 * 13);
            }
            ans.add((1 << i) * 11);
        }
        ans.add(pow(3, 19));
        ans.add(13);
        ans.add(10);
        ans.add(3);
        ans.add(1);
        ans.add(11);
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }
}
