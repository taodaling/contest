package on2021_06.on2021_06_08_TopCoder_Open_Round__2B.IncorrectCancellation;



import template.binary.Bits;
import template.math.GCDs;

import java.util.Arrays;

public class IncorrectCancellation {
    public static void main(String[] args){
        long slowest = 0;
        IncorrectCancellation sol = new IncorrectCancellation();
        for(int i = 1; i <= 9999999; i++){
            if(i % 10000 == 0){
                System.out.println("test " + i);
            }
            long begin = System.currentTimeMillis();
            sol.find(i);
            long end = System.currentTimeMillis();
            slowest = Math.max(slowest, end - begin);
        }
        System.out.println("slowest = " + slowest);
    }

    void add(int x, int[] cnt, int sign) {
        while (x > 0) {
            int y = x / 10;
            cnt[x - y * 10] += sign;
            x = y;
        }
    }

    boolean substring(int x, int y){
        while(x != 0){
            while(y != 0 && y % 10 != x % 10){
                y /= 10;
            }
            if(y % 10 != x % 10){
                return false;
            }
            y /= 10;
            x /= 10;
        }
        return true;
    }

    public int find(int D) {
        String s = "" + D;
        int len = s.length();
        int[] del = new int[10];
        int[] delta = new int[10];
        for (int i = 1; i + 1 < 1 << len; i++) {
            int d = 0;
            Arrays.fill(del, 0);
            for (int j = 0; j < len; j++) {
                if (Bits.get(i, j) == 1) {
                    del[s.charAt(j) - '0']++;
                    continue;
                }
                int old = d;
                d = d * 10 + s.charAt(j) - '0';
                if(d == old){
                    d = 0;
                    break;
                }
            }
            if (d == 0) {
                continue;
            }
            //a / d = A / D
            int g = GCDs.gcd(D, d);
            int step = d / g;
            for (int a = step; a < d; a += step) {
                int A = (int) ((long)D * a / d);
                if(!substring(a, A)){
                    continue;
                }
                Arrays.fill(delta, 0);
                add(A, delta, 1);
                add(a, delta, -1);
                boolean ok = true;
                for(int j = 0; j < 10; j++){
                    if(delta[j] != del[j]){
                        ok = false;
                        break;
                    }
                }
                if(ok){
                    return A;
                }
            }
        }
        return -1;
    }
}
