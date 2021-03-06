package contest;

import template.*;

public class BZOJ4162 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        byte[] data = new byte[20000];
        int len = in.readString(data, 0);
        for(int i = 0; i < len; i++){
            data[i] = (byte) (data[i] - '0');
        }
        ByteList n = new ByteList();
        n.addAll(data, 0, len);
        int k = in.readInt();
        int[][] matArray = new int[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                matArray[i][j] = in.readInt();
            }
        }

        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);

        ModMatrix matrix = new ModMatrix(matArray);
        GravityModLagrangeInterpolation.Polynomial p = matrix.getCharacteristicPolynomial(pow);
        IntegerList pList = new IntegerList();
        pList.addAll(p.toArray());
        IntegerList remainder = new IntegerList();

        Polynomials.module(n, pList, remainder, pow);
        Polynomials.normalize(remainder);
        ModMatrix m = new ModMatrix(k, k);
        m.asStandard(mod);
        ModMatrix ans = new ModMatrix(k, k);
        for (int i = 0; i < remainder.size(); i++) {
            int c = remainder.get(i);
            ans = ModMatrix.plus(ans, ModMatrix.mul(m, c, mod), mod);
            m = ModMatrix.mul(m, matrix, mod);
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                out.append(ans.get(i, j)).append(' ');
            }
            out.println();
        }
    }
}
