package contest;

import template.rand.Randomized;
import template.utils.CompareUtils;

import java.util.Arrays;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.size;

public class MaxPoints {
    public int findMaxPoints(int N, int[] XG, int[] YG, long K, int seedX, int seedY) {
        long[] A = new long[N];
        A[0] = seedX;
        for (int i = 1; i <= N - 1; i++) {
            A[i] = (A[i - 1] * 1103515245 + 12345) % 2147483648L;
        }
        int[] X = Arrays.copyOf(XG, N);
        for (int i = XG.length; i <= N - 1; i++) {
            X[i] = (int) ((A[i] % 2000001) - 1000000);
        }
        long[] B = new long[N];
        B[0] = seedY;
        for (int i = 1; i <= N - 1; i++) {
            B[i] = (B[i - 1] * 1103515245 + 12345) % 2147483648L;
        }
        int[] Y = Arrays.copyOf(YG, N);
        for (int i = YG.length; i <= N - 1; i++) {
            Y[i] = (int) ((B[i] % 2000001) - 1000000);
        }

        long[] xDist = dist(X);
        long[] yDist = dist(Y);
        long[] dist = new long[N];
        for (int i = 0; i < N; i++) {
            dist[i] = xDist[i] + yDist[i];
        }

        long limit = 0;
        for (int i = 0; i < N; i++) {
            limit += dist[i];
        }
        limit = limit / 2 + K;

        Randomized.shuffle(dist);
        Arrays.sort(dist);
        int ans = 0;
        for (int i = 0; i < N && limit - dist[i] >= 0; i++) {
            limit -= dist[i];
            ans++;
        }

        if (limit < 0) {
            return -1;
        }
        return ans;
    }


    public long[] dist(int[] x) {
        int n = x.length;
        int[] index = new int[n];
        for (int i = 0; i < n; i++) {
            index[i] = i;
        }
        CompareUtils.quickSort(index, (a, b) -> Integer.compare(x[a], x[b]), 0, n);
        long pSum = 0;
        long sSum = 0;
        long[] dist = new long[n];
        for (int i = 0; i < n; i++) {
            dist[index[i]] += (long) x[index[i]] * i - pSum;
            pSum += x[index[i]];
        }
        for (int i = n - 1; i >= 0; i--) {
            dist[index[i]] += sSum - (long) x[index[i]] * (n - 1 - i);
            sSum += x[index[i]];
        }

        return dist;
    }
}
