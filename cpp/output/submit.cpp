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

#define X(x) (x).first
#define Y(x) (x).second


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

vector<int> p0b;
vector<int> pb0;

int xorPB(int i, int j) { return p0b[j] ^ pb0[i] ^ p0b[0]; }

int xorPP(int i, int j) { return pb0[i] ^ pb0[j]; }

int ask(int a, int b) {
  std::cout << "? " << a << " " << b << endl;
  std::cout.flush();
  int ans;
  std::cin >> ans;
  return ans;
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;

  p0b.resize(n);
  pb0.resize(n);
  for (int i = 0; i < n; i++) {
    p0b[i] = ask(0, i);
    pb0[i] = ask(i, 0);
  }

  vector<int> p(n);
  vector<int> b(n);

  vector<int> ansP;
  int possible = 0;
  for (int i = 0; i < n; i++) {
    p[0] = i;
    for (int j = 1; j < n; j++) {
      p[j] = xorPP(0, j) ^ p[0];
    }
    for (int j = 0; j < n; j++) {
      b[j] = xorPB(0, j) ^ p[0];
    }

    bool valid = true;
    for (int j = 0; j < n; j++) {
      if (p[j] >= n || b[p[j]] != j) {
        valid = false;
        break;
      }
    }

    if (!valid) {
      continue;
    }
    possible++;
    if (possible == 1) {
      ansP = p;
    }
  }

  out << ("!") << endl << possible << endl;
  if (possible == 0) {
    return;
  }
  for (int x : ansP) {
    out << x << ' ';
  }
}

RUN_ONCE