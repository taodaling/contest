#define ctz __builtin_ctz
#define clz __builtin_clz
#define popcount __builtin_popcount
#define ctzll __builtin_ctzll
#define clzll __builtin_clzll
#define popcountll __builtin_popcountll
#define Mod(x, m) if(x < -m || x >= m) x %= m; if(x < 0) x += m;
#define GetBit(x, i) (((x) >> i) & 1)
#define SetBit(x, i) ((x) | ((int64)1 << i))
#define DelBit(x, i) ((x) & (~((int64)1 << i)))
#define CeilAvg(x, y) ((x) | (y)) - (((x) ^ (y)) >> 1)
#define FloorAvg(x, y) ((x) & (y)) + (((x) ^ (y)) >> 1)

#include <bits/stdc++.h>
using namespace std;
#ifdef LOCAL
void err(istream_iterator<string> it) {}
template<typename T, typename... Args>
void err(istream_iterator<string> it, T a, Args... args) {
	cerr << *it << " = " << a << endl;
	err(++it, args...);
}
#define dbg(args...) { string _s = #args; replace(_s.begin(), _s.end(), ',', ' '); stringstream _ss(_s); istream_iterator<string> _it(_ss); err(_it, args); }
#else
#define dbg(x...) 
#endif

typedef long long int64;
typedef vector<int> vi;
typedef vector<vector<int>> vii;
typedef pair<int, int> pii;
typedef long double ld;
typedef unsigned int uint;
typedef unsigned long long uint64;

template <typename T>
T gcd0(T a, T b) {
  return b ? gcd0(b, a % b) : a;
}

template <typename T>
T gcd(T a, T b) {
  if (a < b) {
	swap(a, b);
  }
  return gcd0(a, b);
}

template <typename T>
T extgcd0(T a, T b, T &x, T &y) {
  if (!b) {
	x = 1;
	y = 0;
	return a;
  }
  T ans = extgcd0(b, a % b, y, x);
  y = y - x * (a / b);
  return ans;
}

/**
 * Find gcd(a, b) and expression xa+yb=g
 */
template <typename T>
T extgcd(T a, T b, T &x, T &y) {
  if (a >= b) {
	return extgcd0(a, b, x, y);
  }
  return extgcd0(b, a, y, x);
}
template<int MOD> struct mint {
	static const int mod = MOD;
	int v; explicit operator int() const { return v; } // explicit -> don't silently convert to int
	mint() { v = 0; }
	mint(int64 _v) { v = int((-MOD < _v && _v < MOD) ? _v : _v % MOD);
		if (v < 0) v += MOD; }
	friend bool operator==(const mint& a, const mint& b) { 
		return a.v == b.v; }
	friend bool operator!=(const mint& a, const mint& b) { 
		return !(a == b); }
	friend bool operator<(const mint& a, const mint& b) { 
return a.v < b.v; }
friend string to_string(mint a) { return to_string(a.v); }
   
	mint& operator+=(const mint& m) { 
		if ((v += m.v) >= MOD) v -= MOD; 
		return *this; }
	mint& operator-=(const mint& m) { 
		if ((v -= m.v) < 0) v += MOD; 
		return *this; }
	mint& operator*=(const mint& m) { 
		v = (int64)v*m.v%MOD; return *this; }
	mint& operator/=(const mint& m) { return (*this) *= inv(m); }
friend mint pow(mint a, int64 p) {
	mint ans = 1; assert(p >= 0);
		for (; p; p /= 2, a *= a) if (p&1) ans *= a;
		return ans; }
	friend mint inv(const mint& a) { assert(a.v != 0); 
		int x, y;
		int g = extgcd(a.v, MOD, x, y);
		x = g != 1 ? 0 : x;
		return mint(x); }
		
	mint operator-() const { return mint(-v); }
	mint& operator++() { return *this += 1; }
	mint& operator--() { return *this -= 1; }
	friend mint operator+(mint a, const mint& b) { return a += b; }
	friend mint operator-(mint a, const mint& b) { return a -= b; }
	friend mint operator*(mint a, const mint& b) { return a *= b; }
	friend mint operator/(mint a, const mint& b) { return a /= b; }
};

typedef mint<998244353> mi;

template<typename T>
struct LinearFunction{
	T a, b; //ax+b
	LinearFunction(){}
	LinearFunction(T a0, T b0): a(a0), b(b0) {}
	LinearFunction(const LinearFunction<T> &x): a(x.a), b(x.b){}
	T operator()(T x) const {return a * x + b;}
	LinearFunction<T> inverse() const {
		T inva = 1 / a;
		return LinearFunction<T>(inva, -b * inva);
	}
	LinearFunction<T> &operator=(const LinearFunction<T> &x){
		a = x.a;
		b = x.b;
		return *this;
	}
	LinearFunction<T> &operator+=(const LinearFunction<T> &x){
		a += x.a;
		b += x.b;
		return *this;
	}
	LinearFunction<T> &operator-=(const LinearFunction<T> &x){
		a -= x.a;
		b -= x.b;
		return *this;
	}
	LinearFunction<T> &operator*=(const LinearFunction<T> &x){
		b = b + a * x.b;
		a = a * x.a;
		return *this;
	}
	friend LinearFunction<T> operator+(const LinearFunction<T> &a, const LinearFunction<T> &b){
		return LinearFunction<T>(a.a + b.a, a.b + b.b);
	}
	friend LinearFunction<T> operator-(const LinearFunction<T> &a, const LinearFunction<T> &b){
		return LinearFunction<T>(a.a - b.a, a.b - b.b);
	}
	friend bool operator==(const LinearFunction<T> &a, const LinearFunction<T> &b){
		return a.a == b.a && a.b == b.b;
	}
	friend LinearFunction<T> operator*(const LinearFunction<T> &a, const LinearFunction<T> &b){
		return LinearFunction<T>(a.a * b.a, a.b + a.a * b.b);
	}
	static LinearFunction<T> identity();
	friend string to_string(const LinearFunction<T> &lf){
		return to_string(lf.a) + "x + " + to_string(lf.b);
	}
};
template<typename T>
LinearFunction<T> LinearFunction<T>::identity(){return LinearFunction<T>(1, 0);}

typedef LinearFunction<mi> mif;

template<int N, int M = 4 * N> struct Segtree {
	mif sum[M];
	
	bool cover(int ll, int rr, int l, int r){
		return ll <= l && rr >= r;
	}
	bool leave(int ll, int rr, int l, int r){
		return rr < l || ll > r;
	}
	int left(int i){
		return i * 2;
	}
	int right(int i){
		return i * 2 + 1;
	}
	void pushUp(int i){
		sum[i] = sum[right(i)] * sum[left(i)];
	}
	void pushDown(int i){
	}
	void build(int i, int l, int r, function<mif(int)> func) {
		if(l != r){
			int m = FloorAvg(l, r);
			build(left(i), l, m, func);
			build(right(i), m + 1, r, func);
			pushUp(i);
		}else{
			sum[i] = func(l);
		}
	}

	void modify(int i, mif func){
		sum[i] = func;
	}
	
	void update(int i, int ll, int rr, int l, int r, mif func){
		if(leave(ll, rr, l, r)){
			return;
		}
		if(cover(ll, rr, l, r)){
			modify(i, func);
			return;
		}
		pushDown(i);
		int m = FloorAvg(l, r);
		update(left(i), ll, rr, l, m, func);
		update(right(i), ll, rr, m + 1, r, func);
		pushUp(i);
	}
	mif query(int i, int ll, int rr, int l, int r){
		if(leave(ll, rr, l, r)){
			return mif::identity();
		}
		if(cover(ll, rr, l, r)){
			return sum[i];
		}
		pushDown(i);
		int m = FloorAvg(l, r);
		return query(right(i), ll, rr, m + 1, r) * query(left(i), ll, rr, l, m);
	}
	
	string ts(int i, int l, int r) {
		if(l == r){
			return to_string(sum[i]);
		}
		int m = FloorAvg(l, r);
		pushDown(i);
		return ts(left(i), l, m) + "," + ts(right(i), m + 1, r);
	}
	
	friend string to_string(const Segtree<N>& segtree, int l, int r){
		Segtree<N> c = segtree;
		return c.ts(1, l, r);
	}
};


void destroy(){}

void init() {
}

mif ele[(int)5e5];
Segtree<(int)5> segtree;
void solveOne(int testNum) {
	int n, q;
	cin >> n >> q;
	dbg(n, q);
	for(int i = 0; i < n; i++){
		int a, b;
		cin >> a >> b;
		ele[i] = mif(a, b);
	}
	for(int i = 0; i < n; i++){
		dbg(i, to_string(ele[i]));
	}
	
	segtree.build(1, 0, n - 1, [&](int i){return ele[i];});
	dbg(segtree.ts(1, 0, n - 1));
	for(int i = 0; i < q; i++){
		dbg(i);
		int t;
		cin >> t;
		if(t == 0){
			int p, c, d;
			cin >> p >> c >> d;
			segtree.update(1, p, p, 0, n - 1, mif(c, d));
		}else{
			int l, r, x;
			cin >> l >> r >> x;
			mif f = segtree.query(1, l, r, 0, n - 1);
			int ans = int(f(x));
			cout << ans << '\n';
		}
		
		string s = to_string(segtree, 0, n - 1);
		dbg(s);
	}
}	

void solve() {
    int q = 1;
    for(int i = 1; i <= q; i++) {
        solveOne(i);
    }
}

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(0);
    std::cout << std::setiosflags(std::ios::fixed);
    std::cout << std::setprecision(15);

#ifdef LOCAL
	int testId = 0;
	while(!cin.eof()){
	testId++;
#endif
    init();
    solve();
#ifdef LOCAL
	cout << endl << "--------" << testId << "--------" << endl;
	destroy();
	}
#endif

    return 0;
}
