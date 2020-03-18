#include "../../libs/common.h"

struct Level {
  int w, l, p;
};

struct State {
  int p;
  int r;
  int l;
};

bool operator>(const State &a, const State &b) { return a.r > b.r; }

struct Machine {
  priority_queue<State, vector<State>, std::greater<State>> pq;
  int sum;
  int mod;
  Machine() : sum(0), mod(0) {}

  void add(int r, int p, int l) {
    sum += p;
    State state = {p : p, r : r - mod, l : l};
    pq.push(state);
    normalize();
  }

  void modify(int x) {
    mod += x;
    normalize();
  }

  void normalize() {
    while (!pq.empty() && pq.top().r + mod < 0) {
      sum -= pq.top().p;
      pq.pop();
    }
  }
};

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<Level> lvs(n);
  for (int i = 0; i < n; i++) {
    in >> lvs[i].w >> lvs[i].l >> lvs[i].p;
  }
  Machine machine;
  int ans = (int)1e9;
  for (int i = n - 1; i >= 0; i--) {
    machine.add(lvs[i].l, lvs[i].p, i);
    machine.modify(-lvs[i].w);
    ans = min(ans, machine.sum);
  }

  machine = Machine();
  for (int i = n - 1; i >= 0; i--) {
    machine.add(lvs[i].l, lvs[i].p, i);
    machine.modify(-lvs[i].w);
    if (ans == machine.sum) {
      break;
    }
  }

  vector<int> vec;
  while (!machine.pq.empty()) {
    vec.push_back(machine.pq.top().l);
    machine.pq.pop();
  }
  sort(vec.begin(), vec.end());
  for (int x : vec) {
    out << x + 1 << endl;
  }
}

RUN_ONCE