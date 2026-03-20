// For MarketCard trending options (WITH images)
export interface TrendingOption {
  label: string;
  image: string;       // image shown in MarketCard
  payout: string;
  odds: string;
  color: string;
}

// For TopicCard options (WITHOUT images)
export interface TopicOption {
  label: string;
  payout: string;
  odds: string;
  color: string;
}

// Each trending topic shown in MarketCard
export interface TrendingMarket {
  id: string;
  title: string;
  options: TrendingOption[];   // has images
  volume: string;
  marketCount: string;
  newsText: string;
}

// Each title/market inside a category
export interface MarketTitle {
  id: string;
  title: string;
  date: string;
  options: TopicOption[];      // no images
  volume: string;
  marketCount: string;
}

// A full category like Politics, Comedy, etc.
export interface MarketCategory {
  categoryId: string;
  categoryName: string;        // "Politics", "Comedy", etc.
  categoryIcon: string;        // icon identifier or url
  markets: MarketTitle[];      // backend sends ALL, frontend shows top 4
}

// Full API response shape for home page
export interface HomePageData {
  trending: TrendingMarket[];       // variable count, for MarketCard
  categories: MarketCategory[];     // all categories with all their markets
}