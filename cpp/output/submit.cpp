#ifndef BITS_H
#define BITS_H

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

namespace bits {
template <class T>
inline bool BitAt(T x, int i) {
  return (x >> i) & 1;
}
template <class T>
inline T SetBit(T x, int i) {
  return x |= T(1) << i;
}
template <class T>
inline T RemoveBit(T x, int i) {
  return x &= ~(T(1) << i);
}
template <class T>
inline T LowestOneBit(T x) {
  return x & -x;
}
inline int FloorLog2(unsigned int x) {
  if (x == 0) {
    return 0;
  }
  return (sizeof(unsigned int) * 8) - 1 - __builtin_clz(x);
}
inline int FloorLog2(unsigned long long x) {
  if (x == 0) {
    return 0;
  }
  return (sizeof(unsigned long long) * 8) - 1 - __builtin_clzll(x);
}
inline int CeilLog2(unsigned int x) {
  if (x == 0) {
    return 0;
  }
  return (sizeof(unsigned int) * 8) - __builtin_clz(x - 1);
}
inline int CeilLog2(unsigned long long x) {
  if (x == 0) {
    return 0;
  }
  return (sizeof(unsigned long long) * 8) - __builtin_clzll(x - 1);
}
template <class T>
inline T HighestOneBit(T x) {
  return T(1) << FloorLog2(x);
}
inline int CountOne(unsigned int x) { return __builtin_popcount(x); }
inline int CountOne(unsigned long long x) { return __builtin_popcountll(x); }
}  // namespace bits
#endif


void solve(int testId, istream &in, ostream &out) {
  ll a, b;
  in >> a >> b;
  if (a > b) {
    swap(a, b);
  }

  if (a == 0) {
    out << 0;
    return;
  }

  ll n = a + b;
  ll k = 1;
  while (a % 2 == 0 && n % 2 == 0) {
    a /= 2;
    n /= 2;
  }

  if(a % 2 == 0){
    out << -1;
    return;
  }

  int step = 0;
  while(n % 2 == 0){
    n /= 2;
    k *= 2;
    step++;
  }

  if (a % n != 0){
    out << -1;
    return;
  }

  int t = a / n;
  if(t > k / 2){
    out << -1;
    return;
  }

  out << step;
}

RUN_ONCE