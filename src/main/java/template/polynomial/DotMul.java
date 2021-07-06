package template.polynomial;

public class DotMul {
    public static void dotMulCover(int[] a, int[] b, int[] c, int l, int r, int mod) {
        for (int i = l; i <= r; i++) {
            c[i] = (int) ((long) a[i] * b[i] % mod);
        }
    }

    public static void dotMulPlus(int[] a, int[] b, int[] c, int l, int r, int mod) {
        for (int i = l; i <= r; i++) {
            c[i] = (int) (((long) a[i] * b[i] + c[i]) % mod);
        }
    }
}
