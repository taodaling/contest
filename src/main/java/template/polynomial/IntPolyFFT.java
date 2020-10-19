package template.polynomial;

import template.math.DigitUtils;
import template.utils.PrimitiveBuffers;

public class IntPolyFFT extends IntPoly {
    private static final int FFT_THRESHOLD = 50;
    private static final int FFT_DIVIDE_THRESHOLD = 200;

    public IntPolyFFT(int mod) {
        super(mod);
    }

    @Override
    public int[] convolution(int[] a, int[] b) {
        if (a != b) {
            return multiplyMod(a, b);
        } else {
            return pow2(a);
        }
    }

    @Override
    public int[] pow2(int[] a) {
        int rA = rankOf(a);
        if (rA < FFT_THRESHOLD) {
            return mulBF(a, a);
        }

        int need = rA * 2 + 1;

        double[] aReal = PrimitiveBuffers.allocDoublePow2(need);
        double[] aImag = PrimitiveBuffers.allocDoublePow2(need);
        int n = aReal.length;

        for (int i = 0; i <= rA; i++) {
            int x = DigitUtils.mod(a[i], mod);
            aReal[i] = x & ((1 << 15) - 1);
            aImag[i] = x >> 15;
        }
        FastFourierTransform.fft(new double[][]{aReal, aImag}, false);

        double[] bReal = PrimitiveBuffers.allocDoublePow2(aReal, aReal.length);
        double[] bImag = PrimitiveBuffers.allocDoublePow2(aImag, bReal.length);


        for (int i = 0, j = 0; i <= j; i++, j = n - i) {
            double ari = aReal[i];
            double aii = aImag[i];
            double bri = bReal[i];
            double bii = bImag[i];
            double arj = aReal[j];
            double aij = aImag[j];
            double brj = bReal[j];
            double bij = bImag[j];

            double a1r = (ari + arj) / 2;
            double a1i = (aii - aij) / 2;
            double a2r = (aii + aij) / 2;
            double a2i = (arj - ari) / 2;

            double b1r = (bri + brj) / 2;
            double b1i = (bii - bij) / 2;
            double b2r = (bii + bij) / 2;
            double b2i = (brj - bri) / 2;

            aReal[i] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
            aImag[i] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
            bReal[i] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
            bImag[i] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;

            if (i != j) {
                a1r = (arj + ari) / 2;
                a1i = (aij - aii) / 2;
                a2r = (aij + aii) / 2;
                a2i = (ari - arj) / 2;

                b1r = (brj + bri) / 2;
                b1i = (bij - bii) / 2;
                b2r = (bij + bii) / 2;
                b2i = (bri - brj) / 2;

                aReal[j] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                aImag[j] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                bReal[j] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                bImag[j] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;
            }
        }

        FastFourierTransform.fft(new double[][]{aReal, aImag}, true);
        FastFourierTransform.fft(new double[][]{bReal, bImag}, true);

        int[] ans = PrimitiveBuffers.allocIntPow2(need);
        for (int i = 0; i < need; i++) {
            long aa = DigitUtils.mod(Math.round(aReal[i]), mod);
            long bb = DigitUtils.mod(Math.round(bReal[i]), mod);
            long cc = DigitUtils.mod(Math.round(aImag[i]), mod);
            ans[i] = DigitUtils.mod(aa + (bb << 15) + (cc << 30), mod);
        }

        PrimitiveBuffers.release(aReal, bReal, aImag, bImag);
        return ans;
    }

    private int[] multiplyMod(int[] a, int[] b) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        if (Math.min(rA, rB) < FFT_THRESHOLD) {
            return mulBF(a, b);
        }

        int need = rA + rB + 1;

        double[] aReal = PrimitiveBuffers.allocDoublePow2(need);
        double[] aImag = PrimitiveBuffers.allocDoublePow2(need);
        int n = aReal.length;

        for (int i = 0; i <= rA; i++) {
            int x = DigitUtils.mod(a[i], mod);
            aReal[i] = x & ((1 << 15) - 1);
            aImag[i] = x >> 15;
        }
        FastFourierTransform.fft(new double[][]{aReal, aImag}, false);

        double[] bReal = PrimitiveBuffers.allocDoublePow2(need);
        double[] bImag = PrimitiveBuffers.allocDoublePow2(need);
        for (int i = 0; i <= rB; i++) {
            int x = DigitUtils.mod(b[i], mod);
            bReal[i] = x & ((1 << 15) - 1);
            bImag[i] = x >> 15;
        }
        FastFourierTransform.fft(new double[][]{bReal, bImag}, false);


        for (int i = 0, j = 0; i <= j; i++, j = n - i) {
            double ari = aReal[i];
            double aii = aImag[i];
            double bri = bReal[i];
            double bii = bImag[i];
            double arj = aReal[j];
            double aij = aImag[j];
            double brj = bReal[j];
            double bij = bImag[j];

            double a1r = (ari + arj) / 2;
            double a1i = (aii - aij) / 2;
            double a2r = (aii + aij) / 2;
            double a2i = (arj - ari) / 2;

            double b1r = (bri + brj) / 2;
            double b1i = (bii - bij) / 2;
            double b2r = (bii + bij) / 2;
            double b2i = (brj - bri) / 2;

            aReal[i] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
            aImag[i] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
            bReal[i] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
            bImag[i] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;

            if (i != j) {
                a1r = (arj + ari) / 2;
                a1i = (aij - aii) / 2;
                a2r = (aij + aii) / 2;
                a2i = (ari - arj) / 2;

                b1r = (brj + bri) / 2;
                b1i = (bij - bii) / 2;
                b2r = (bij + bii) / 2;
                b2i = (bri - brj) / 2;

                aReal[j] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                aImag[j] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                bReal[j] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                bImag[j] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;
            }
        }

        FastFourierTransform.fft(new double[][]{aReal, aImag}, true);
        FastFourierTransform.fft(new double[][]{bReal, bImag}, true);

        int[] ans = PrimitiveBuffers.allocIntPow2(need);
        for (int i = 0; i < need; i++) {
            long aa = DigitUtils.mod(Math.round(aReal[i]), mod);
            long bb = DigitUtils.mod(Math.round(bReal[i]), mod);
            long cc = DigitUtils.mod(Math.round(aImag[i]), mod);
            ans[i] = DigitUtils.mod(aa + (bb << 15) + (cc << 30), mod);
        }

        PrimitiveBuffers.release(aReal, bReal, aImag, bImag);
        return ans;
    }

    /**
     * <p>
     * calc a = b * c + remainder, m >= 1 + ceil(log_2 max(|a|, |b|))
     * </p>
     * <p>
     * ret[0] for c and ret[1] for remainder
     * </p>
     */
    @Override
    public int[][] divideAndRemainder(int[] a, int[] b) {
        int rankA = rankOf(a);
        int rankB = rankOf(b);
        int rankC = rankA - rankB;

        if (rankC < 0) {
            int[][] ans = new int[2][];
            ans[0] = PrimitiveBuffers.allocIntPow2(1);
            ans[1] = PrimitiveBuffers.allocIntPow2(a, rankA + 1);
            return ans;
        }

        if (rankB < FFT_DIVIDE_THRESHOLD || rankC < FFT_DIVIDE_THRESHOLD) {
            return super.divideAndRemainder(a, b);
        }

        int[] quotient = divide(a, b);
        int[] prod = convolution(b, quotient);
        int[] remainder = PrimitiveBuffers.replace(subtract(a, prod), prod);
        remainder = PrimitiveBuffers.replace(module(remainder, rankB), remainder);
        remainder = Polynomials.normalizeAndReplace(remainder);
        return new int[][]{quotient, remainder};
    }
}
