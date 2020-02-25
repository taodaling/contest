package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntegerModValue;
import template.math.IntegerModValueImpl;
import template.math.IntegerModValueUtils;

public class P5091 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        //try {

            int a = in.readInt();
            int m = in.readInt();
            byte[] b = new byte[20000000];
            int n = in.readString(b, 0);
            IntegerModValue base = new IntegerModValueImpl(a);
            IntegerModValue exp = new IntegerModValue() {
                @Override
                public int get(int mod) {
                    long ans = 0;
                    for (int i = 0; i < n; i++) {
                        ans = ans * 10 + b[i] - '0';
                        if(ans >= (long)1e17){
                            ans %= mod;
                        }
                    }
                    return (int) (ans % mod);
                }

                @Override
                public boolean isZero() {
                    for (int i = 0; i < n; i++) {
                        if (b[i] != '0') {
                            return false;
                        }
                    }
                    return true;
                }

                @Override
                public int limit(int limit) {
                    long ans = 0;
                    for (int i = 0; i < n && ans < limit; i++) {
                        ans = ans * 10 + b[i] - '0';
                        ans = Math.min(ans, limit);
                    }
                    return (int) ans;
                }
            };

            int ans = IntegerModValueUtils.wrapExponentValue(base, exp).get(m);
            out.println(ans);
//        } catch (ArithmeticException e) {
//        } catch (StackOverflowError e) {
//        } catch (IndexOutOfBoundsException e) {
//        } catch (OutOfMemoryError e){}
    }
}
