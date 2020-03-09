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
using std::reverse;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

#ifdef LOCAL
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
#else
#define error(args...)
#endif

#define mp make_pair

#endif

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

bitset<(int)1e7 + 1> bs;

struct Query{
  int k;
  int ans;
};

int Plus(int x){
  if(x == 0){
    return x;
  }
  return x % 10 + Plus(x / 10);
}

Query qs[5000];
void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;
  for(int i = 1; i <= n; i++){
    int x = i + Plus(i);
    if(x > n){
      continue;
    }
    bs[x] = true;
  }
  vector<int> ks(k);
  for(int i = 0; i < k; i++){
    ks[i] = i;
    in >> qs[i].k;
  }
  sort(ks.begin(), ks.end(), [&](int a, int b) {return qs[a].k < qs[b].k;});
  deque<int> dq;
  dq.assign(ks.begin(), ks.end());
  int cnt = 0;
  for(int i = 1; i <= n; i++){
    if(!bs[i]){
      cnt++;
    }
    while(!dq.empty() && qs[dq.front()].k == cnt){
      qs[dq.front()].ans = i;
      dq.pop_front();
    }
  }

  out << cnt << endl;

  for(int i = 0; i < k; i++){
    out << qs[i].ans << ' ';
  }
}

RUN_ONCE