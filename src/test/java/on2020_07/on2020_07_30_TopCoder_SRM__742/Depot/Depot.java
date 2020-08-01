package on2020_07.on2020_07_30_TopCoder_SRM__742.Depot;



import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Depot {
    public int countPositive(String[] arrivals, String[] queries) {
        Package[] packs = Arrays.stream(arrivals)
                .map(Package::new).toArray(x -> new Package[x]);
        int limit = (int) 1e6;
        long[] first = new long[limit];
        long inf = (long) 1e15;
        Arrays.fill(first, inf);
        first[0] = 0;
        for (int i = 1; i < limit; i++) {
            for (int j = 0; j < packs.length; j++) {
                if (i >= packs[j].s) {
                    long prev = first[i - packs[j].s];
                    long next = packs[j].nextDay(prev / 100, prev % 100 <= j) * 100
                            + (j + 1);
                    first[i] = Math.min(first[i], next);
                }
            }
        }

        int ans = 0;
        for (String query : queries) {
            int[] parsed = parse(query);
            if (parsed.length == 2) {
                int d = parsed[0];
                int s = parsed[1];
                if (first[s] / 100 <= d) {
                    ans++;
                }
            } else {
                long d = parsed[0];
                long s = parsed[1];
                long a1 = parsed[2];
                long b1 = parsed[3];
                long a2 = parsed[4];
                long b2 = parsed[5];
                long n = parsed[6];
                for(int i = 0; i < n; i++){
                    long day = (d + i * a1 - 1) % b1 + 1;
                    long size = (s + i * a2 - 1) % b2 + 1;
                    if(first[(int)size] / 100 <= day){
                        ans++;
                    }
                }
            }
        }

        return ans;
    }

    public int[] parse(String s) {
        StringTokenizer st = new StringTokenizer(s);
        List<Integer> list = new ArrayList<>();
        while (st.hasMoreTokens()) {
            list.add(Integer.parseInt(st.nextToken()));
        }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }
}

class Package {
    long d;
    long i;
    long m;
    int s;

    static long inf = (long) 1e15;

    public long nextDay(long x, boolean allowToday) {
        long end = d + (m - 1) * i;
        if (x > end) {
            return inf;
        }
        if (x < d) {
            return d;
        }
        //d + (k - 1) * i > x
        //(k - 1) > (x - d) / i
        long k;
        if (!allowToday) {
            k = DigitUtils.minimumIntegerGreaterThanDiv(x - d, i) + 1;
        } else {
            k = DigitUtils.ceilDiv(x - d, i) + 1;
        }
        return d + (k - 1) * i;
    }

    public Package(String s) {
        StringTokenizer st = new StringTokenizer(s);
        d = Integer.parseInt(st.nextToken());
        i = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        this.s = Integer.parseInt(st.nextToken());
    }
}