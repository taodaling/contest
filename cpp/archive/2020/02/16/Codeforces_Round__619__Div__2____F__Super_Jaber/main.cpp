#include "../../libs/common.h"


int n;
int m;
int k;

vector<vector<pair<int, int>>> colors;
deque<pair<int, int>> dq;
bool visited[41];
int dirs[4][2]{
    {-1, 0},
    {1, 0},
    {0, -1},
    {0, 1}};
int dists[41][1000][1000];
int mat[1000][1000];
void Bfs(int c)
{
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < m; j++)
        {
            dists[c][i][j] = -1;
        }
    }
    fill(visited, visited + 41, false);
    visited[c] = true;
    for (auto p : colors[c])
    {
        dists[c][p.first][p.second] = 0;
        dq.push_back(p);
    }

    int cnt = 0;
    while (!dq.empty())
    {
        pair<int, int> head = dq.front();
        cnt++;
        dq.pop_front();
        if (!visited[mat[head.first][head.second]])
        {
            visited[mat[head.first][head.second]] = true;
            for (auto p : colors[mat[head.first][head.second]])
            {
                if (dists[c][p.first][p.second] == -1)
                {
                    dists[c][p.first][p.second] = dists[c][head.first][head.second] + 1;
                    dq.push_back(p);
                }
            }
        }
        for (auto dir : dirs)
        {
            int nx = head.first + dir[0];
            int ny = head.second + dir[1];
            if (nx < 0 || nx >= n || ny < 0 || ny >= m)
            {
                continue;
            }
            if (dists[c][nx][ny] == -1)
            {
                dists[c][nx][ny] = dists[c][head.first][head.second] + 1;
                dq.push_back(mp(nx, ny));
            }
        }
    }

    assert(cnt == n * m);
}

int Dist(int x1, int y1, int x2, int y2)
{
    int ans = abs(x1 - x2) + abs(y1 - y2);
    for (int i = 1; i <= k; i++)
    {
        ans = min(ans, dists[i][x1][y1] + dists[i][x2][y2] + 1);
    }
    return ans;
}

void solve(int testId, istream &in, ostream &out)
{
    in >> n >> m >> k;
    colors.resize(k + 1);
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < m; j++)
        {
            in >> mat[i][j];
            colors[mat[i][j]].push_back(mp(i, j));
        }
    }
    for (int i = 1; i <= k; i++)
    {
        Bfs(i);
    }
    int q;
    in >> q;
    for (int i = 0; i < q; i++)
    {
        int x1, y1, x2, y2;
        in >> x1 >> y1 >> x2 >> y2;
        x1--;
        y1--;
        x2--;
        y2--;
        out << Dist(x1, y1, x2, y2) << endl;
    }
}

#include "../../libs/run_once.h"