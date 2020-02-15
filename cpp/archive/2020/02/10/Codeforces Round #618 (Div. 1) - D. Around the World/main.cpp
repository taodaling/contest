#include "../../libs/common.h"
#include "../../libs/modular.h"
#include "../../libs/bits.h"
#include "../../libs/hash.h"

using Mint = modular::Modular<int, (int)1e9 + 7>;
using namespace bits;

vector<ui> allSubgroup;
vector<vector<ui>> subGroupPlusSingle;
vector<vector<ui>> subGroupPlusSet;
unordered_map<ui, int, hash::custom_hash> groupToIndex;

ui expand(int x, vector<int> &vec, int i){
    if(i < 0){
        return ui(1) << x;
    }
    return expand(x, vec, i - 1) | expand(x ^ vec[i], vec, i - 1);
}

void search(vector<int>& vec, int i){
    if(i == 0){
        int mask = expand(0, vec, vec.size() - 1);
        if(groupToIndex.find(mask) == groupToIndex.end()){
            int index = groupToIndex.size();
            groupToIndex[mask] = 1;
        }
        return;
    }
    for(int j = 0; j < 32; j++){
        vec.push_back(j);
        search(vec, i - 1);
        vec.pop_back();
    }
}

int add(ui x, int i)
{
    ui t = SetBit(x, i);
    for (int j = 0; ; j++)
    {
        if ((allSubgroup[j] & t) == t)
        {
            return j;
        }
    }
}

int merge(int a, int b)
{
    for (int i = 0; i < 32; i++)
    {
        if (BitAt(allSubgroup[b], i))
        {
            a = subGroupPlusSingle[a][i];
        }
    }
    return a;
}

void generate()
{
    for(int i = 0; i <= 4; i++){
        vector<int> record;
        search(record, i);
    }
    for(auto &p : groupToIndex){
        allSubgroup.push_back(p.first);
    }
    allSubgroup.push_back(~0);

    error(allSubgroup.size());
    sort(allSubgroup.begin(), allSubgroup.end(), [](int a, int b) { return BitCount((ui)a) < BitCount((ui)b); });

    subGroupPlusSingle.resize(allSubgroup.size(), vector<ui>(32));
    for (int i = 0; i < allSubgroup.size(); i++)
    {
        for (int j = 0; j < 32; j++)
        {
            subGroupPlusSingle[i][j] = add(allSubgroup[i], j);
        }
    }

    subGroupPlusSet.resize(allSubgroup.size(), vector<ui>(allSubgroup.size()));
    for (int i = 0; i < allSubgroup.size(); i++)
    {
        for (int j = 0; j < allSubgroup.size(); j++)
        {
            subGroupPlusSet[i][j] = merge(i, j);
        }
    }
}

struct edge
{
    int to;
    int w;
};

struct node
{
    bool isNear;
    bool visited;
    bool instk;
    ui w;
    int partner;
    ui partnerW;
    ui nearW;
};

vector<vector<edge>> edges;
vector<node> nodes;

void findLoop(ui x, ui &mask, bool &zero)
{
    if ((subGroupPlusSingle[mask][x] == mask) || x == 0)
    {
        zero = true;
    }
    mask = subGroupPlusSingle[mask][x];
}

void detect(int root, int p, ui w, ui &mask, bool &zero)
{
    if (root == 1)
    {
        return;
    }
    if (nodes[root].visited)
    {
        if (nodes[root].instk)
        {
            ui x = w ^ nodes[root].w;
            findLoop(x, mask, zero);
        }
        return;
    }
    nodes[root].visited = true;
    nodes[root].instk = true;
    nodes[root].w = w;
    for (auto &e : edges[root])
    {
        if (e.to == p)
        {
            continue;
        }
        if(e.to == root) {
            findLoop(e.w, mask, zero);
            continue;
        }
        detect(e.to, root, w ^ e.w, mask, zero);
    }
    nodes[root].instk = false;
}

void solve(int testId, istream &in, ostream &out)
{
    generate();
    int n, m;
    in >> n >> m;
    edges.resize(n + 1);
    nodes.resize(n + 1);
    for (int i = 0; i < m; i++)
    {
        int a, b, w;
        in >> a >> b >> w;
        edges[a].push_back({b, w});
        edges[b].push_back({a, w});
    }
    for (auto &e : edges[1])
    {
        nodes[e.to].isNear = true;
        nodes[e.to].nearW = e.w;
    }
    for(int i = 1; i <= n; i++){
        if(!nodes[i].isNear){
            continue;
        }
        for(auto &e : edges[i]){
            if(nodes[e.to].isNear && e.to != i){
                nodes[i].partner = e.to;
                nodes[i].partnerW = e.w;
            }
        }
    }

    int k = allSubgroup.size();
    vector<Mint> dp(k);
    vector<Mint> last(k);
    last[0] = 1;
    for(auto &e : edges[1]){
        int i = e.to;
        if(nodes[i].visited){
            continue;
        }
        ui mask = 0;
        bool zero = false;
        detect(i, 1, 0, mask, zero);
        if(zero){
            continue;
        }
        dp = last;
        if(nodes[i].partner == 0){
            //add
            for(int i = 0; i < k; i++){
                if((allSubgroup[i] & allSubgroup[mask]) > 1)
                {
                    continue;
                }
                dp[subGroupPlusSet[i][mask]] += last[i];
            }
        }else{
            //add one
            for(int i = 0; i < k; i++){
                if((allSubgroup[i] & allSubgroup[mask]) > 1)
                {
                    continue;
                }
                dp[subGroupPlusSet[i][mask]] += 2 * last[i];
            }
            //add two
            int circle = nodes[i].nearW ^ nodes[nodes[i].partner].nearW ^ nodes[i].partnerW;
            if(!(subGroupPlusSingle[mask][circle] == mask)){
                mask = subGroupPlusSingle[mask][circle];
                for(int i = 0; i < k; i++){
                   if((allSubgroup[i] & allSubgroup[mask]) > 1)
                   {
                        continue;
                    }
                    dp[subGroupPlusSet[i][mask]] += last[i];
                }
            }
        }
        dp.swap(last);
    }

    Mint ans = 0;
    for(int i = 0; i < k; i++){
        ans += last[i];
    }
    out << ans;
}

#include "../../libs/run_once.h"