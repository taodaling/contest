#include "../../libs/common.h"
#include "../../libs/tree.h"

void solve(int testId, istream &in, ostream &out)
{
    int n, q; 
    in >> n >> q;
    vector<vector<int>> edges(n);
    for(int i = 0; i < n - 1; i++){
        int a, b;
        in >> a >> b;
        a--;
        b--;
        edges[a].push_back(b);
        edges[b].push_back(a);
    }
    tree::Lca lca;
    lca.init(edges, 0);
    for(int i = 0; i < q; i++){
        int a, b, c, d;
        in >> a >> b >> c >> d;
        a--;
        b--;
        c--;
        d--;
        bool ans = tree::Intersect(mp(a, b), mp(c, d), lca);
        out << (ans ? "Y" : "N") << endl;
    }
}

#include "../../libs/run_once.h"