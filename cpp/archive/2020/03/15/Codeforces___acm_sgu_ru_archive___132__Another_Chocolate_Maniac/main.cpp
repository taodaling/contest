#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/radix.h"
#include "../../libs/reader.h"

radix::Radix<int> rdx(3);

#define MAX_N 80
#define MAX_K 1000

char mat[MAX_N + 1][7];
int validState[MAX_K];
int dp[MAX_N + 1][MAX_K];
bool transfer[MAX_K][MAX_K];
int require[5000];
int remain[5000];

void solve(int testId, istream &in, ostream &out) {
  reader::Input input(in);
  int n = input.ri();
  int m = input.ri();
  for (int i = 1; i <= n; i++) {
    for (int j = 0; j < m; j++) {
      mat[i][j] = input.rc();
    };
  }
  for (int i = 0; i < m; i++) {
    mat[n + 1][i] = '*';
  }
  int mask = rdx(m);
  int inf = 1e8;

  int k = 0;
  for (int i = 0; i < mask; i++) {
    bool valid = true;
    for (int j = 1; j < m; j++) {
      if (rdx.get(i, j) == 0 && rdx.get(i, j - 1) == 0 ||
          rdx.get(i, j) == 2 && rdx.get(i, j - 1) == 2) {
        valid = false;
      }
    }
    if (valid) {
      validState[k++] = i;
    }
  }

  dbg(k);

  for (int i = 0; i <= n; i++) {
    for (int j = 0; j < k; j++) {
      dp[i][j] = inf;
    }
  }
  for (int i = 0; i < k; i++) {
    for (int j = 0; j < k; j++) {
      transfer[i][j] = true;
    }
  }
  for (int i = 0; i < k; i++) {
    for (int j = 0; j < k; j++) {
      for (int t = 0; t < m; t++) {
        if (rdx.get(validState[j], t) == 0 && rdx.get(validState[i], t) == 0 ||
            rdx.get(validState[j], t) == 2 && rdx.get(validState[i], t) == 2 ||
            rdx.get(validState[j], t) == 0 && rdx.get(validState[i], t) == 2) {
          transfer[i][j] = false;
        }
      }
    }
  }

  for (int i = 0; i < mask; i++) {
    remain[i] = 0;
    for (int x = 0; x < m; x++) {
      if (rdx.get(i, x) == 2) {
        remain[i] = rdx.set(remain[i], x, 1);
      }
    }
  }

  for (int i = 0; i < mask; i++) {
    int req = 0;
    bool add = false;
    for (int x = 0; x < m; x++) {
      if (rdx.get(i, x) == 0) {
        add = false;
        continue;
      }
      if (rdx.get(i, x) == 2) {
        add = false;
        req++;
        continue;
      }
      if (add) {
        add = false;
        continue;
      }
      add = true;
      req++;
    }
    require[i] = req;
  }

  int target = 0;
  for (int i = 0; i < m; i++) {
    target = rdx.set(target, i, 1);
  }
  for (int i = 0; i < k; i++) {
    if (validState[i] == target) {
      dp[0][i] = 0;
    }
  }

  for (int i = 1; i <= n; i++) {
    int init = 0;
    for (int j = 0; j < m; j++) {
      if (mat[i][j] == '*') {
        init = rdx.set(init, j, 1);
      }
    }
    for (int j = 0; j < k; j++) {
      int sj = validState[j];
      bool checkj = true;
      for (int x = 0; x < m; x++) {
        if (mat[i][x] == '*' && rdx.get(sj, x) != 1 ||
            mat[i + 1][x] == '*' && rdx.get(sj, x) == 2) {
          checkj = false;
        }
      }
      if (!checkj) {
        continue;
      }
      for (int t = 0; t < k; t++) {
        int st = validState[t];
        if (dp[i - 1][t] == inf || !transfer[t][j]) {
          continue;
        }
        dp[i][j] =
            min(dp[i][j],
                dp[i - 1][t] +
                    require[validState[j] - remain[validState[t]] - init]);
      }
    }
  }

  // for (int i = 0; i < k; i++) {
  //   dbg(i, require[i]);
  // }
  //  for (int i = 0; i < k; i++) {
  //   dbg(i, remain[i]);
  // }
  //  for (int i = 0; i <= n; i++) {
  //   for(int j = 0; j < k; j++){
  //     dbg(i, j, dp[i][j]);
  //   }
  // }
  int ans = inf;
  for (int i = 0; i < k; i++) {
    ans = min(ans, dp[n][i]);
  }

  out << ans;

  return;
}

RUN_ONCE