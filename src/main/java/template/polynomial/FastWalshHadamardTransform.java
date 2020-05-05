package template.polynomial;

public class FastWalshHadamardTransform {
    public static void orFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        orFWT(p, l, m);
        orFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[m + 1 + i] = a + b;
        }
    }

    public static void orIFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[m + 1 + i] = b - a;
        }
        orIFWT(p, l, m);
        orIFWT(p, m + 1, r);
    }

    public static void orFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        orFWT(p, l, m);
        orFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[m + 1 + i] = a + b;
        }
    }

    public static void orIFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[m + 1 + i] = b - a;
        }
        orIFWT(p, l, m);
        orIFWT(p, m + 1, r);
    }

    public static void andFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        andFWT(p, l, m);
        andFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[l + i] = a + b;
        }
    }

    public static void andIFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[l + i] = a - b;
        }
        andIFWT(p, l, m);
        andIFWT(p, m + 1, r);
    }


    public static void andFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        andFWT(p, l, m);
        andFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = a + b;
        }
    }

    public static void andIFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = a - b;
        }
        andIFWT(p, l, m);
        andIFWT(p, m + 1, r);
    }

    public static void xorFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        xorFWT(p, l, m);
        xorFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[l + i] = a + b;
            p[m + 1 + i] = a - b;
        }
    }

    public static void xorIFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[l + i] = (a + b) / 2;
            p[m + 1 + i] = (a - b) / 2;
        }
        xorIFWT(p, l, m);
        xorIFWT(p, m + 1, r);
    }

    public static void xorFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        xorFWT(p, l, m);
        xorFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = a + b;
            p[m + 1 + i] = a - b;
        }
    }

    public static void xorIFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) >> 1;
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = (a + b) / 2;
            p[m + 1 + i] = (a - b) / 2;
        }
        xorIFWT(p, l, m);
        xorIFWT(p, m + 1, r);
    }

    public static void dotMul(int[] a, int[] b, int[] c, int l, int r) {
        for (int i = l; i <= r; i++) {
            c[i] = a[i] * b[i];
        }
    }

    public static void dotMul(long[] a, long[] b, long[] c, int l, int r) {
        for (int i = l; i <= r; i++) {
            c[i] = a[i] * b[i];
        }
    }
}
