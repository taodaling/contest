package template.geometry.geo2;

import template.utils.GeoConstant;

import java.util.Comparator;

public class IntegerLine2 {
    /**
     * direction of line
     */
    public IntegerPoint2 vec;
    public long c;

    public IntegerLine2(IntegerPoint2 vec, long c) {
        this.vec = vec;
        this.c = c;
    }

    public IntegerPoint2 anyPoint() {
        long a = -vec.y;
        long b = vec.x;
        if (GeoConstant.sign(a) != 0) {
            return new IntegerPoint2(c / a, 0);
        } else {
            return new IntegerPoint2(0, c / b);
        }
    }

    /**
     * ax + by = c
     */
    public IntegerLine2(long a, long b, long c) {
        this(new IntegerPoint2(b, -a), c);
    }

    public IntegerLine2(IntegerPoint2 a, IntegerPoint2 b) {
        this.vec = IntegerPoint2.minus(b, a);
        this.c = IntegerPoint2.cross(vec, a);
    }

    private long side0(IntegerPoint2 pt) {
        return IntegerPoint2.cross(vec, pt) - c;
    }

    /**
     * 如果pt在逆时针方向，返回1，顺时针方向返回-1，否则返回0
     */
    public int side(IntegerPoint2 pt) {
        return GeoConstant.sign(side0(pt));
    }

    public double dist(IntegerPoint2 pt) {
        return Math.abs(side0(pt)) / vec.abs();
    }

    public long squareDist(IntegerPoint2 pt) {
        long side = IntegerPoint2.cross(vec, pt) - c;
        return side * side / vec.square();
    }

    public IntegerLine2 perpendicularThrough(IntegerPoint2 pt) {
        return new IntegerLine2(pt, IntegerPoint2.plus(pt, vec.perpendicular()));
    }

    /**
     * 判断一条平行的直线处于当前的上方还是下方，上方为1，下方为-1，重合为0
     */
    public int side(IntegerLine2 line) {
        //return GeoConstant.compare(line.c, c);
        return side(line.anyPoint());
    }

    public static Comparator<IntegerPoint2> sortPointOnLine(IntegerLine2 line) {
        return new Comparator<IntegerPoint2>() {
            @Override
            public int compare(IntegerPoint2 a, IntegerPoint2 b) {
                return Double.compare(IntegerPoint2.dot(a, line.vec), IntegerPoint2.dot(b, line.vec));
            }
        };
    }

    public IntegerLine2 translate(IntegerPoint2 pt) {
        return new IntegerLine2(vec, c + IntegerPoint2.cross(vec, pt));
    }


    public IntegerPoint2 projection(IntegerPoint2 pt) {
        long factor = side0(pt) / vec.square();
        return new IntegerPoint2(pt.x - (-vec.y * factor), pt.y - vec.x * factor);
        //return IntegerPoint2.minus(pt, IntegerPoint2.mul(vec.perpendicular(), side0(pt) / vec.square()));
    }

    public IntegerPoint2 reflect(IntegerPoint2 pt) {
        return IntegerPoint2.minus(pt, IntegerPoint2.mul(vec.perpendicular(), 2 * side0(pt) / vec.square()));
    }

    public Comparator<IntegerPoint2> sortPointAlongLine() {
        return (a, b) -> GeoConstant.compare(IntegerPoint2.dot(vec, a), IntegerPoint2.dot(vec, b));
    }

    @Override
    public String toString() {
        return -vec.y + "x + " + vec.x + "y = " + c;
    }

    public static IntegerLine2[] asLinePolygon(IntegerPoint2[] pts){
        int n = pts.length;
        IntegerLine2[] ans = new IntegerLine2[n];
        for(int i = 0; i < n; i++){
            ans[i] = new IntegerLine2(pts[i], pts[(i + 1) % n]);
        }
        return ans;
    }
}
