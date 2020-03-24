#include "../../libs/common.h"

const int FIX = 32000;
const int INF = 1e8;

vector<vector<int>> mat;
vector<vector<int>> fMem;
vector<vector<int>> gMem;
int n;
int m;

int g(int i, int j) {
  if (j >= m || i >= n || i < 0 || j < 0) {
    return INF;
  }
  if (gMem[i][j] == -1) {
    gMem[i][j] = min(mat[i][j], g(i - 1, j + 1));
  }
  return gMem[i][j];
}

int f(int i, int j) {
  if (j >= m || i >= n || i < 0 || j < 0) {
    return INF;
  }
  if (fMem[i][j] == -1) {
    if (i < n - 1) {
      fMem[i][j] = min(f(i + 1, j), g(i, j));
    }else{
      fMem[i][j] = min(f(i, j + 1), g(i, j));
    }
  }
  return fMem[i][j];
}

void solve(int testId, istream &in, ostream &out) {
  in >> n >> m;
  mat.resize(n, vector<int>(m));
  fMem.resize(n, vector<int>(m, -1));
  gMem.resize(n, vector<int>(m, -1));

  for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
      in >> mat[i][j];
      mat[i][j] += FIX;
    }
  }

  for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
      out << f(i, j) - FIX << ' ';
    }
    out << endl;
  }
}

RUN_ONCE