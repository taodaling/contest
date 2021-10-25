package on2021_10.on2021_10_09_AtCoder___AtCoder_Beginner_Contest_221.G___Jumping_sequence1;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.KnapsackProblemWithBoundedWeights;

import java.util.Arrays;

public class GJumpingSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = in.ri();
        int[] d = in.ri(n);
        int x = a - b;
        int y = a + b;
        int[] xs = solve(d, x);
        int[] ys = solve(d, y);
        char[][] map = new char[3][3];
        map[-1 + 1][-1 + 1] = 'L';
        map[1 + 1][-1 + 1] = 'D';
        map[1 + 1][1 + 1] = 'R';
        map[-1 + 1][1 + 1] = 'U';
        if(xs == null || ys == null){
            out.println("No");
            return;
        }
        out.println("Yes");
        for(int i = 0; i < n; i++){
            out.append(map[xs[i] + 1][ys[i] + 1]);
        }
        out.println();
    }

    public int[] solve(int[] a, int target) {
        boolean neg = target < 0;
        target = Math.abs(target);
        int sum = Arrays.stream(a).sum();
        if (sum < target || sum % 2 != target % 2) {
            return null;
        }
        int reduce = (sum - target) / 2;
        boolean[] sol = KnapsackProblemWithBoundedWeights.solution(a, reduce);
        int get = 0;
        for (int i = 0; i < sol.length; i++) {
            if (sol[i]) {
                get += a[i];
            }
        }
        if (get != reduce) {
            return null;
        }
        int[] sign = new int[a.length];
        Arrays.fill(sign, 1);
        for (int i = 0; i < a.length; i++) {
            if (sol[i]) {
                sign[i] = -1;
            }
            if (neg) {
                sign[i] = -sign[i];
            }
        }
        return sign;
    }
}
