package template.utils;

public class SegmentUtils {
    public static boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    public static boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }
}
