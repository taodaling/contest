#ifndef COMMON_H
#define COMMON_H

#pragma GCC target ("sse4.2")

#include <iostream>
#include <fstream>
#include <iomanip>
#include <bits/stdc++.h>
#include <chrono>
#include <random>
#include<ext/rope>

using __gnu_cxx::rope;
using std::cerr;
using std::deque;
using std::endl;
using std::fill;
using std::ios_base;
using std::istream;
using std::iterator;
using std::make_pair;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::set;
using std::sort;
using std::string;
using std::swap;
using std::unordered_map;
using std::vector;
using std::bitset;
using std::uniform_int_distribution;
using std::uniform_real_distribution;
using std::stringstream;
using std::istream_iterator;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

#define error(args...) { string _s = #args; replace(_s.begin(), _s.end(), ',', ' '); stringstream _ss(_s); istream_iterator<string> _it(_ss); err(_it, args); }
void err(std::istream_iterator<string> it) {}
template<typename T, typename... Args>
void err(std::istream_iterator<string> it, T a, Args... args) {
	cerr << *it << " = " << a << endl;
	err(++it, args...);
}

#define mp make_pair
#define popcount __builtin_popcount

#endif


struct BIT {
    int data[501][501];
    int n;
    int m;



    /**
     * 创建大小A[1...n][1..,m]
     */
    BIT() {
       n = 500;
       m = 500;
    }

    /**
     * 查询左上角为(1,1)，右下角�?(x,y)的矩形和
     */
    int query(int x, int y) {
        int sum = 0;
        for (int i = x; i > 0; i -= i & -i) {
            for (int j = y; j > 0; j -= j & -j) {
                sum += data[i][j];
            }
        }
        return sum;
    }


    int rect(int ltx, int lty, int rbx, int rby) {
        return query(rbx, rby) - query(ltx - 1, rby) - query(rbx, lty - 1) + query(ltx - 1, lty - 1);
    }

    /**
     * 将A[x][y] 更新为A[x][y]+mod
     */
    void update(int x, int y, int mod) {
        for (int i = x; i <= n; i += i & -i) {
            for (int j = y; j <= m; j += j & -j) {
                data[i][j] += mod;
            }
        }
    } }b2d;

struct Query{
    int x1;
    int y1;
    int x2;
    int y2;
    int ans;
};

struct Cell{
    int i;
    int j;
    int w;
    int x;
    int y;
};

vector<Query> queries;


int query(int x1, int y1, int x2, int y2){
    int sum = b2d.rect(x1, y1, x2, y2);
    return sum;
}

int query(Query &q){
    return query(q.x1, q.y1, q.x2, q.y2);
}

void apply(Cell &cell, int x){
    b2d.update(cell.x, cell.y, x);
    if(cell.i > 1){
        b2d.update(cell.i - 1, cell.y, -x);
    }
    if(cell.j > 1){
        b2d.update(cell.x, cell.j - 1, -x);
    }
    if(cell.i > 1 && cell.j > 1){
        b2d.update(cell.i - 1, cell.j - 1, x);
    }
}

deque<int> exist;
deque<int> notExist;

void dac(vector<int> &qs, int ql, int qr, vector<Cell> &cells, int cl, int cr)
{
    if(cl > cr || ql > qr){
        return;
    }
    if(cl == cr){
        for(int i = ql; i <= qr; i++){
            queries[qs[i]].ans = cells[cl].w;
        }
        return;
    }
    int cm = (cl + cr + 1) / 2;
    for(int i = cm; i <= cr; i++){
        apply(cells[i], 1);
    }
    for(int i = ql; i <= qr; i++){
        if(query(queries[qs[i]]) == 0){
            notExist.push_back(qs[i]);
        }else{
            exist.push_back(qs[i]);
        }
    }
    int qm = ql + notExist.size() - 1;
    for(int i = ql; i <= qm; i++){
        qs[i] = notExist.back();
        notExist.pop_back();
    }
    for(int i = qm + 1; i <= qr; i++){
        qs[i] = exist.back();
        exist.pop_back();
    }
    assert(notExist.empty());
    assert(exist.empty());
    dac(qs, ql, qm, cells, cl, cm - 1);
    for(int i = cm; i <= cr; i++){
        apply(cells[i], -1);
    }
    dac(qs, qm + 1, qr, cells, cm, cr);
}

void solve(int testId, istream &in, ostream &out)
{
    int n, m, q;
    in >> n >> m >> q;
    vector<vector<int>> mat(n + 1, vector<int>(m + 1));
    vector<vector<int>> sz(n + 1, vector<int>(m + 1));
    for(int i = 1; i <= n; i++){
        for(int j = 1; j <= m; j++){
            char c;
            in >> c;
            mat[i][j] = c == 'R' ? 0 : c == 'G' ? 1 : c == 'Y' ? 2 : 3;
        }
    }
    for(int i = n ; i >= 1; i--)
    {
        for(int j = m; j >= 1; j--){
            if(i + 1 <= n && j + 1 <= m && mat[i][j] == mat[i + 1][j + 1] && 
            mat[i][j] == mat[i + 1][j] && mat[i][j] == mat[i][j + 1])
            {
                sz[i][j] = 1 + sz[i + 1][j + 1];
            }
            else{
                sz[i][j] = 1;
            }
        }
    }

    vector<Cell> cells;
    for(int i = 1; i <= n; i++){
        for(int j = 1; j <= m; j++){
            if(mat[i][j] != 0){
                continue;
            }
            int s = sz[i][j];
            int x2 = i + s;
            int y2 = j + s;
            if(x2 > n || y2 > m){
                continue;
            }
            if(mat[i][y2] == 1 && sz[i][y2] >= s && 
            mat[x2][j] == 2 && sz[x2][j] >= s && 
            mat[x2][y2] == 3 && sz[x2][y2] >= s){
                Cell cell;
                cell.i = i;
                cell.j = j;
                cell.x = x2 + s - 1;
                cell.y = y2 + s - 1;
                cell.w = s * s * 4;
                cells.push_back(cell);
            }
        }
    }

    sort(cells.begin(), cells.end(), [](auto &a, auto &b){return a.w < b.w;});
    queries.resize(q);
    for(int i = 0; i < q; i++){
        in >> queries[i].x1 >> queries[i].y1 >> queries[i].x2 >> queries[i].y2;
    }
    vector<int> qsIndex(q);
    for(int i = 0; i < q; i++){
        qsIndex[i] = i;
    }
    dac(qsIndex, 0, q - 1, cells, 0, cells.size() - 1);
    for(int i = 0; i < q; i++){
        out << queries[i].ans << endl;
    }
}

int main()
{
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout << std::setiosflags(std::ios::fixed);
    std::cout << std::setprecision(15);
    
    solve(1, std::cin, std::cout);

    return 0;
}