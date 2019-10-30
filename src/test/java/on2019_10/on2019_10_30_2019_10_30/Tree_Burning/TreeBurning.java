package on2019_10.on2019_10_30_2019_10_30.Tree_Burning;



import template.FastInput;
import template.FastOutput;

public class TreeBurning {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int l = in.readInt();
        int n = in.readInt();
        int[] x = new int[n + 1];
        for(int i = 1; i <= n; i++){
            x[i] = in.readInt();
        }

        long[] clockwisePreSum = new long[n + 2];
        long[] countClockwisePreSum = new long[n + 2];
        for(int i = 1; i <= n; i++){
            clockwisePreSum[i] = clockwisePreSum[i - 1] + x[i];
        }
        for(int i = n; i >= 1; i--){
            countClockwisePreSum[i] = countClockwisePreSum[i + 1] + (l - x[i]);
        }

        long ans = 0;
        for(int i = 1; i <= n; i++){
            int lCnt = i - 1;
            int rCnt = n - i;
            int oneCnt = Math.min(lCnt, rCnt);

            //same
            ans = Math.max(ans, Math.max(x[i], l - x[i]) +
                    (clockwisePreSum[i - 1] - clockwisePreSum[i - 1 - oneCnt] +
                    countClockwisePreSum[i + 1] - countClockwisePreSum[i + 1 + oneCnt]) * 2);

            //leftMore
            if(lCnt > 0){
                int rOneCnt = Math.min(lCnt - 1, rCnt);
                int lOneCnt = rOneCnt + 1;
                ans = Math.max(ans, l - x[i] +
                        (clockwisePreSum[i - 1] - clockwisePreSum[i - 1 - lOneCnt] +
                        countClockwisePreSum[i + 1] - countClockwisePreSum[i + 1 + rOneCnt]) * 2);
            }

            //rightMore
            if(rCnt > 0){
                int lOneCnt = Math.min(lCnt, rCnt - 1);
                int rOneCnt = lOneCnt + 1;
                ans = Math.max(ans, x[i] +
                        (clockwisePreSum[i - 1] - clockwisePreSum[i - 1 - lOneCnt] +
                        countClockwisePreSum[i + 1] - countClockwisePreSum[i + 1 + rOneCnt]) * 2);
            }
        }

        out.println(ans);
    }
}
