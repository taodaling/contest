#include "../../libs/common.h"
#include "../../libs/acautomaton.h"
#include "../../libs/modular.h"

using namespace ac_automaton;
using Mint = modular::Modular<int, 1000000007>;

inline bool gt(int n, int k)
{
    return k >= n - k;
}

void solve(int testId, istream &in, ostream &out)
{
    int n;
    string s;
    in >> n >> s;

    ACAutomaton<0, 1> am;
    am.beginBuilding();
    for (char c : s)
    {
        am.build(c == '(' ? 0 : 1);
    }
    am.endBuilding();
    auto &states = am.getAllNodes();
    int m = states.size();
    states[m - 1]->next[0] = states[m - 1]->next[1] = states[m - 1];

    vector<vector<vector<Mint>>> dp(n * 2 + 1, vector<vector<Mint>>(n + 1, vector<Mint>(m)));
    dp[0][0][0] = 1;
    for (int i = 0; i < n * 2; i++)
    {
        for (int j = 0; j <= n; j++)
        {
            for (int k = 0; k < m; k++)
            {
                //0
                if (j + 1 <= n && gt(i + 1, j + 1))
                {
                    dp[i + 1][j + 1][states[k]->next[0]->id] += dp[i][j][k];
                }
                //1
                if (gt(i + 1, j))
                {
                    dp[i + 1][j][states[k]->next[1]->id] += dp[i][j][k];
                }
            }
        }
    }
    out << dp[n * 2][n][m - 1];
}

#include "../../libs/run_once.h"