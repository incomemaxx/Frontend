export default function MobileMenu() {
  const menuItems = [
    { name: "Home", href: "/" },
    { name: "Wallet", href: "/wallet" },
    { name: "Search", href: "/search" },
    { name: "Profile", href: "/profile" },
  ];

  const icons = {
    Home: (
      <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
        <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z" />
      </svg>
    ),
    Wallet: (
      <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
        <path d="M4 4a2 2 0 00-2 2v1h16V6a2 2 0 00-2-2H4z" />
        <path fillRule="evenodd" d="M18 9H2v5a2 2 0 002 2h12a2 2 0 002-2V9zM4 13a1 1 0 011-1h1a1 1 0 110 2H5a1 1 0 01-1-1zm5-1a1 1 0 100 2h1a1 1 0 100-2H9z" clipRule="evenodd" />
      </svg>
    ),
    Search: (
      <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
        <path fillRule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clipRule="evenodd" />
      </svg>
    ),
    Profile: (
      <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
        <path fillRule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clipRule="evenodd" />
      </svg>
    ),
  };

  return (
    <div className="md:hidden fixed bottom-0 left-0 right-0 bg-[#000000] border-t border-[#ffffff]/20 z-50">
      <div className="flex justify-around items-center py-3">
        {menuItems.map((item) => (
          <a
            key={item.name}
            href={item.href}
            className="flex flex-col items-center gap-1 text-[#ffffff]/60 hover:text-[#48CAE4] transition-colors active:scale-95"
          >
            {icons[item.name as keyof typeof icons]}
            <span className="text-xs font-semibold">{item.name}</span>
          </a>
        ))}
      </div>
    </div>
  );
}
