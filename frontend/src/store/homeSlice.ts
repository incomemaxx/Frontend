import { createSlice } from "@reduxjs/toolkit";

interface Candidate {
  name: string;
  image: string;
  payout: string;
  odds: string;
  color: string;
}

interface MarketCardData {
  title: string;
  candidates: Candidate[];
  volume: string;
  marketCount: string;
  newsText: string;
  currentIndex: number;
  totalCards: number;
}

interface InfoCardData {
  id: string;
  title: string;
  description: string;
}

interface Option {
  label: string;
  payout: string;
  odds: string;
  color: string;
}

interface TopicCardData {
  id: string;
  category: string;
  title: string;
  date: string;
  options: Option[];
  volume: string;
  marketCount: string;
}

interface TopicSection {
  id: string;
  heading: string;
  cards: TopicCardData[];
}

interface HomeState {
  marketCard: MarketCardData;
  infoCards: InfoCardData[];
  topicSections: TopicSection[];
}

const initialState: HomeState = {
  marketCard: {
    title: "2028 U.S. Presidential Election winner?",
    candidates: [
      { name: "Gavin Newsom", image: "https://via.placeholder.com/40", payout: "5.25x", odds: "18%", color: "#4a9eff" },
      { name: "J.D. Vance", image: "https://via.placeholder.com/40", payout: "5.25x", odds: "17%", color: "#00d4aa" },
      { name: "Marco Rubio", image: "https://via.placeholder.com/40", payout: "5.25x", odds: "18%", color: "#ffa500" },
    ],
    volume: "$17,834,984",
    marketCount: "22",
    newsText:
      "Secretary of State Marco Rubio's chances in the race for the 2028 Republican presidential nomination have risen amid the U.S. conflict with Iran, Newsweek reports. Vice President JD Vance...",
    currentIndex: 1,
    totalCards: 7,
  },
  infoCards: [
    { id: "1", title: "Markets over Monopolies", description: "How fair markets protect consumers" },
    { id: "2", title: "Responsible Trading", description: "Tools and tips for trading smart" },
    { id: "3", title: "Market Integrity", description: "Learn how Max prevents insider trading" },
  ],
  topicSections: [
    {
      id: "politics",
      heading: "Politics",
      cards: [
        {
          id: "p1", category: "CONGRESS",
          title: "How long will the government shutdown last?",
          date: "Feb 14 @ 10:00AM",
          options: [
            { label: "At least 60 days", payout: "2.05x", odds: "46%", color: "#48CAE4" },
            { label: "At least 55 days", payout: "1.65x", odds: "59%", color: "#48CAE4" },
          ],
          volume: "$4,199,600", marketCount: "21",
        },
        {
          id: "p2", category: "CONGRESS",
          title: "Will members of Congress be banned from trading stocks?",
          date: "Before Jan 21, 2029",
          options: [
            { label: "Before Jan 21, 2029", payout: "1.59x", odds: "56%", color: "#00d4aa" },
            { label: "Before 2027", payout: "7.14x", odds: "12%", color: "#00d4aa" },
          ],
          volume: "$222,525", marketCount: "3",
        },
        {
          id: "p3", category: "TRUMP",
          title: "Will Americans receive tariff stimulus checks?",
          date: "Before 2027",
          options: [
            { label: "Before 2027", payout: "5.25x", odds: "18%", color: "#48CAE4" },
            { label: "Before August", payout: "10.4x", odds: "9%", color: "#4a9eff" },
          ],
          volume: "$1,334,816", marketCount: "9",
        },
        {
          id: "p4", category: "INTERNATIONAL",
          title: "What will the US tariff rate on China be on July 1?",
          date: "Between 10% and 19.99%",
          options: [
            { label: "Between 10% and 19.99%", payout: "1.47x", odds: "69%", color: "#00d4aa" },
            { label: "Between 20% and 29.99%", payout: "4.35x", odds: "27%", color: "#00d4aa" },
          ],
          volume: "$36,656", marketCount: "7",
        },
      ],
    },
    {
      id: "elections",
      heading: "Elections",
      cards: [
        {
          id: "e1", category: "PRESIDENTIAL",
          title: "Who will win the 2028 Presidential Election?",
          date: "Nov 5, 2028",
          options: [
            { label: "Republican", payout: "1.85x", odds: "52%", color: "#ff4444" },
            { label: "Democrat", payout: "2.15x", odds: "45%", color: "#4444ff" },
          ],
          volume: "$8,500,000", marketCount: "15",
        },
        {
          id: "e2", category: "SENATE",
          title: "Will Democrats control the Senate in 2026?",
          date: "Jan 3, 2027",
          options: [
            { label: "Yes", payout: "2.85x", odds: "35%", color: "#4444ff" },
            { label: "No", payout: "1.55x", odds: "65%", color: "#ff4444" },
          ],
          volume: "$2,100,000", marketCount: "8",
        },
      ],
    },
    {
      id: "companies",
      heading: "Companies",
      cards: [
        {
          id: "c1", category: "TECH",
          title: "Will Apple stock hit $250 by end of 2025?",
          date: "Dec 31, 2025",
          options: [
            { label: "Yes", payout: "2.25x", odds: "44%", color: "#00d4aa" },
            { label: "No", payout: "1.75x", odds: "56%", color: "#ff6b6b" },
          ],
          volume: "$3,450,000", marketCount: "12",
        },
        {
          id: "c2", category: "AUTOMOTIVE",
          title: "Will Tesla deliver 2M vehicles in 2025?",
          date: "Dec 31, 2025",
          options: [
            { label: "Yes", payout: "3.15x", odds: "32%", color: "#00d4aa" },
            { label: "No", payout: "1.45x", odds: "68%", color: "#ff6b6b" },
          ],
          volume: "$1,890,000", marketCount: "6",
        },
      ],
    },
    {
      id: "economics",
      heading: "Economics",
      cards: [
        {
          id: "ec1", category: "INFLATION",
          title: "Will inflation rate be below 2% by end of 2025?",
          date: "Dec 31, 2025",
          options: [
            { label: "Yes", payout: "1.95x", odds: "51%", color: "#00d4aa" },
            { label: "No", payout: "2.05x", odds: "49%", color: "#ff6b6b" },
          ],
          volume: "$5,200,000", marketCount: "18",
        },
        {
          id: "ec2", category: "FED",
          title: "Will the Fed cut rates in Q2 2025?",
          date: "Jun 30, 2025",
          options: [
            { label: "Yes", payout: "2.65x", odds: "38%", color: "#00d4aa" },
            { label: "No", payout: "1.62x", odds: "62%", color: "#ff6b6b" },
          ],
          volume: "$6,750,000", marketCount: "22",
        },
      ],
    },
  ],
};

const homeSlice = createSlice({
  name: "home",
  initialState,
  reducers: {},
});

export default homeSlice.reducer;
