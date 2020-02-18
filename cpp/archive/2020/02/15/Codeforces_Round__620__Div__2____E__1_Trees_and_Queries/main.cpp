#include "../../libs/common.h"
#include "../../libs/lca.h"
using tree_lca::Lca;

vector<vector<int>> edges;
vector<int> parents;
vector<int> depthes;
Lca lca;

void dfs(int root, int p, int depth)
{
    depthes[root] = depth;
    parents[root] = p;
    for (int node : edges[root])
    {
        if (node == p)
        {
            continue;
        }
        dfs(node, root, depth + 1);
    }
}

int distBetween(int u, int v)
{
    int l = lca.lca(u, v);
    return depthes[u] + depthes[v] - 2 * depthes[l];
}

bool under(int a, int b)
{
    return lca.lca(a, b) == b;
}

bool intersect(int a, int b, int x, int y)
{
    int lab = lca.lca(a, b);
    int lxy = lca.lca(x, y);
    return (under(x, lab) || under(y, lab)) &&
           (under(a, lxy) || under(b, lxy));
}

int extraDist(int a, int b, int x, int y){
    int lab = lca.lca(a, b);
    int lxy = lca.lca(x, y);
    int lall = lca.lca(lab, lxy);
    if(lall != lab && lall != lxy){
        return distBetween(lab, lxy);
    }
    if(lall == lab){
        swap(a, x);
        swap(b, y);
        swap(lab, lxy);
    }
    int lx = lca.lca(lab, x);
    int ly = lca.lca(lab, y);
    if(lx != lxy){
        return distBetween(lab, lx);
    }
    return distBetween(lab, ly);
}

void solve(int testId, istream &in, ostream &out)
{
    int n;
    in >> n;
    edges.resize(n);
    parents.resize(n);
    depthes.resize(n);
    for (int i = 1; i < n; i++)
    {
        int a, b;
        in >> a >> b;
        a--;
        b--;
        edges[a].push_back(b);
        edges[b].push_back(a);
    }

    dfs(0, -1, 0);
    lca.init_lca(edges, 0);

    int q;
    in >> q;
    for (int i = 0; i < q; i++)
    {
        int x, y, a, b, k;
        in >> x >> y >> a >> b >> k;
        x--;
        y--;
        a--;
        b--;
        int circle = distBetween(x, y) + 1;

        int choice = distBetween(x, a) + distBetween(b, y) + 1;
        if (choice <= k)
        {
            if ((k - choice) % 2 == 0 ||
                circle % 2 == 1 && k - choice >= circle)
            {
                out << "YES" << endl;
                continue;
            }
        }
        choice = distBetween(x, b) + distBetween(y, a) + 1;
        if (choice <= k)
        {
            if ((k - choice) % 2 == 0 ||
                circle % 2 == 1 && k - choice >= circle)
            {
                out << "YES" << endl;
                continue;
            }
        }

        bool inter = intersect(x, y, a, b);
        choice = distBetween(a, b);
        if (choice <= k)
        {
            int plus = 0;
            if (!inter)
            {
                plus = 2 * extraDist(a, b, x, y);
            }
            if ((k - choice) % 2 == 0 ||
                circle % 2 == 1 && k - choice >= circle + plus)
            {
                out << "YES" << endl;
                continue;
            }
        }

        out << "NO" << endl;
    }
}

#include "../../libs/run_once.h"