package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Polynomial {
    private double[] coes;

    public Polynomial(int n) {
        this.coes = new double[n + 1];
    }

    public Polynomial(double[] coes) {
        this.coes = coes;
    }

    public double coefficientOf(int i) {
        return i >= coes.length ? 0 : coes[i];
    }

    public double y(double x) {
        double xp = 1;
        double sum = 0;
        for (double c : coes) {
            sum += xp * c;
            xp *= x;
        }
        return sum;
    }

    public static PolynomialBuilder newBuilder(){
        return new PolynomialBuilder();
    }

    public static class PolynomialBuilder {
        Map<Double, Double> points = new HashMap<>();

        public PolynomialBuilder addPoint(double x, double y) {
            if (points.containsKey(x)) {
                if (points.get(x) != y) {
                    throw new RuntimeException();
                }
                return this;
            }
            points.put(x, y);
            return this;
        }

        public Polynomial build() {
            if (points.size() == 0) {
                throw new RuntimeException();
            }
            int n = points.size();
            Matrix mat = new Matrix(n, n);
            Matrix vec = new Matrix(n, 1);
            List<Map.Entry<Double, Double>> entries = new ArrayList(points.entrySet());
            for (int i = 0; i < n; i++) {
                double xp = 1;
                double x = entries.get(i).getKey();
                double y = entries.get(i).getValue();
                for (int j = 0; j < n; j++, xp *= x) {
                    mat.mat[i][j] = xp;
                }
                vec.mat[i][0] = y;
            }
            Matrix ans = Matrix.mul(Matrix.inverse(mat), vec);
            double[] coes = new double[n];
            for (int i = 0; i < n; i++) {
                coes[i] = ans.mat[i][0];
            }
            return new Polynomial(coes);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("y=");
        for (int i = 0; i < coes.length; i++) {
            builder.append(coes[i]).append("x^").append(i).append("+");
        }
        if (builder.length() > 0 && builder.charAt(builder.length() - 1) == '+') {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
