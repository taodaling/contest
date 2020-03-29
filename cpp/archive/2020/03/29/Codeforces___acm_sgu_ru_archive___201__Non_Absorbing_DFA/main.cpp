#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/bigint.h"
using bi = bigint::Bigint;


const int MAX_CHARSET = 'z' - 'a' + 1;
const int MAX_DP = 100;

struct Node {
  Node *next[MAX_CHARSET];
  bool accept;
  bool keep[MAX_CHARSET];
  bi dp[MAX_DP];
  bool instk;
  bool visited;
};

int charset;
vector<Node> nodes;

Node *Dfs(Node *root, int c) {
  if (root->visited) {
    // circular
    if (root->instk) {
      return NULL;
    }
    return root->next[c];
  }
  root->visited = true;
  if (!root->keep[c]) {
    return root->next[c];
  }
  root->instk = true;

  root->next[c] = Dfs(root->next[c], c);

  root->instk = false;
  return root->next[c];
}

bi invisited = -1;
bi zero = 0;
bi one = 1;
const bi &Dp(Node *root, int r) {
  if (root == NULL) {
    return zero;
  }
  if (root->dp[r] == invisited) {
    root->dp[r] = zero;
    if (r == 0) {
      return root->dp[r] = root->accept ? one : zero;
    }
    for (int i = 0; i < charset; i++) {
      root->dp[r] += Dp(root->next[i], r - 1);
    }
  }
  return root->dp[r];
}

void solve(int testId, istream &in, ostream &out) {
  string all;
  int K, S, L;
  in >> all >> K >> S >> L;
  S--;
  charset = all.size();
  nodes.resize(K);
  for (int i = 0; i < L; i++) {
    int index;
    in >> index;
    index--;
    nodes[index].accept = true;
  }
  for (int i = 0; i < K; i++) {
    for (int j = 0; j < charset; j++) {
      int next;
      in >> next;
      next--;
      nodes[i].next[j] = &nodes[next];
    }
  }
  for (int i = 0; i < K; i++) {
    for (int j = 0; j < charset; j++) {
      int X;
      in >> X;
      nodes[i].keep[j] = X == 1;
    }
  }

  int n;
  in >> n;

  for (int i = 0; i < charset; i++) {
    for (int j = 0; j < K; j++) {
      nodes[j].visited = nodes[j].instk = false;
    }
    for (int j = 0; j < K; j++) {
      Dfs(&nodes[j], i);
    }
  }

  for (int i = 0; i < K; i++) {
    for (int j = 0; j <= n; j++) {
      nodes[i].dp[j] = invisited;
    }
  }

  //dbg(Dp(&nodes[S], n - 1));
  bi ans = Dp(&nodes[S], n);
  // for (int i = 0; i < K; i++) {
  //   dbg(i, vector<ll>(nodes[i].dp, nodes[i].dp + n + 1));
  // }
  out << ans;
}

RUN_ONCE