#ifndef COMMON_H
#define COMMON_H

#include <bits/stdc++.h>
#include <chrono>
#include <ext/rope>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <queue>
#include <random>
#ifndef COMPILER_MACRO_H
#define COMPILER_MACRO_H

#ifndef LOCAL

#pragma GCC diagnostic error "-std=c++14"
#pragma GCC target("avx")
#pragma GCC optimize(3)
#pragma GCC optimize("Ofast")
#pragma GCC target("sse4.2")
#pragma GCC optimize("inline")

#endif

#endif

using __gnu_cxx::rope;
using std::bitset;
using std::cerr;
using std::deque;
using std::endl;
using std::fill;
using std::function;
using std::ios_base;
using std::istream;
using std::istream_iterator;
using std::iterator;
using std::make_pair;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::queue;
using std::set;
using std::sort;
using std::string;
using std::stringstream;
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

#define error(args...)                       \
  {                                          \
    string _s = #args;                       \
    replace(_s.begin(), _s.end(), ',', ' '); \
    stringstream _ss(_s);                    \
    istream_iterator<string> _it(_ss);       \
    err(_it, args);                          \
  }
void err(std::istream_iterator<string> it) {}
template <typename T, typename... Args>
void err(std::istream_iterator<string> it, T a, Args... args) {
  cerr << *it << " = " << a << endl;
  err(++it, args...);
}

#define mp make_pair

#endif

#ifdef LOCAL
#define PREPARE_INPUT                                \
  {                                                  \
    std::cout << "Input file name:";                 \
    string file;                                     \
    std::cin >> file;                                \
    file = string(__FILE__) + "/../" + file + ".in"; \
    std::cout << "Open file:" << file << std::endl;  \
    freopen(file.data(), "r", stdin);                \
  }
#else
#define PREPARE_INPUT
#endif

#define RUN_ONCE                                    \
  int main() {                                      \
    PREPARE_INPUT;                                  \
    std::ios_base::sync_with_stdio(false);          \
    std::cin.tie(0);                                \
    std::cout << std::setiosflags(std::ios::fixed); \
    std::cout << std::setprecision(15);             \
    solve(1, std::cin, std::cout);                  \
    return 0;                                       \
  }

#define RUN_MULTI                                   \
  int main() {                                      \
    PREPARE_INPUT;                                  \
    std::ios_base::sync_with_stdio(false);          \
    std::cin.tie(0);                                \
    std::cout << std::setiosflags(std::ios::fixed); \
    std::cout << std::setprecision(15);             \
    int t;                                          \
    std::cin >> t;                                  \
    for (int i = 1; i <= t; i++) {                  \
      solve(i, std::cin, std::cout);                \
    }                                               \
    return 0;                                       \
  }
#ifndef DSU_H
#define DSU_H



namespace dsu {
template <int N>
class DSU {
 private:
  int p[N];
  int rank[N];

 public:
  DSU() { reset(); }
  void reset() {
    for (int i = 0; i < N; i++) {
      p[i] = i;
      rank[i] = 0;
    }
  }

  int find(int a) { return p[a] == p[p[a]] ? p[a] : (p[a] = find(p[a])); }

  void merge(int a, int b) {
    a = find(a);
    b = find(b);
    if (a == b) {
      return;
    }
    if (rank[a] == rank[b]) {
      rank[a]++;
    }
    if (rank[a] > rank[b]) {
      p[b] = a;
    } else {
      p[a] = b;
    }
  }
};
}  // namespace dsu

#endif

const int LIMIT = 1 << 18;
dsu::DSU<LIMIT> cc;
int cnts[LIMIT];

void solve(int testId, istream &in, ostream &out) {
    int n;
    in >> n;
    cnts[0]++;
    ll sum = 0;
    for(int i = 0; i < n; i++){
        int x;
        in >> x;
        cnts[x]++;
        sum -= x;
    }
    for(int i = LIMIT - 1; i >= 0; i--){
        for(int j = i; j; )
        {
            
        }
    }
}

RUN_ONCE