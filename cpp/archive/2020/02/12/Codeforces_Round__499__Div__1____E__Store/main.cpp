#include "../../libs/common.h"
#define MEM_LIMIT 250
#include "../../libs/memory_pool.h"
#include "../../libs/hash.h"

struct Point{
    int x, y, z;
    int w;
    int ans;
    
    Point(){
        x = y = z = w = ans = 0;
    }

    ll hashValue(){
        return x * (ll)1 + y * (ll)1e5 + z * (ll)1e10; 
    }
};

istream& operator>>(istream& in, Point &pt){
    in >> pt.x >> pt.y >> pt.z;
    return in;
}

struct Query{
    Point *rely[8];

    int query(){
        return rely[0]->ans - rely[1]->ans - rely[2]->ans - rely[3]->ans
            + rely[4]->ans + rely[5]->ans + rely[6]->ans - rely[7]->ans;

    }
};

void solve(int testId, istream &in, ostream &out)
{
    int xm, ym, zm, n, m, k;
    in >> xm >> ym >> zm >> n >> m >> k;
    Point top, bot;
    top.x = top.y = top.z = 0;
    bot.x = bot.y = bot.z = 1e9;
    for(int i = 0; i < n; i++){
        Point tmp;
        in >> tmp;
        top.x = max(top.x, tmp.x);
        top.y = max(top.y, tmp.y);
        top.z = max(top.z, tmp.z);
        bot.x = min(bot.x, tmp.x);
        bot.y = min(bot.y, tmp.y);
        bot.z = min(bot.z, tmp.z);
    }
    vector<Point *> points;
    points.reserve(m + 8 * k);
    for(int i = 0; i < m; i++){
        Point *pt = new Point();
        in >> *pt;
        if(pt->x >= bot.x && pt->x <= top.x &&
        pt->y >= bot.y && pt->y <= top.y &&
        pt->z >= bot.z && pt->z <= top.z)
        {
            out << "INCORRECT";
            return;
        }
        points.push_back(pt);
    }

    for(int i = 0; i < k; i++){
        
    }
}

#include "../../libs/run_once.h"