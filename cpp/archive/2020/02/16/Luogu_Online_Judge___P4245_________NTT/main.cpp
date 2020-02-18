#include "../../libs/common.h"
#include "../../libs/bits.h"
#include "../../libs/fft.h"

using namespace fast_fourie_transform;
void solve(int testId, istream &in, ostream &out)
{
    int n, m, p;
    in >> n >> m >> p;
    n++;
    m++;
    vector<int> f(n);
    vector<int> g(m);
    for(int i = 0; i < n; i++){
        in >> f[i];
    }
    for(int i = 0; i < m; i++){
        in >> g[i];
    }
    vector<int> ans = MultiplyMod(f, g, p);
    for(int x : ans){
        out << x << ' ';
    }
}

#include "../../libs/run_once.h"