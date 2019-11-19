package on2019_11.on2019_11_19_Codeforces_Round__601__Div__1_.B1___Send_Boxes_to_Alice__Easy_Version_;



import template.*;

public class TaskB1 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            sum += a[i];
        }
        if (sum == 1) {
            out.println(-1);
            return;
        }
        long ans = Long.MAX_VALUE;
        LongList allPrimes = new LongList();
        for (long i = 2; i * i <= sum; i++) {
            if (sum % i != 0) {
                continue;
            }
            allPrimes.add(i);
            while (sum % i == 0) {
                sum /= i;
            }
        }
        if (sum > 1) {
            allPrimes.add(sum);
        }
        for (int i = 0; i < allPrimes.size(); i++) {
            ans = Math.min(ans, solve(a, allPrimes.get(i), n, ans));

        }
        out.println(ans);
    }

    public long solve(int[] a, long k, int n, long now) {
        if (k == 1) {
            return Long.MAX_VALUE;
        }
        long sum = 0;
        int middleIndex = -1;
        long dist = 0;
        long middle = DigitUtils.ceilDiv(k, 2);
        for (int i = 0; i < n; i++) {
            if (middleIndex == -1) {
                dist += sum;
            }
            long remain = a[i] % k;
            if (remain + sum >= middle && middleIndex == -1) {
                middleIndex = i;
            }
            long used = Math.min(k - sum, remain);
            remain -= used;
            sum += used;
            if (middleIndex != -1) {
                dist += used * (i - middleIndex);
            }
            remain %= k;
            if (sum == k) {
                sum = remain;
                middleIndex = remain >= middle ? i : -1;
            }
            if (dist >= now) {
                return now;
            }
        }

        return dist;
    }
}
