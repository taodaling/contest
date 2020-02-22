#include "../../libs/common.h"
#include "../../libs/tree_path_bruteforce.h"

struct LinearFunction
{
    ll a;
    ll b;
};
tree_path_bruteforce::TreePathBF<LinearFunction, 100000 + 5> treePath;


void solve(int testId, istream &in, ostream &out)
{
    int n, q;
    in >> n >> q;
    vector<LinearFunction> funcs(n);
    for(int i = 0; i < n; i++){
        in >> funcs[i].a;
    }
    for(int i = 0; i < n; i++){
        in >> funcs[i].b;
    }
    vector<vector<int>> edges(n);
    for(int i = 1; i < n; i++){
        int u, v;
        in >> u >> v;
        u--;
        v--;
        edges[u].push_back(v);
        edges[v].push_back(u);
    }
    treePath.init(edges);
    for(int i = 0; i < n; i++){
        treePath(i, i, [&](auto &node){
            node.data = funcs[i];
        });
    }
    for(int i = 0; i < q; i++){
        int t, u, v;
        in >> t >> u >> v;
        u--;
        v--;
        if(t == 1){
            ll a, b;
            in >> a >> b;
            treePath(u, v, [&](auto &node){
                node.data.a += a;
                node.data.b += b;
            });
        }else{
            ll z;
            ll ans = -2e18;
            in >> z;
            treePath(u, v, [&](auto &node){
                ans = max(ans, node.data.a * z + node.data.b);
            });
            out << ans << endl;
        }
    }
}

RUN_ONCE