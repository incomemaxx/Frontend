// mocks/homeData.mock.ts
import type { HomePageData } from "../store/market";

export const mockHomeData: HomePageData = {
  trending: [
    {
      id: "t1",
      title: "2028 U.S. Presidential Election winner?",
      options: [
        { label: "Gavin Newsom", image: "https://i.pravatar.cc/40?img=1", payout: "5.25x", odds: "18%", color: "#4a9eff" },
        { label: "J.D. Vance",   image: "https://i.pravatar.cc/40?img=2", payout: "5.25x", odds: "17%", color: "#00d4aa" },
        { label: "Marco Rubio",  image: "https://i.pravatar.cc/40?img=3", payout: "5.25x", odds: "18%", color: "#ffa500" },
      ],
      volume: "$17,834,984",
      marketCount: "22",
      newsText: "Secretary of State Marco Rubio's chances in the race for the 2028 Republican presidential nomination have risen...",
    },
    {
      id: "t2",
      title: "Will the Fed cut rates in Q2 2025?",
      options: [
        { label: "Yes", image: "https://i.pravatar.cc/40?img=4", payout: "2.65x", odds: "38%", color: "#00d4aa" },
        { label: "No",  image: "https://i.pravatar.cc/40?img=5", payout: "1.62x", odds: "62%", color: "#ff6b6b" },
      ],
      volume: "$6,750,000",
      marketCount: "22",
      newsText: "Federal Reserve officials remain cautious about cutting rates amid persistent inflation...",
    },
  ],
  categories: [
    {
      categoryId: "politics",
      categoryName: "Politics",
      categoryIcon: "shield",
      markets: [
        {
          id: "m1",
          title: "How long will the government shutdown last?",
          date: "Feb 14 @ 10:00AM",
          options: [
            { label: "At least 60 days", payout: "2.05x", odds: "46%", color: "#48CAE4" },
            { label: "At least 55 days", payout: "1.65x", odds: "59%", color: "#48CAE4" },
          ],
          volume: "$4,199,600",
          marketCount: "21",
        },
        {
          id: "m2",
          title: "Will members of Congress be banned from trading stocks?",
          date: "Before Jan 21, 2029",
          options: [
            { label: "Before Jan 21, 2029", payout: "1.59x", odds: "56%", color: "#00d4aa" },
            { label: "Before 2027",         payout: "7.14x", odds: "12%", color: "#00d4aa" },
          ],
          volume: "$222,525",
          marketCount: "3",
        },
        {
          id: "m3",
          title: "Will Americans receive tariff stimulus checks?",
          date: "Before 2027",
          options: [
            { label: "Before 2027",  payout: "5.25x", odds: "18%", color: "#48CAE4" },
            { label: "Before August", payout: "10.4x", odds: "9%",  color: "#4a9eff" },
          ],
          volume: "$1,334,816",
          marketCount: "9",
        },
        {
          id: "m4",
          title: "What will the US tariff rate on China be on July 1?",
          date: "Jul 1, 2025",
          options: [
            { label: "Between 10% and 19.99%", payout: "1.47x", odds: "69%", color: "#00d4aa" },
            { label: "Between 20% and 29.99%", payout: "4.35x", odds: "27%", color: "#00d4aa" },
          ],
          volume: "$36,656",
          marketCount: "7",
        },
        // This 5th one won't show — slice(0,4) will cut it
        {
          id: "m5",
          title: "Will Biden endorse a 2028 candidate before 2026?",
          date: "Dec 31, 2025",
          options: [
            { label: "Yes", payout: "3.10x", odds: "30%", color: "#4a9eff" },
            { label: "No",  payout: "1.42x", odds: "70%", color: "#ff6b6b" },
          ],
          volume: "$980,000",
          marketCount: "5",
        },
      ],
    },
    {
      categoryId: "comedy",
      categoryName: "Comedy",
      categoryIcon: "laugh",
      markets: [
        {
          id: "c1",
          title: "Will SNL get cancelled in 2025?",
          date: "Dec 31, 2025",
          options: [
            { label: "Yes", payout: "8.00x", odds: "12%", color: "#ffa500" },
            { label: "No",  payout: "1.14x", odds: "88%", color: "#00d4aa" },
          ],
          volume: "$450,000",
          marketCount: "4",
        },
        {
          id: "c2",
          title: "Will a stand-up special win an Emmy in 2025?",
          date: "Sep 30, 2025",
          options: [
            { label: "Yes", payout: "2.50x", odds: "40%", color: "#00d4aa" },
            { label: "No",  payout: "1.65x", odds: "60%", color: "#ff6b6b" },
          ],
          volume: "$120,000",
          marketCount: "2",
        },
        {
          id: "c3",
          title: "Will Netflix release 10+ comedy specials in 2025?",
          date: "Dec 31, 2025",
          options: [
            { label: "Yes", payout: "1.35x", odds: "74%", color: "#00d4aa" },
            { label: "No",  payout: "3.85x", odds: "26%", color: "#ff6b6b" },
          ],
          volume: "$89,000",
          marketCount: "3",
        },
        {
          id: "c4",
          title: "Will a comedian host the Oscars in 2026?",
          date: "Mar 2026",
          options: [
            { label: "Yes", payout: "1.75x", odds: "57%", color: "#4a9eff" },
            { label: "No",  payout: "2.25x", odds: "43%", color: "#ff6b6b" },
          ],
          volume: "$200,000",
          marketCount: "6",
        },
      ],
    },
  ],
};