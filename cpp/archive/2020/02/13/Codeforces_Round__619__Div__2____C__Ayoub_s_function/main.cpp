#include "../../libs/common.h"

ll pick(ll n){
    return n * (n - 1) / 2 + n;
}

void solve(int testId, istream &in, ostream &out)
{
    ll n, m;
    in >> n >> m;
    ll blank = n - m;
    ll avg = blank / (m + 1);
    ll gCnt = blank - (m + 1) * avg;
    ll lCnt = m + 1 - gCnt;
    ll ans = pick(n) - lCnt * pick(avg) - gCnt * pick(avg + 1);
    out << ans << endl;
}

#include "../../libs/run_multi.h"