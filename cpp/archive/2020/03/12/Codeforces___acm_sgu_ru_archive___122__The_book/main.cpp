#include "../../libs/common.h"
#include "../../libs/reader.h"
#include "../../libs/debug.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  string s;
  getline(in, s);
  vector<vector<bool>> edges(n, vector<bool>(n));
  for (int i = 0; i < n; i++) {
    getline(in, s);
    reader::StringReader rd(s);
    while (rd.hasMore()) {
      int to = rd.readInt() - 1;
      edges[i][to] = true;
    }
  }

  vector<bool> included(n);
  vector<bool> access(n);
  vector<int> ans;
  ans.push_back(0);
  included[0] = true;
  for (int i = 0; i < n; i++) {
    access[i] = access[i] || edges[0][i];
  }

  while (true) {
    dbg(ans);
    int find = -1;
    for (int i = 0; i < n; i++) {
      if (!edges[ans.back()][i] || included[i]) {
        continue;
      }
      find = i;
      break;
    }
    if (find != -1) {
      ans.push_back(find);
      included[find] = true;
      for (int i = 0; i < n; i++) {
        access[i] = access[i] || edges[find][i];
      }
      continue;
    }
    break;
  }

  while (true) {
    dbg(ans);
    if (!edges[ans.front()][ans.back()]) {
      for (int i = 1; i < ans.size() - 1; i++) {
        if (edges[ans.front()][ans[i]] && edges[ans.back()][ans[i - 1]]) {
          reverse(ans.begin() + i, ans.end());
          break;
        }
      }
    }

    if (ans.size() == n) {
      break;
    }

    int z;
    for (int i = 0; i < n; i++) {
      if (access[i] && !included[i]) {
        z = i;
        break;
      }
    }

    int k;
    for (int i = 0; i < ans.size(); i++) {
      if (edges[ans[i]][z]) {
        k = i;
        break;
      }
    }

    rotate(ans.begin(), ans.begin() + k, ans.end());
    ans.insert(ans.begin(), z);
    included[z] = true;
    for (int i = 0; i < n; i++) {
      access[i] = access[i] || edges[z][i];
    }
  }

  int index = -1;
  for (int i = 0; i < n; i++) {
    if (ans[i] == 0) {
      index = i;
      break;
    }
  }
  rotate(ans.begin(), ans.begin() + index, ans.end());
  ans.push_back(0);
  for (int x : ans) {
    out << x + 1 << ' ';
  }
}

RUN_ONCE