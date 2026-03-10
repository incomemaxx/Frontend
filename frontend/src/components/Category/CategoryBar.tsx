const categories = [
  "Trending",
  "Politics",
  "Sports",
  "Culture",
  "Crypto",
  "Climate",
  "Economics",
  "Mentions",
  "Companies",
  "Financials",
  "Tech & Science",
];

export default function CategoryBar() {
  return (
    <div className="w-full flex gap-7 pt-0.5 pb-3 bg-[#000000]/50 overflow-x-auto max-w-330 mx-auto scrollbar-hide pl-6">
      {categories.map((category) => (
        <a
          key={category}
          href={`/category/${category.toLowerCase()}`}
          className="text-[#ffffff]/60 font-semibold rounded-full hover:text-[#ffffff] transition-all duration-200 whitespace-nowrap active:scale-95"
        >
          {category}
        </a>
      ))}
    </div>
  );
}