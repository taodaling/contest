#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out)
{
    int n, g, b;
    in >> n >> g >> b;

    ll l = 0;
    ll r = 1e18;
    while(l < r){
        ll m = (l + r) >> 1;
        ll good = m / (b + g) * g;
        good += min<ll>(m % (b + g), g);
        ll bad = m - good;
        bool pass = (good + min(bad, good)) >= n;
        if(pass){
            r = m;
        }else{
            l = m + 1;
        }
    }

    out << l << endl;
}

#include "../../libs/run_multi.h"