#include "../../libs/common.h"

vector<vector<int>> dp(401, vector<int>(401));

bool test(string &s, string &t, int begin)
{
    int n = s.size();
    int m = t.size();
    for(int i = 0; i <= n; i++){
        for(int j = 0; j <= m; j++){
            dp[i][j] = -10;
        }
    }
    dp[0][0] = begin - 1;
    for(int i = 1; i <= n; i++){
        for(int j = 0; j <= m; j++){
            dp[i][j] = dp[i - 1][j];
            if(j > 0 && s[i - 1] == t[j - 1])
            {
                dp[i][j] = max(dp[i][j], dp[i - 1][j - 1]);
            }
            if(dp[i - 1][j] + 1 >= 0 && dp[i - 1][j] + 1 < m  && t[dp[i - 1][j] + 1] == s[i - 1])
            {
                dp[i][j] = max(dp[i][j], dp[i - 1][j] + 1);
            }
        }
    }

    return dp[n][begin] >= m - 1;
}

void solve(int testId, istream &in, ostream &out)
{
    string s, t;
    in >> s >> t;
    for(int i = 0; i < t.size(); i++){
        if(test(s, t, i))
        {
            error(i);
            out << "YES" << endl;
            return;
        }
    }
    out << "NO" << endl;
    return;
}

#include "../../libs/run_multi.h"