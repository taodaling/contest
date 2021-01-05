package test;

import com.sun.org.apache.bcel.internal.generic.FSUB;
import template.binary.Bits;
import template.math.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        double[][] mat = new double[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                mat[i][(j + i) % 5] = 1;
            }
        }
        for (int i = 1; i < 1 << 5; i++) {
            for (int j = 1; j < 1 << 5; j++) {
                if (Integer.bitCount(i) == Integer.bitCount(j)) {
                    double[][] sub = submat(mat, i, j);
                    Matrix matrix = new Matrix(sub);
                    double dm = Matrix.determinant(matrix);
                    if(!(near(dm, -1) || near(dm, 0) || near(dm, 1))){
                        System.err.println(Integer.toBinaryString(i));
                        System.err.println(Integer.toBinaryString(j));
                        throw new RuntimeException();
                    }
                }
            }
        }
    }

    public static boolean near(double a, double b) {
        return Math.abs(a - b) <= 1e-10;
    }

    public static double[][] submat(double[][] mat, int row, int col) {
        double[] buf = new double[5];
        List<double[]> sub = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            if (Bits.get(row, i) == 0) {
                continue;
            }
            int wpos = 0;
            for (int j = 0; j < 5; j++) {
                if (Bits.get(col, j) == 1) {
                    buf[wpos++] = mat[i][j];
                }
            }
            sub.add(Arrays.copyOf(buf, wpos));
        }
        return sub.stream().toArray(i -> new double[i][]);
    }
}
