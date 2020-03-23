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


namespace ac_automaton {
template <int L, int R>
class ACNode {
 public:
  ACNode *next[R - L + 1];
  ACNode *fail;
  ACNode *father;
  int index;
  int id;
  int cnt;
  int preSum;
 ll w;
  ACNode() {
    memset(next, 0, sizeof(next));
    fail = father = 0;
    w = 0;
    index = id = cnt = preSum = 0;
  }

  int getId() { return id; }

  int getCnt() { return cnt; }

  void decreaseCnt() { cnt--; }

  void increaseCnt() { cnt++; }

  int getPreSum() { return preSum; }
};

template <int L, int R>
class ACAutomaton {
 public:
  ACNode<L, R> *_root;
  ACNode<L, R> *_buildLast;
  ACNode<L, R> *_matchLast;
  vector<ACNode<L, R> *> _allNodes;
 

  ACNode<L, R> *addNode() {
    ACNode<L, R> *node = new ACNode<L, R>();
    node->id = _allNodes.size();
    _allNodes.push_back(node);
    return node;
  }

 public:
  ACNode<L, R> *getBuildLast() { return _buildLast; }

  ACNode<L, R> *getMatchLast() { return _matchLast; }

  vector<ACNode<L, R> *> &getAllNodes() { return _allNodes; }

  ACAutomaton() {
    _root = _buildLast = _matchLast = 0;
    _root = addNode();
    
  }

  void beginBuilding() { _buildLast = _root; }

  void endBuilding() {
    deque<ACNode<L, R> *> que;
    for (int i = 0; i < (R - L + 1); i++) {
      if (_root->next[i] != NULL) {
        que.push_back(_root->next[i]);
      }
    }

    while (!que.empty()) {
      ACNode<L, R> *head = que.front();
      que.pop_front();
      ACNode<L, R> *fail = visit(head->father->fail, head->index);
      if (fail == NULL) {
        head->fail = _root;
      } else {
        head->fail = fail->next[head->index];
      }
      head->w += head->fail->w;
      head->preSum = head->cnt + head->fail->preSum;
      for (int i = 0; i < (R - L + 1); i++) {
        if (head->next[i] != NULL) {
          que.push_back(head->next[i]);
        }
      }
    }

    for (int i = 0; i < (R - L + 1); i++) {
      if (_root->next[i] != NULL) {
        que.push_back(_root->next[i]);
      } else {
        _root->next[i] = _root;
      }
    }
    while (!que.empty()) {
      ACNode<L, R> *head = que.front();
      que.pop_front();
      for (int i = 0; i < (R - L + 1); i++) {
        if (head->next[i] != NULL) {
          que.push_back(head->next[i]);
        } else {
          head->next[i] = head->fail->next[i];
        }
      }
    }
  }

  ACNode<L, R> *visit(ACNode<L, R> *trace, int index) {
    while (trace != NULL && trace->next[index] == NULL) {
      trace = trace->fail;
    }
    return trace;
  }

  void build(char c) {
    int index = c - L;
    if (_buildLast->next[index] == NULL) {
      ACNode<L, R> *node = addNode();
      node->father = _buildLast;
      node->index = index;
      _buildLast->next[index] = node;
    }
    _buildLast = _buildLast->next[index];
  }

  void beginMatching() { _matchLast = _root; }

  void match(char c) {
    int index = c - L;
    _matchLast = _matchLast->next[index];
  }
};
}  // namespace ac_automaton

using namespace ac_automaton;

const int charset = 14;
const int mask = (1 << charset) - 1;

int nextQuest(string &buf, int i, int n) {
  for (int j = i; j < n; j++) {
    if (buf[j] == '?') {
      return i;
    }
  }
  return n;
}

void solve(int testId, istream &in, ostream &out) {
  int k;
  in >> k;
  ACAutomaton<'a', 'z'> ac;
  for (int i = 0; i < k; i++) {
    string buf;
    int w;
    in >> buf >> w;
    ac.beginBuilding();
    for (int j = 0; j < buf.length(); j++) {
      ac.build(buf[j]);
    }
    ac.getBuildLast()->w += w;
  }
  ac.endBuilding();
  vector<ACNode<'a', 'z'> *> &nodes = ac.getAllNodes();
  int m = nodes.size();
  string buf;
  in >> buf;
  ll inf = (ll)1e18;
  vector<vector<ll>> cur(m, vector<ll>(mask + 1, -inf));
  vector<vector<ll>> last(m, vector<ll>(mask + 1, -inf));
  last[0][0] = 0;

  vector<int> transfer = vector<int>(m);
  vector<ll> collect = vector<ll>(m);

  for (int i = 0; i < buf.length(); i++) {
    // debug.debug("last", last);
    int l = i;
    int r = nextQuest(buf, i, buf.length());
    i = r;

    for (int j = 0; j < m; j++) {
      transfer[j] = j;
      collect[j] = 0;
    }
    for (int j = l; j < r; j++) {
      int offset = buf[j] - 'a';
      for (int t = 0; t < m; t++) {
        transfer[t] = nodes[transfer[t]]->next[offset]->id;
        collect[t] += nodes[transfer[t]]->w;
      }
    }

    fill(cur.begin(), cur.end(), vector<ll>(mask + 1, -inf));

    if (r != buf.length()) {
      for (int t = 0; t <= mask; t++) {
        for (int z = 0; z < charset; z++) {
          if (bits::BitAt(t, z)) {
            continue;
          }
          for (int j = 0; j < m; j++) {
            int nid = nodes[transfer[j]]->next[z]->id;
            int bit = bits::SetBit(t, z);
            cur[nid][bit] =
                max<ll>(cur[nid][bit], last[j][t] + collect[j] + nodes[nid]->w);
          }
        }
      }

    } else {
      for (int t = 0; t <= mask; t++) {
        for (int j = 0; j < m; j++) {
          cur[transfer[j]][t] =
              max(cur[transfer[j]][t], last[j][t] + collect[j]);
        }
      }
    }

    cur.swap(last);
    continue;
  }

  ll ans = -inf;
  for (int i = 0; i < m; i++) {
    for (int j = 0; j <= mask; j++) {
      ans = max(ans, last[i][j]);
    }
  }

  out << ans;
}

RUN_ONCE