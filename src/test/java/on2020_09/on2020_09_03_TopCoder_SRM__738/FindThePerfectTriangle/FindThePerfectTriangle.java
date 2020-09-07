package on2020_09.on2020_09_03_TopCoder_SRM__738.FindThePerfectTriangle;



public class FindThePerfectTriangle {
    public int[] constructTriangle(int area, int perimeter) {
        int[] possible = new int[2];
        for (int x = 0; x <= perimeter / 3; x++) {
            for (int y = 1; y <= perimeter / 3; y++) {
                double A = dist(x, y);
                if (Math.round(A * 3) > perimeter) {
                    continue;
                }
                //|xb-ya|=2area
                for (int b = -perimeter / 2; b <= perimeter / 2; b++) {
                    //xb-ya=2area
                    int a1 = (x * b - 2 * area) / y;
                    int a2 = (x * b + 2 * area) / y;
                    possible[0] = a1;
                    possible[1] = a2;
                    for (int a : possible) {
                        if (Math.abs(x * b - y * a) != 2 * area) {
                            continue;
                        }
                        //check dist
                        double total = dist(a, b) + dist(a - x, b - y) + A;
                        if (Math.abs(total - perimeter) <= 1e-10) {
                            int[] ans = new int[]{0, 0, x, y, a, b};
                            for (int i = 0; i < 6; i++) {
                                ans[i] += 1000;
                            }
                            return ans;
                        }
                    }
                }
            }
        }
        return new int[0];
    }

    public double dist(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
}
