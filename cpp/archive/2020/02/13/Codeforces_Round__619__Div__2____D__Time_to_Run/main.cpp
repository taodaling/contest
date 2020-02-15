#include "../../libs/common.h"

vector<pair<char, int>> solution;

void add(char c, int t){
    if(t == 0){
        return;
    }
    solution.push_back(mp(c, t));
}

void solve(int testId, istream &in, ostream &out)
{
    int n, m, k;
    in >> n >> m >> k;
    
    add('R', m - 1);
    for(int i = 1; i < n; i++){
        add('D', 1);
        add('L', m - 1);
        add('R', m - 1);
    }
    add('U', n - 1);
    for(int i = m; i > 1; i--){
        add('L', 1);
        add('D', n - 1);
        add('U', n - 1);
    }

    ll total = 0;
    for(auto &x : solution){
        total += x.second;
    }
    if(total < k){
        out << "NO";
        return;
    }

    out << "YES" << endl;
    int to = -1;
    ll sum = 0;
    while(sum < k){
        to++;
        sum += solution[to].second;
        if(sum >= k){
            solution[to].second -= sum - k;
            break;
        }
    }
    out << to + 1 << endl;
    for(int i = 0; i <= to; i++){
        out << solution[i].second << ' ' << solution[i].first << endl;
    }
}

#include "../../libs/run_once.h"