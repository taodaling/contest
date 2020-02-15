#include "../../libs/common.h"

int oneChild;
int twoChild;

bool edges[26][26];

void dfs(ostream &out, int root, int p){
    out << (char)('a' + root);
    for(int i = 0; i < 26; i++){
        if(edges[root][i] && p != i){
            dfs(out, i, root);
        }
    }
}

void solve(int testId, istream &in, ostream &out)
{
    string s;
    in >> s;
    
    if(s.size() == 1){
        out << "YES" << endl;
        for(char i = 'a'; i <= 'z'; i++){
            out << i;
        }
        out << endl;
        return;
    }

    memset(edges, 0, sizeof(edges));
    for(int i = 1; i < s.size(); i++){
        edges[s[i] - 'a'][s[i - 1] - 'a'] = true;
        edges[s[i - 1] - 'a'][s[i] - 'a'] = true;
    }

    vector<int> cnts(26);
    vector<int> num(3);
    for(int i = 0; i < 26; i++){
        for(int j = 0; j < 26; j++){
            if(edges[i][j]){
                cnts[i]++;
            }
        }
        if(cnts[i] > 2){
            out << "NO" << endl;
            return;
        }
        num[cnts[i]]++;
    }

    if(num[1] != 2){
        out << "NO" << endl;
        return;
    }

    out << "YES" << endl;
    for(int i = 0; i < 26; i++){
        if(cnts[i] == 1){
            dfs(out, i, -1);
            break;
        }
    }
    for(int i = 0; i < 26; i++){
        if(cnts[i] == 0){
            out << (char)('a' + i);
        }
    }

    out << endl;

}

#include "../../libs/run_multi.h"