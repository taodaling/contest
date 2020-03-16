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

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
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
#ifndef GCD_H
#define GCD_H



namespace gcd {
template <typename T>
T Gcd0(T a, T b) {
  return b ? Gcd0(b, a % b) : a;
}

template <typename T>
T Gcd(T a, T b) {
  if (a < b) {
    swap(a, b);
  }
  return Gcd0(a, b);
}

template <typename T>
T  Extgcd0(T a, T b, T &x, T &y) {
  if (!b) {
    x = 1;
    y = 0;
    return a;
  }
  T ans = Extgcd0(b, a % b, y, x);
  y = y - x * (a / b);
  return ans;
}

/**
 * Find gcd(a, b) and expression xa+yb=g
 */
template <typename T>
T Extgcd(T a, T b, T &x, T &y) {
  if (a >= b) {
    return Extgcd0(a, b, x, y);
  }
  return Extgcd0(b, a, y, x);
}
}  // namespace gcd

#endif

void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;
  int base = k / n;
  k %= n;

  int a;
  int b;
  gcd::Extgcd(k, n, a, b);  // modular::Inverse(k, n);
  int step = (a % n + n) % n;

  vector<int> ans(n, base);
  for (int i = 0, j = 0; i < k; i++, j = (j + step) % n) {
    ans[j]++;
  }

  rotate(ans.begin(), ans.begin() + 1, ans.end());
  for (int x : ans) {
    out << x << ' ';
  }
}

RUN_ONCE