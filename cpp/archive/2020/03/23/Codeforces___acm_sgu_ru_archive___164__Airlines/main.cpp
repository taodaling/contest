#include "../../libs/common.h"

const int MAX_N = 200;
int dist[MAX_N][MAX_N];

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  int thresold = m / 2;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      in >> dist[i][j];
      if (dist[i][j] == 0 || dist[i][j] > thresold) {
        dist[i][j] = 10;
      } else {
        dist[i][j] = 1;
      }
    }
  }

  for (int i = 0; i < n; i++) {
    dist[i][i] = 0;
  }
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      for (int k = 0; k < n; k++) {
        dist[j][k] = min(dist[j][k], dist[j][i] + dist[i][k]);
      }
    }
  }

  bool valid = true;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (dist[i][j] > 3) {
        valid = false;
      }
    }
  }

  int l = 1;
  int r = thresold;
  if(!valid){
    l = thresold + 1;
    r = m;
  }

  out << r - l + 1 << endl;
  for(int i = l; i <= r; i++){
    out << i << ' ';
  }
}

RUN_ONCE