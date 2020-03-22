#include "../../libs/common.h"

void output(ostream &out, int x) {
  out << x / 2;
  if (x % 2) {
    out << ".5";
  }
}

void solve(int testId, istream &in, ostream &out) {
  int l, m;
  in >> l >> m;
  l *= 2;
  vector<int> people(m);
  for (int i = 0; i < m; i++) {
    in >> people[i];
    people[i] *= 2;
  }
  int n;
  in >> n;
  vector<int> door(n);
  for (int i = 1; i < n; i++) {
    in >> door[i];
    door[i] *= 2;
  }

  ll offset = 0;
  ll ans = -1;

  for (int i = 0; i <= l && door.back() + i <= l; i++) {
    int l = 0;
    int local = 0;
    for (int j = 0; j < m; j++) {
      while (l < n && door[l] + i <= people[j]) {
        l++;
      }
      int contrib = (int)1e9;
      if (l > 0) {
        contrib = min(contrib, people[j] - door[l - 1] - i);
      }
      if (l < n) {
        contrib = min(contrib, door[l] + i - people[j]);
      }
      local += contrib;
    }

    if (local > ans) {
      ans = local;
      offset = i;
    }
  }

  output(out, offset);
  out << ' ';
  output(out, ans);
}

RUN_ONCE