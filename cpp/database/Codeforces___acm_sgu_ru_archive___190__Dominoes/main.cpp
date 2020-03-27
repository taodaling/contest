#include "../../libs/common.h"
#include "../../libs/match.h"

using match::BipartiteMatch;

int n;

int IdOf(int i, int j) { return i * n + j; }

void solve(int testId, istream &in, ostream &out) {
  int p;
  in >> n >> p;
  vector<vector<bool>> mat(n, vector<bool>(n));
  for (int i = 0; i < p; i++) {
    int x, y;
    in >> x >> y;
    mat[x - 1][y - 1] = true;
  }

  BipartiteMatch bm(n * n, n * n);
  vector<vector<int>> dirs{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (mat[i][j] || (i + j) % 2 == 1) {
        continue;
      }
      for (auto &d : dirs) {
        int ni = i + d[0];
        int nj = j + d[1];
        if (ni < 0 || nj < 0 || ni >= n || nj >= n || mat[ni][nj]) {
          continue;
        }
        bm.addEdge(IdOf(i, j), IdOf(ni, nj), true);
      }
    }
  }

  int match = 0;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (mat[i][j] || (i + j) % 2 == 1) {
        continue;
      }
      match += bm.matchLeft(IdOf(i, j)) >= 0;
    }
  }

  if (match * 2 + p != n * n) {
    out << "No";
    return;
  }
  out << "Yes" << endl;
  vector<pair<int, int>> vertical;
  vector<pair<int, int>> horizontal;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (mat[i][j] || (i + j) % 2 == 1) {
        continue;
      }
      int op = bm.matchLeft(IdOf(i, j));
      int r = op / n;
      int c = op % n;
      if (r == i) {
        horizontal.emplace_back(i, min(j, c));
      } else {
        vertical.emplace_back(min(i, r), j);
      }
    }
  }

  out << vertical.size() << endl;
  for(auto &p : vertical){
    out << p.first + 1 << ' ' << p.second + 1 << endl;
  }
  out << horizontal.size() << endl;
  for (auto &p : horizontal) {
    out << p.first + 1 << ' ' << p.second + 1 << endl;
  }
}

RUN_ONCE