// pages/Home.tsx
import { useState } from "react";
import { useHomeData } from "../hooks/useHomeData";
import Header from "../components/Header/Header";
import Footer from "../components/Footer/Footer";
import MarketCard from "../components/MarketCard/MarketCard";
import TopicCard from "../components/TopicCard/TopicCard";

function LoadingSkeleton() {
  return (
    <div className="min-h-screen bg-[#000000] flex items-center justify-center">
      <div className="text-[#aaaaaa] text-sm animate-pulse">Loading markets...</div>
    </div>
  );
}

function ErrorState({ message }: { message: string }) {
  return (
    <div className="min-h-screen bg-[#000000] flex items-center justify-center">
      <div className="text-red-400 text-sm">{message}</div>
    </div>
  );
}

const TOPICS_TO_SHOW = 4; // controls how many TopicCards show per category

export default function Home() {
  const { data, loading, error } = useHomeData();
  const [currentTrendingIndex, setCurrentTrendingIndex] = useState(0);

  if (loading) return <LoadingSkeleton />;
  if (error)   return <ErrorState message={error} />;
  if (!data)   return null;

  const currentTrending = data.trending[currentTrendingIndex];

  return (
    <div className="min-h-screen bg-[#000000]">
      <Header />

      <main className="max-w-7xl mx-auto px-4 py-8">

        {/* ✅ MarketCard - driven by trending array from backend */}
        {currentTrending && (
          <MarketCard
            title={currentTrending.title}
            options={currentTrending.options}       // options with images
            volume={currentTrending.volume}
            marketCount={currentTrending.marketCount}
            newsText={currentTrending.newsText}
            currentIndex={currentTrendingIndex + 1}
            totalCards={data.trending.length}       // dynamic count from backend
            onNext={() =>
              setCurrentTrendingIndex((i) =>
                i < data.trending.length - 1 ? i + 1 : 0
              )
            }
            onPrev={() =>
              setCurrentTrendingIndex((i) =>
                i > 0 ? i - 1 : data.trending.length - 1
              )
            }
          />
        )}

        {/* ✅ Categories - backend sends all markets, frontend shows top 4 */}
        {data.categories.map((category) => (
          <div key={category.categoryId} className="mt-12">

            {/* Section Header */}
            <div className="flex items-center gap-2 mb-6">
              <h2 className="text-white text-2xl font-bold">
                {category.categoryName}
              </h2>
              <svg className="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
              </svg>
            </div>

            {/* Top 4 only — slice happens here in frontend */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {category.markets.slice(0, TOPICS_TO_SHOW).map((market) => (
                <TopicCard
                  key={market.id}
                  category={category.categoryName.toUpperCase()}
                  title={market.title}
                  date={market.date}
                  options={market.options}          // options WITHOUT images
                  volume={market.volume}
                  marketCount={market.marketCount}
                />
              ))}
            </div>

          </div>
        ))}

      </main>

      <Footer />
    </div>
  );
}