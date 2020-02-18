#include "../../libs/common.h"

set<string> sets;

string reverse(string &x){
    string ans = "";
    for(char c : x){
        ans = c + ans;
    }
    return ans;
}

void solve(int testId, istream &in, ostream &out)
{
    int n, m;
    in >> n >> m;
    vector<string> s(n);
    for(int i = 0; i < n; i++){
        in >> s[i];
    }
    sets.insert(s.begin(), s.end());
    deque<string> dq1;
    deque<string> dq2;
    deque<string> dq3;
    for(int i = 0; i < n; i++){
        if(sets.find(s[i]) == sets.end())
        {
            continue;
        }
        string r = reverse(s[i]);
        if(s[i] == r){
            if(dq3.empty())
            dq3.push_back(s[i]);
        }
        else if(sets.find(r) != sets.end())
        {
            dq1.push_back(s[i]);
            dq2.push_back(r);
        } 
        sets.erase(r);
        sets.erase(s[i]);
    }

    out << (dq1.size() + dq2.size() + dq3.size()) * m << endl;
    while(!dq1.empty()){
        out << dq1.front();
        dq1.pop_front();
    }
    while(!dq3.empty()){
        out << dq3.front();
        dq3.pop_front();
    }
    while(!dq2.empty()){
        out << dq2.back();
        dq2.pop_back();
    }
}

#include "../../libs/run_once.h"