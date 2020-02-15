#include "../../libs/common.h"
#include "../../libs/pollard_rho.h"

void solve(int testId, istream &in, ostream &out)
{
//assert(miller_rabin::MillerRabin(13, 3));

    ll n;
    in >> n;
    error(n);
    set<ll> factors;
    pollard_rho::FindAllPrimeFactors(factors, n);
    ll mx = *factors.rbegin();
    if(mx == n){
        out << "Prime" << endl;
    }else{
        out << mx << endl;
    }
}

#include "../../libs/run_multi.h"