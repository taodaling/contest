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

struct Node {
  vector<int> out;
  vector<int> all;
  deque<int> dq;
  bool instk;
  int indeg;
  int set;
  int dfn;
  int low;
  int id;
};

vector<Node> nodes;
deque<int> stk;
int order = 0;

int ask(ostream &out, istream &in, int a, int b) {
  out << "? " << a << ' ' << b << endl;
  out.flush();
  int ans;
  in >> ans;
  return ans == 1 ? a : b;
}

void tarjan(int root) {
  if (nodes[root].dfn != 0) {
    return;
  }
  nodes[root].dfn = nodes[root].low = ++order;
  nodes[root].instk = true;
  stk.push_back(root);
  for (int node : nodes[root].out) {
    tarjan(node);
    if (nodes[node].instk && nodes[node].low < nodes[root].low) {
      nodes[root].low = nodes[node].low;
    }
  }

  if (nodes[root].low == nodes[root].dfn) {
    while (true) {
      int last = stk.back();
      stk.pop_back();
      nodes[last].set = root;
      nodes[last].instk = false;
      nodes[root].all.push_back(last);
      if (last == root) {
        break;
      }
    }
    nodes[root].dq.assign(nodes[root].all.begin(), nodes[root].all.end());
  }
}

void addBack(int x, deque<int> &dq) {
  if (!nodes[x].dq.empty()) {
    dq.push_back(x);
    return;
  }
  for (int root : nodes[x].all) {
    for (int node : nodes[root].out) {
      if (nodes[node].set == x) {
        continue;
      }
      nodes[nodes[node].set].indeg--;
      if (nodes[nodes[node].set].indeg == 0) {
        dq.push_back(nodes[node].set);
      }
    }
  }
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  int m;
  in >> n >> m;

  nodes.resize(n + 1);
  for (int i = 1; i <= n; i++) {
    nodes[i].id = i;
  }
  for (int i = 0; i < m; i++) {
    int a, b;
    in >> a >> b;
    nodes[a].out.push_back(b);
  }

  for (int i = 1; i <= n; i++) {
    tarjan(i);
  }

  deque<int> dq;
  for (int i = 1; i <= n; i++) {
    for (int node : nodes[i].out) {
      if (nodes[node].set == nodes[i].set) {
        continue;
      }
      nodes[nodes[node].set].indeg++;
    }
  }

  for (int i = 1; i <= n; i++) {
    if (i == nodes[i].set && nodes[i].indeg == 0) {
      dq.push_back(i);
    }
  }

  while (dq.size() > 1) {
    int a = dq.front();
    dq.pop_front();
    int b = dq.front();
    dq.pop_front();
    

    int x = nodes[a].dq.front();
    int y = nodes[b].dq.front();

    if (ask(out, in, x, y) == x) {
      nodes[b].dq.pop_front();
    } else {
      nodes[a].dq.pop_front();
    }

    addBack(a, dq);
    addBack(b, dq);
  }

  int node = nodes[dq.front()].dq.front();
  out << "! " << node << endl;
  out.flush();
}

RUN_ONCE