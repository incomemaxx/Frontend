// hooks/useHomeData.ts
import { useState, useEffect } from "react";
import { marketService } from "../services/marketService";
import type { HomePageData } from "../store/market";

export function useHomeData() {
  const [data, setData] = useState<HomePageData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const homeData = await marketService.getHomeData();
        setData(homeData);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Something went wrong");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []); // single fetch on mount, no frequent requests

  return { data, loading, error };
}