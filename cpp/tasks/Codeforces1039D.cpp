//https://codeforces.com/contest/1039/problem/D
#include <iostream>
#include <fstream>
#include <iomanip>
#include <bits/stdc++.h>
#include <chrono>
#include <random>
#include <ext/rope>

using __gnu_cxx::rope;
using std::bitset;
using std::cerr;
using std::deque;
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
using std::uniform_int_distribution;
using std::uniform_real_distribution;
using std::unordered_map;
using std::vector;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

#define error(args...)                           \
    {                                            \
        string _s = #args;                       \
        replace(_s.begin(), _s.end(), ',', ' '); \
        stringstream _ss(_s);                    \
        istream_iterator<string> _it(_ss);       \
        err(_it, args);                          \
    }
void err(std::istream_iterator<string> it)
{
}
template <typename T, typename... Args>
void err(std::istream_iterator<string> it, T a, Args... args)
{
    cerr << *it << " = " << a << endl;
    err(++it, args...);
}

namespace dalt
{
};
namespace other
{
};

using namespace dalt;

int count;
int k;
vector<vector<int>> edges;

int dp(int root, int p)
{
    int first = 0;
    int second = 0;
    for (int node : edges[root])
    {
        if (node == p)
        {
            continue;
        }
        int ans = dp(node, root);
        if (ans > second)
        {
            second = ans;
        }
        if (second > first)
        {
            swap(second, first);
        }
    }

    if (first + second + 1 >= k)
    {
        count++;
        return 0;
    }
    return first + 1;
}

map<int, int> cache;

int solve(int t)
{
    if(cache.find(t) == cache.end()){
        auto lb = cache.lower_bound(t);
        if(lb != cache.end()){
            int top = lb->second;

            if(lb != cache.begin())
            {
                lb--;
                int bot = lb->second;
                if(bot == top){
                    return cache[t] = bot;
                }
            }
        }

        k = t;
        count = 0;
        dp(1, 0);
        cache[t] = count;
    }
    return cache[t];
}

void solve(istream &in, ostream &out)
{
    int n;
    in >> n;
    edges.resize(n + 1);
    for (int i = 1; i < n; i++)
    {
        int a, b;
        in >> a >> b;
        edges[a].push_back(b);
        edges[b].push_back(a);
    }

    int block = min(1000, n);
    for (int i = 1; i <= block; i++)
    {
        out << solve(i) << endl;
    }

    int l, r;
    r = block;
    while (r < n)
    {
        int since = r + 1;
        l = r + 1;
        r = n;
        int val = solve(l);

        while (l < r)
        {
            int m = (l + r + 1) >> 1;
            if (solve(m) == val)
            {
                l = m;
            }
            else
            {
                r = m - 1;
            }
        }

        for (int i = since; i <= r; i++)
        {
            out << val << endl;
        }
    }
}

int main()
{
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout << std::setiosflags(std::ios::fixed);
    std::cout << std::setprecision(15);

    solve(std::cin, std::cout);

    return 0;
}
