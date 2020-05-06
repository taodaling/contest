import java.util.Arrays;

public class Rectoggle {
    public int whoWins(int[] ledrow, int[] ledcol, int maxrows, int maxcols) {
        int n = ledcol.length;
        maxrows++;
        maxcols++;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            int x = ledrow[i];
            int y = ledcol[i];

            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    int nx = x + j;
                    int ny = y + k;
                    nx %= maxrows;
                    ny %= maxcols;
                    sum ^= Nimber.product(nx, ny);
                }
            }
        }

        if (sum == 0) {
            return 2;
        }
        return 1;
    }

}

class SequenceUtils {
    public static void deepFill(Object array, int val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof int[]) {
            int[] intArray = (int[]) array;
            Arrays.fill(intArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

}

class Nimber {
    private static int[][] nimProductCache = new int[16][16];
    private static int[][] nimProductPowerCache = new int[16][16];

    static {
        SequenceUtils.deepFill(nimProductCache, -1);
        SequenceUtils.deepFill(nimProductPowerCache, -1);
    }

    public static long product(long a, long b) {
        if (a < b) {
            return product(b, a);
        }
        if (a < 16 && nimProductCache[(int) a][(int) b] != -1) {
            return nimProductCache[(int) a][(int) b];
        }
        long ans;
        if (b <= 1) {
            ans = a * b;
        } else {
            long m = getLargestFermatNumber(a);
            long p = a / m;
            long q = a % m;
            long s = b / m;
            long t = b % m;

            long c1 = product(p, s);
            long c2 = product(p, t) ^ product(q, s);
            long c3 = product(q, t);
            ans = ((c1 ^ c2) * m) ^ c3 ^ productPower(m / 2, c1);
        }

        if (a < 16) {
            nimProductCache[(int) a][(int) b] = (int) ans;
        }

        return ans;
    }

    private static long productPower(long a, long b) {
        if (a < 16 && nimProductPowerCache[(int) a][(int) b] != -1) {
            return nimProductPowerCache[(int) a][(int) b];
        }
        long ans;
        if (b <= 1 || a <= 1) {
            ans = a * b;
        } else {
            long m = getLargestFermatNumber(a);
            long p = a / m;
            long s = b / m;
            long t = b % m;

            long d1 = productPower(p, s);
            long d2 = productPower(p, t);
            ans = ((d1 ^ d2) * m) ^ productPower(m / 2, d1);
        }

        if (a < 16) {
            nimProductPowerCache[(int) a][(int) b] = (int) ans;
        }

        return ans;
    }

    private static long getLargestFermatNumber(long x) {
        int y = Log2.floorLog(Log2.floorLog(x));
        return 1L << (1 << y);
    }

}

class Log2 {
    public static int floorLog(int x) {
        return 31 - Integer.numberOfLeadingZeros(x);
    }

    public static int floorLog(long x) {
        return 63 - Long.numberOfLeadingZeros(x);
    }

}
