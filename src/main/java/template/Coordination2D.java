package template;

public class Coordination2D {
    private Matrix mat;
    private Matrix inv;

    private static final double EPS = 1e-8;


    private static boolean near(double x, double y) {
        return Math.abs(x - y) <= EPS;
    }

    private Coordination2D(Matrix mat) {
        this.mat = mat;
        this.inv = Matrix.inverse(mat);
    }

    //set x coordination
    public static Coordination2D ofXAxis(double x, double y) {
        if (near(x * x + y * y, 1)) {
            double d = Math.sqrt(x * x + y * y);
            x /= d;
            y /= d;
        }
        Matrix mat = new Matrix(3, 3);
        mat.asStandard();
        mat.set(0, 0, x);
        mat.set(1, 0, y);
        mat.set(0, 1, -y);
        mat.set(1, 1, x);
        return new Coordination2D(mat);
    }

    public static Coordination2D ofOrigin(double x, double y) {
        Matrix mat = new Matrix(3, 3);
        mat.asStandard();
        mat.set(0, 2, x);
        mat.set(1, 2, y);
        return new Coordination2D(mat);
    }

    //As a * b
    public static Coordination2D merge(Coordination2D a, Coordination2D b) {
        return new Coordination2D(Matrix.mul(b.mat, a.mat));
    }

    public void toNormalCoordination(Matrix vec, Matrix ans) {
        Matrix.mul(mat, vec, ans);
    }

    public void toCurrentCoordination(Matrix vec, Matrix ans) {
        Matrix.mul(inv, vec, ans);
    }
}