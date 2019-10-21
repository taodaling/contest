package template;

public class DigitUtils {
    private DigitUtils() {}

    public static long asLong(int high, int low) {
        return (((long) high) << 32) | low;
    }

    public static int highBit(long x) {
        return (int) (x >> 32);
    }

    public static int lowBit(long x) {
        return (int) x;
    }

    public static boolean isOdd(int x) {
        return (x & 1) == 1;
    }

    public static boolean isOdd(long x) {
        return (x & 1) == 1;
    }

    public static boolean isEven(int x) {
        return (x & 1) == 0;
    }

    public static boolean isEven(long x) {
        return (x & 1) == 0;
    }

    public static int mod(long x, int mod){
        x %= mod;
        if(x < 0){
            x += mod;
        }
        return (int)x;
    }

    public static int mod(int x, int mod){
        x %= mod;
        if(x < 0){
            x += mod;
        }
        return x;
    }

    public static long mod(long x, long mod){
        x %= mod;
        if(x < 0){
            x += mod;
        }
        return x;
    }
}
