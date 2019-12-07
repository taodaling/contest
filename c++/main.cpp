#include <bits/stdc++.h>
#include <chrono>
#include <random>
#include<iomanip>
using std::cin;
using std::cout;
using std::deque;
using std::endl;
using std::map;
using std::max;
using std::min;
using std::pair;
using std::set;
using std::swap;
using std::vector;
using std::ios_base;
using std::ostream;
using std::istream; 

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;                    
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());
#define MOD(x, mod) x %= mod; if(x < 0) x += mod;
                                                 
ll gcd(ll a, ll b){
	if(a < b) swap(a, b);
	return gcd0(a, b);	
}
ll gcd0(ll a, ll b){
	return b == 0 ? a : gcd0(b, a % b);
}

struct Segment{
	int sum[20];
	int val[20];
	int pow[20];
};

Segment *buildSegment(int l, int r){
	Segment seg = new Segment();
	if(l < r){
		int m = (l + r) >> 1;
		seg->left = buildSegment = l;
		seg->right = buildSegment = r;

	}else{
		
	}
}

void pushUp(Segment *seg){
	
}


int main(){
	ios_base::sync_with_stdio(false);
    cin.tie(NULL);
	cout << std::setiosflags(std::ios::fixed);
	cout << std::setprecision(2);



    return 0;
}