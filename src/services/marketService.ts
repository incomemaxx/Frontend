import type { HomePageData } from "../store/market";
import { mockHomeData } from "../mocks/homeData.mock";


const BASE_URL = import.meta.env.VITE_API_URL;
const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true"; // 👈 flag


export const marketService = {
  getHomeData: async (): Promise<HomePageData> => {
    if (USE_MOCK) {
      // Simulates real network delay so loading state is visible
      await new Promise((res) => setTimeout(res, 800));
      return mockHomeData;
    }

    const res = await fetch(`${BASE_URL}/home`);
    if (!res.ok) throw new Error("Failed to fetch home data");
    return res.json();
  },
};