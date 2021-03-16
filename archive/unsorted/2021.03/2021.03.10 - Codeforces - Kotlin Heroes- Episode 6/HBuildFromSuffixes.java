package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Sum;
import template.utils.Update;

public class HBuildFromSuffixes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n - 1);
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 2, SumImpl::new, UpdateImpl::new, i -> {
            SumImpl ans = new SumImpl();
            ans.start = ans.end = a[i];
            for (int j = 0; j < 4; j++) {
                ans.state[j][j] = 1;
            }
            return ans;
        });
        int mod = 998244353;
        for (int i = 0; i < q; i++) {
            int x = in.ri() - 1;
            a[x] ^= 1;
            UpdateImpl upd = new UpdateImpl();
            upd.x = a[x];
            st.update(x, x, 0, n - 2, upd);
            SumImpl whole = st.sum;
            long ans = 0;
            if(whole.end == 0){
                for(int j = 0; j < 4; j++){
                    for(int k = 0; k < 4; k++){
                        ans += whole.state[j][k] * (k + 1);
                    }
                }
            }else{
                for(int j = 0; j < 4; j++){
                    for(int k = 0; k < 4; k++){
                        ans += whole.state[j][k] * (3 - k);
                    }
                }
            }
            ans %= mod;
            out.println(ans);
        }
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    static int mod = 998244353;
    long[][] state = new long[4][4];
    static long[][] buf = new long[4][4];
    int start;
    int end;

    @Override
    public void add(SumImpl sum) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buf[i][j] = state[i][j];
                state[i][j] = 0;
            }
        }
        if (end == 1) {

            for (int i = 0; i < 4; i++) {
                for (int j = 1; j < 4; j++) {
                    buf[i][j] += buf[i][j - 1];
                    if (buf[i][j] >= mod) {
                        buf[i][j] -= mod;
                    }
                }
            }


            if (sum.start == 1) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 4; k++) {
                            state[i][k] += buf[i][j] * sum.state[j][k] % mod;
                        }
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    for (int j = 1; j < 4; j++) {
                        for (int k = 0; k < 4; k++) {
                            state[i][k] += buf[i][j - 1] * sum.state[j][k] % mod;
                        }
                    }
                }
            }

        } else {
            for (int i = 0; i < 4; i++) {
                for (int j = 2; j >= 0; j--) {
                    buf[i][j] += buf[i][j + 1];
                    if (buf[i][j] >= mod) {
                        buf[i][j] -= mod;
                    }
                }
            }

            if (sum.start == 0) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 4; k++) {
                            state[i][k] += buf[i][j] * sum.state[j][k] % mod;
                        }
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 4; k++) {
                            state[i][k] += buf[i][j + 1] * sum.state[j][k] % mod;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] %= mod;
            }
        }

        end = sum.end;
    }

    @Override
    public void update(UpdateImpl update) {
        start = end = update.x;
    }

    @Override
    public void copy(SumImpl sum) {
        start = sum.start;
        end = sum.end;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = sum.state[i][j];
            }
        }
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int x;

    @Override
    public void update(UpdateImpl update) {
        x = update.x;
    }

    @Override
    public void clear() {
        x = -1;
    }

    @Override
    public boolean ofBoolean() {
        return x != -1;
    }
}