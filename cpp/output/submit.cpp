#ifndef COMMON_H
#define COMMON_H

#include <bits/stdc++.h>
#include <chrono>
#include <random>
#include<ext/rope>
#ifndef COMPILER_MACRO_H
#define COMPILER_MACRO_H

#ifndef LOCAL

#pragma GCC diagnostic error "-std=c++14"
#pragma GCC target("avx")
#pragma GCC optimize(3)
#pragma GCC optimize("Ofast")
#pragma GCC target ("sse4.2")
#pragma GCC optimize("inline")

#endif

#endif 
#include <iostream>
#include <fstream>
#include <iomanip>
#include<queue>

using __gnu_cxx::rope;
using std::cerr;
using std::deque;
using std::queue;
using std::endl;
using std::fill;
using std::ios_base;
using std::istream;
using std::iterator;
using std::make_pair;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::set;
using std::sort;
using std::string;
using std::swap;
using std::unordered_map;
using std::vector;
using std::bitset;
using std::uniform_int_distribution;
using std::uniform_real_distribution;
using std::stringstream;
using std::istream_iterator;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

#define error(args...) { string _s = #args; replace(_s.begin(), _s.end(), ',', ' '); stringstream _ss(_s); istream_iterator<string> _it(_ss); err(_it, args); }
void err(std::istream_iterator<string> it) {}
template<typename T, typename... Args>
void err(std::istream_iterator<string> it, T a, Args... args) {
	cerr << *it << " = " << a << endl;
	err(++it, args...);
}

#define mp make_pair

#endif

#ifdef LOCAL
#define PREPARE_INPUT {std::cout << "Input file name:"; string file; std::cin >> file; file = string(__FILE__) + "/../" + file + ".in"; std::cout << "Open file:" << file << std::endl; freopen(file.data(),"r",stdin); }
#else
#define PREPARE_INPUT
#endif


#define RUN_ONCE \
int main()\
{\
    PREPARE_INPUT;\
    std::ios_base::sync_with_stdio(false);\
    std::cin.tie(0);\
    std::cout << std::setiosflags(std::ios::fixed);\
    std::cout << std::setprecision(15);\
    solve(1, std::cin, std::cout);\
    return 0;\
}

#define RUN_MULTI \
int main()\
{\
    PREPARE_INPUT;\
    std::ios_base::sync_with_stdio(false);\
    std::cin.tie(0);\
    std::cout << std::setiosflags(std::ios::fixed);\
    std::cout << std::setprecision(15);\
    int t;\
    std::cin >> t;\
    for (int i = 1; i <= t; i++)\
    {\
        solve(i, std::cin, std::cout);\
    }\
    return 0;\
}
#ifndef PUSH_RELABEL_H
#define PUSH_RELABEL_H



namespace push_relabel
{
template <int MAXN, class T = int>
struct HLPP
{
    const T INF = numeric_limits<T>::max();
    struct edge
    {
        int to, rev;
        T f;
    };
    int s = MAXN - 1, t = MAXN - 2;
    vector<edge> adj[MAXN];
    deque<int> lst[MAXN];
    vector<int> gap[MAXN];
    int ptr[MAXN];
    T excess[MAXN];
    int highest, height[MAXN], cnt[MAXN], work;
    void addEdge(int from, int to, int f, bool isDirected = true)
    {
        adj[from].push_back({to, adj[to].size(), f});
        adj[to].push_back({from, adj[from].size() - 1, isDirected ? 0 : f});
    }
    void updHeight(int v, int nh)
    {
        work++;
        if (height[v] != MAXN)
            cnt[height[v]]--;
        height[v] = nh;
        if (nh == MAXN)
            return;
        cnt[nh]++, highest = nh;
        gap[nh].push_back(v);
        if (excess[v] > 0)
            lst[nh].push_back(v), ptr[nh]++;
    }
    void globalRelabel()
    {
        work = 0;
        for (int i = 0; i < MAXN; i++)
        {
            height[i] = MAXN;
            cnt[i] = 0;
        }
        for (int i = 0; i <= highest; i++)
            lst[i].clear(), gap[i].clear(), ptr[i] = 0;
        height[t] = 0;
        queue<int> q({t});
        while (!q.empty())
        {
            int v = q.front();
            q.pop();
            for (auto &e : adj[v])
                if (height[e.to] == MAXN && adj[e.to][e.rev].f > 0)
                    q.push(e.to), updHeight(e.to, height[v] + 1);
            highest = height[v];
        }
    }
    void push(int v, edge &e)
    {
        if (excess[e.to] == 0)
            lst[height[e.to]].push_back(e.to), ptr[height[e.to]]++;
        T df = min(excess[v], e.f);
        e.f -= df, adj[e.to][e.rev].f += df;
        excess[v] -= df, excess[e.to] += df;
    }
    void discharge(int v)
    {
        int nh = MAXN;
        for (auto &e : adj[v])
        {
            if (e.f > 0)
            {
                if (height[v] == height[e.to] + 1)
                {
                    push(v, e);
                    if (excess[v] <= 0)
                        return;
                }
                else
                    nh = min(nh, height[e.to] + 1);
            }
        }
        if (cnt[height[v]] > 1)
            updHeight(v, nh);
        else
        {
            for (int i = height[v]; i < MAXN; i++)
            {
                for (auto j : gap[i])
                    updHeight(j, MAXN);
                gap[i].clear(), ptr[i] = 0;
            }
        }
    }
    T calc(int src, int sink, int heur_n = MAXN)
    {
        s = src;
        t = sink;
        memset(excess, 0, sizeof(excess));
        excess[s] = INF, excess[t] = -INF;
        globalRelabel();
        for (auto &e : adj[s])
            push(s, e);
        for (; highest >= 0; highest--)
        {
            // while (ptr[highest] >= 0) {
            while (!lst[highest].empty())
            {
                // int v = lst[highest][ptr[highest]];
                // cout << highest << ' ' << ptr[highest] << ' ' << lst[highest].size() << endl;
                int v = lst[highest].back();
                // ptr[highest]--;
                lst[highest].pop_back();
                discharge(v);
                if (work > 4 * heur_n)
                    globalRelabel();
            }
        }
        return excess[t] + INF;
    }
};
} // namespace push_relabel

#endif

using Flow = push_relabel::HLPP<250000 + 1, int>;

#include <iostream>
#include <fstream>
 
#include <bits/stdc++.h>
 
#define pb push_back
#define sz(v) ((int)(v).size())
#define all(v) (v).begin(),(v).end()
#define mp make_pair
 
using namespace std;
 
typedef long long int64;
typedef vector<int> vi;
typedef pair<int, int> ii;
 
class TaskF {
 public:
  void dfs(const vector<vector<int>>& edge, vector<int>& parent, vector<int>& depth, vector<int>& order, int at, int from) {
    order.pb(at);
    parent[at] = from;
    if (from < 0) {
      depth[at] = 0;
    } else {
      depth[at] = 1 + depth[from];
    }
    for (int x : edge[at]) if (x != from) {
      dfs(edge, parent, depth, order, x, at);
    }
  }
 
  void solveOne(istream &in, ostream &out) {
    int n;
    in >> n;
    vector<vector<int>> a(n);
    vector<vector<int>> b(n);
    for (int i = 0; i < n - 1; ++i) {
      int u; int v; in >> u >> v;
      --u; --v;
      a[u].pb(v);
      a[v].pb(u);
    }
    for (int i = 0; i < n - 1; ++i) {
      int u; int v; in >> u >> v;
      --u; --v;
      b[u].pb(v);
      b[v].pb(u);
    }
    vector<int> parent(n);
    vector<int> depth(n);
    vector<int> order;
    dfs(a, parent, depth, order, 0, -1);
    vector<vector<int>> jump(n);
    for (int i : order) {
      if (parent[i] < 0) continue;
      jump[i].pb(parent[i]);
      while (jump[i].size() <= jump[jump[i].back()].size()) {
        jump[i].pb(jump[jump[i].back()][jump[i].size() - 1]);
      }
    }
    vector<int> jumpOffset(n);
    int totalV = n + 1;
    for (int i = 0; i < n; ++i) {
      jumpOffset[i] = totalV;
      totalV += jump[i].size();
    }
    
    Flow flow;
    int s = 0;
    int t = 1;
    map<int, ii> sedges;
    for (int i = 0; i < n; ++i) {
      if (parent[i] < 0) continue;
      flow.addEdge(jumpOffset[i], t, 1);
      sedges[jumpOffset[i]] = ii(i, parent[i]);
      for (int j = 1; j < jump[i].size(); ++j) {
        flow.addEdge(jumpOffset[i] + j, jumpOffset[i] + j - 1, n);
        flow.addEdge(jumpOffset[i] + j, jumpOffset[jump[i][j - 1]] + j - 1, n);
      }
    }
    int edgePtr = 2;
    vector<ii> edges;
    for (int u = 0; u < n; ++u) {
      for (int v : b[u]) {
        if (u > v) continue;
        flow.addEdge(s, edgePtr, 1);
        edges.emplace_back(u, v);
        int pu = u;
        int pv = v;
        for (int i = (int) jump[pu].size() - 1; i >= 0; --i) {
          if (depth[pu] - (1 << i) >= depth[pv]) {
            flow.addEdge(edgePtr, jumpOffset[pu] + i, 1);
            pu = jump[pu][i];
          }
        }
        for (int i = (int) jump[pv].size() - 1; i >= 0; --i) {
          if (depth[pv] - (1 << i) >= depth[pu]) {
            flow.addEdge(edgePtr, jumpOffset[pv] + i, 1);
            pv = jump[pv][i];
          }
        }
        assert(depth[pu] == depth[pv]);
        for (int i = jump[pu].size() - 1; i >= 0; --i) {
          if ((1 << i) <= depth[pu]) {
            if (jump[pu][i] != jump[pv][i]) {
              flow.addEdge(edgePtr, jumpOffset[pu] + i, 1);
              flow.addEdge(edgePtr, jumpOffset[pv] + i, 1);
              pu = jump[pu][i];
              pv = jump[pv][i];
            }
          }
        }
        if (pu != pv) {
          flow.addEdge(edgePtr, jumpOffset[pu], 1);
          flow.addEdge(edgePtr, jumpOffset[pv], 1);
          pu = parent[pu];
          pv = parent[pv];
        }
        assert(pu == pv);
        int lca = pu;
        ++edgePtr;
      }
    }
    assert(edgePtr == n + 1);
    int size = flow.calc(s, t);
    vector<int> ptr(totalV);
    out << size << "\n";
    for (int step = 0; step < size; ++step) {
      int at = s;
      int left = -1;
      int right = -1;
      while (at != t) {
        while (flow.adj[at][ptr[at]].f <= 0) {
          ++ptr[at];
          assert(ptr[at] < flow.adj[at].size());
        }
        --flow.adj[at][ptr[at]].f;
        int pat = at;
        at = flow.adj[at][ptr[at]].to;
        if (at == t) {
          right = pat;
        }
        if (pat == s) {
          left = at;
        }
      }
      assert(sedges.find(right) != sedges.end());
      out << sedges[right].first + 1 << " " << sedges[right].second + 1 << " " << edges[left - 2].first + 1 << " " << edges[left - 2].second + 1 << "\n";
    }
  }
 
  void solve(std::istream &in, std::ostream &out) {
    int nt = 1;
    for (int it = 0; it < nt; ++it) {
      solveOne(in, out);
    }
  }
};
 

void solve(int testId, istream &in, ostream &out)
{
    TaskF().solve(in, out);
}

RUN_ONCE