#include "../../libs/common.h"

vector<vector<int>> edges;
struct node{
    int dep;
    bool deleted;
};

void solve(int testId, istream &in, ostream &out)
{
    int n, m;
    in >> n >> m;

    deque<int> end;
    deque<int> end2;
    vector<node> nodes(n + 1);
    edges.resize(n + 1);

    for(int i = 1; i <= m; i++){
        int a, b;
        in >> a >> b;
        edges[a].push_back(b);
        nodes[b].dep++;
    }

    for(int i = 1; i <= n; i++){
        if(nodes[i].dep == 0){
            end.push_back(i);
        }
    }

    vector<int> ans;
    while(!end.empty()){
        while(!end.empty()){
            int tail = end.back();
            end.pop_back();
            if(nodes[tail].deleted){
                continue;
            }
            ans.push_back(tail);
            for(int next : edges[tail])
            {
                if(nodes[next].deleted){
                    continue;
                }
                nodes[next].deleted = true;
                for(int x : edges[next]){
                    nodes[x].dep--;
                    if(nodes[x].dep == 0){
                        end2.push_back(x);
                    }
                }
            }
        }
        end.swap(end2);
    }

    out << ans.size() << endl;
    for(int x : ans){
        out << x << " ";
    }
}

#include "../../libs/run_once.h"