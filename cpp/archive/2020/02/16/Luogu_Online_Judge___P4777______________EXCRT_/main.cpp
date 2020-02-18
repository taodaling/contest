#include "../../libs/common.h"
#include "../../libs/extcrt.h"

void solve(int testId, istream &in, ostream &out)
{
    int n;
    in >> n;
    number_theory::ExtCRT<ll> crt;
    for(int i = 0; i < n; i++){
        ll a, b;
        in >> a >> b;
        crt.add(b, a);
    }
    out << crt();
}

#include "../../libs/run_once.h"