#include "../../libs/common.h"

struct Box {
  int xyz[3];
  int max;
  int min;
};

Box boxes[20];
int sorted[20];
vector<int> ranks;
int bit[51];
int n;

void clear() { C0(bit); }

/**
 * 查询A[1]+A[2]+...+A[i]
 */

int query(int i) {
  int sum = 0;
  for (; i > 0; i -= i & -i) {
    sum = max(sum, bit[i]);
  }
  return sum;
}

/**
 * 将A[i]更新为A[i]+mod
 */
void update(int i, int mod) {
  if (i <= 0) {
    return;
  }
  for (; i <= 50; i += i & -i) {
    bit[i] = max(bit[i], mod);
  }
}

int rankOf(int x) {
  return std::lower_bound(ranks.begin(), ranks.end(), x) - ranks.begin();
}

class BoxTower {
 public:
  int tallestTower(vector<int> x, vector<int> y, vector<int> z) {
    n = x.size();
    for (int i = 0; i < n; i++) {
      sorted[i] = i;
    }
    for (auto v : x) {
      ranks.push_back(v);
    }
    for (auto v : y) {
      ranks.push_back(v);
    }
    for (auto v : z) {
      ranks.push_back(v);
    }
    ranks.push_back(0);
    sort(ranks.begin(), ranks.end());
    ranks.resize(unique(ranks.begin(), ranks.end()) - ranks.begin());

    for (int i = 0; i < n; i++) {
      x[i] = rankOf(x[i]);
      y[i] = rankOf(y[i]);
      z[i] = rankOf(z[i]);
    }
    //dbg(ranks, x, y, z);

    for (int i = 0; i < n; i++) {
      boxes[i].xyz[0] = x[i];
      boxes[i].xyz[1] = y[i];
      boxes[i].xyz[2] = z[i];
    }

    int ans = dfs(n - 1);
    return ans;
  }

  int solve() {
    for (int i = 0; i < n; i++) {
      if (boxes[i].xyz[0] < boxes[i].xyz[1]) {
        boxes[i].min = boxes[i].xyz[0];
        boxes[i].max = boxes[i].xyz[1];
      } else {
        boxes[i].min = boxes[i].xyz[1];
        boxes[i].max = boxes[i].xyz[0];
      }
    }
    sort(sorted, sorted + n, [&](int a, int b) {
      return boxes[a].max == boxes[b].max ? boxes[a].min < boxes[b].min
                                          : boxes[a].max < boxes[b].max;
    });

    for (int i = 0; i < n; i++) {
      auto &b = boxes[sorted[i]];
      int h = query(b.min) + ranks[b.xyz[2]];
      update(b.min, h);
    }
    int ans = query(50);
    clear();
    return ans;
  }

  int dfs(int t) {
    if (t == -1) {
      return solve();
    }
    int ans = 0;
    for (int i = 2; i >= 0; i--) {
      swap(boxes[t].xyz[i], boxes[t].xyz[2]);
      ans = max(ans, dfs(t - 1));
    }
    return ans;
  }
};

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<vector<int>> x(3, vector<int>(n));
  for(int i = 0; i < 3; i++){
    for(int j = 0; j < n; j++){
      in >> x[i][j];
    }
  }

  BoxTower solution;
  int ans = solution.tallestTower(x[0], x[1], x[2]);
  out << ans;
}

RUN_ONCE