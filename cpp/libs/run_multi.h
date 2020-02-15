
int main()
{
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout << std::setiosflags(std::ios::fixed);
    std::cout << std::setprecision(15);
    
    int t;
    std::cin >> t;
    for(int i = 1; i <= t; i++)
    {
        solve(i, std::cin, std::cout);
    }

    return 0;
}