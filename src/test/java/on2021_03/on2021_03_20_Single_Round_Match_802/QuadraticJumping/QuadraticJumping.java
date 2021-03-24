package on2021_03.on2021_03_20_Single_Round_Match_802.QuadraticJumping;



import template.problem.SumOfSquares;

public class QuadraticJumping {
    public long jump(long goal) {
        long sum = 0;
        for (int i = 0; ; i++) {
            sum += (long) i * i;
            if (sum < goal || (sum - goal) % 2 != 0) {
                continue;
            }
            long delta = (sum - goal) / 2;
            if (SumOfSquares.isSumOfDistinctSquares(delta)) {
                return i;
            }
        }
    }
}
