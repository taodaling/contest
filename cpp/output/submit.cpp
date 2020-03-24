#ifndef COMMON_H
#define COMMON_H

#include <bits/stdc++.h>
#include <algorithm>
#include <chrono>
#include <complex>
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
using std::array;
using std::bitset;
using std::cerr;
using std::complex;
using std::deque;
using std::endl;
using std::fill;
using std::function;
using std::ios_base;
using std::istream;
using std::istream_iterator;
using std::iterator;
using std::make_pair;
using std::make_tuple;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::queue;
using std::reverse;
using std::rotate;
using std::set;
using std::sort;
using std::string;
using std::stringstream;
using std::swap;
using std::uniform_int_distribution;
using std::uniform_real_distribution;
using std::unique;
using std::unordered_map;
using std::vector;
using std::tuple;
using std::get;
using std::multiset;
using std::multimap;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
typedef unsigned char byte;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

const double E = 2.7182818284590452354;
const double PI = 3.14159265358979323846;

#define mp make_pair

#ifdef LOCAL
#define PREPARE_INPUT                                  \
  {                                                    \
    std::cout << "Input file name:";                   \
    string file;                                       \
    std::cin >> file;                                  \
    if (file != "stdin") {                             \
      file = string(__FILE__) + "/../" + file + ".in"; \
      std::cout << "Open file:" << file << std::endl;  \
      freopen(file.data(), "r", stdin);                \
    }                                                  \
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

#endif

#define C0(x) memset(x, 0, sizeof(x))
#define C1(x) memset(x, -1, sizeof(x))
#ifndef PRESUM_H
#define PRESUM_H

namespace pre_sum {
template <class T>
class PreSum {
 public:
  PreSum() {}
  PreSum(const vector<T> &vals) { populate(vals); }
  void populate(const vector<T> &vals) {
    _data = vals;
    _n = _data.size();
    for (int i = 1; i < _n; i++) {
      _data[i] += _data[i - 1];
    }
  }
  T prefix(int r) {
    if (r < 0) {
      return 0;
    }
    r = min(r, _n - 1);
    return _data[r];
  }
  T suffix(int l) { return _data[_n - 1] - prefix(l - 1); }
  T interval(int l, int r) { return prefix(r) - prefix(l - 1); }

 private:
  vector<T> _data;
  int _n;
};
}  // namespace pre_sum

#endif

void solve(int testId, istream &in, ostream &out) {
  string a;
  string b;
  in >> a >> b;
  if(a.length() != b.length()){
    out << -1;
    return;
  }
  int n = a.length();
  vector<int> va(n);
  vector<int> vb(n);
  for(int i = 0; i < n; i++){
    va[i] = a[i] == '+' ? 1 : 0;
    vb[i] = b[i] == '+' ? 1 : 0;
  }
  pre_sum::PreSum<int> pa(va);
  pre_sum::PreSum<int> pb(vb);
  int ans = 0;

  if(pa.prefix(n - 1) != pb.prefix(n - 1)){
    out << -1;
    return;
  }

  for(int i = 0; i < n; i++){
    ans += abs(pa.prefix(i) - pb.prefix(i));
  }

  out << ans;
}

RUN_ONCE