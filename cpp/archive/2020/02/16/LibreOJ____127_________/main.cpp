#include "../../libs/common.h"
#include "../../libs/push_relabel.h"

using Flow = push_relabel::HLPP<1201, ll>;

void solve(int testId, istream &in, ostream &out)
{
    int n, m, s, t;
    in >> n >> m >> s >> t;

    Flow flow;
    for(int i = 0; i < m; i++){
        int u, v, c;
        in >> u >> v >> c;
        flow.addEdge(u, v, c);
    }
    ll ans = flow.calc(s, t);
    out << ans;
}

RUN_ONCE