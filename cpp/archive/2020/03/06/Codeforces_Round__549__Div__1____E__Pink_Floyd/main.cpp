#include "../../libs/common.h"

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