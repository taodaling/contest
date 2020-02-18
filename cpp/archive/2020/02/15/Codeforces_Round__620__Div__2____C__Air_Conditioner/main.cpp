#include "../../libs/common.h"


void solve(int testId, istream &in, ostream &out)
{
    int n, m;
    in >> n >> m;
    pair<ll, ll> range = mp(m, m);
    int now = 0;
    bool valid = true;
    for(int i = 0; i < n; i++){
        int t;
        ll l, r;
        in >> t >> l >> r;
        range.first -= t - now;
        range.second += t - now;
        now = t;
        range.first = max(range.first, l);
        range.second = min(range.second, r);
        if(range.first > range.second){
            valid = false;
        }
    }
    out << (valid ? "YES" : "NO") << endl;
}

#include "../../libs/run_multi.h"