#include "../../libs/common.h"
#include "../../libs/bits.h"

using namespace bits;


ll log(ll x)
{
    return x == 1 ? 0 : (1 + log(x / 2));
}

void solve(int testId, istream &in, ostream &out)
{
    ll n;
    int m;
    in >> n >> m;
    vector<ll> cnts(63 + 1);
    for (int i = 0; i < m; i++)
    {
        ll x;
        in >> x;
        cnts[log(x)]++;
    }

    ll ans = 0;
    int borrow = 0;
    for (int i = 0; i < 63; i++)
    {
        ll bit = BitAt(n, i);
        if (cnts[i] >= bit + borrow)
        {
            cnts[i] -= bit + borrow;
            borrow = 0;
        }
        else
        {
            cnts[i] += 2 - bit - borrow;
            borrow = 1;
            ans++;
        }
        cnts[i + 1] += cnts[i] / 2;
    }

    if (borrow > 0)
    {
        out << -1 << endl;
    }
    else
    {
        out << ans << endl;
    }
}

#include "../../libs/run_multi.h"