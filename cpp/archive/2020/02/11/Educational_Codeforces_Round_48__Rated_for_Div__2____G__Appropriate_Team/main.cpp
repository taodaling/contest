#include "../../libs/common.h"
#include "../../libs/pollard_rho.h"
#include "../../libs/radix.h"
#include "../../libs/bits.h"
#include "../../libs/fwt.h"

using radix::Radix;
using namespace bits;

int Log(ll x, ll n)
{
    int cnt = 0;
    while (n % x == 0)
    {
        n /= x;
        cnt++;
    }
    return cnt;
}

void solve(int testId, istream &in, ostream &out)
{
    int n;
    ll x, y;
    in >> n >> x >> y;
    if (y % x != 0)
    {
        out << 0;
        return;
    }
    set<ll> factors;
    pollard_rho::FindAllPrimeFactors(factors, y);

    vector<ll> primes;
    for (ll f : factors)
    {
        if (Log(f, y) == Log(f, x))
        {
            continue;
        }
        primes.push_back(f);
        //error(f);
    }
    int k = primes.size();
    vector<int> high(k);
    vector<int> low(k);
    for (int i = 0; i < k; i++)
    {
        high[i] = Log(primes[i], y);
        low[i] = Log(primes[i], x);
    }

    vector<ll> as(n);
    vector<ll> left;
    vector<ll> right;
    left.reserve(n);
    right.reserve(n);

    for (int i = 0; i < n; i++)
    {
        in >> as[i];
        if (as[i] % x == 0)
        {
            left.push_back(as[i]);
        }
        if (y % as[i] == 0)
        {
            right.push_back(as[i]);
        }
    }

    vector<int> rightCnt(1 << k);

    for (ll r : right)
    {
        int mask = 0;
        for (int i = 0; i < k; i++)
        {
            int log = Log(primes[i], r);
            if (log != high[i])
            {
                mask = SetBit(mask, i);
            }
        }
        rightCnt[mask]++;
    }

    fwt::OrFWT(rightCnt, 0, rightCnt.size() - 1);
    // for(int i = 0; i < (1 << k); i++){
    //     assert(rightCnt[i] <= right.size());
    // }

    ll ans = 0;
    for (ll l : left)
    {
        int mask = 0;
        for (int i = 0; i < k; i++)
        {
            int log = Log(primes[i], l);
            if (log != low[i])
            {
                mask = SetBit(mask, i);
            }
        }
        int revert = ((1 << k) - 1) ^ mask;
        ans += rightCnt[revert];
    }

    out << ans;
}

#include "../../libs/run_once.h"