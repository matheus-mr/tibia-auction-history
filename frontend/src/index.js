import React, { useEffect, useState } from "react";
import ReactDOM from "react-dom/client";
import {
  createBrowserRouter,
  RouterProvider,
  useParams,
  useSearchParams,
} from "react-router-dom";
import axios from "axios";
import useSWRImmutable from "swr/immutable";
import { buildFilters } from "./filters/auction-search-to-filters";
import { getSearchParamOrDefault } from "./utils/react-router-utils";
import App from "./components/App";

import "./index.css";
import { AppStateProvider, useAppState } from "./components/AppStateContext";

const getParamLimit = getSearchParamOrDefault("limit", 10);
const getParamOffset = getSearchParamOrDefault("offset", 0);
const getParamSortBy = getSearchParamOrDefault("sortBy", "auctionEnd");
const getParamOrderBy = getSearchParamOrDefault("orderBy", "DESC");

const AppWithSearch = () => {
  const { searchId } = useParams();
  const [searchParams] = useSearchParams();
  const [initialFilters, setInitialFilters] = useState();
  const shouldFetchFilters = !initialFilters;

  const { setLimit, setOffset, setSortBy, setOrderBy } = useAppState();
  const limit = getParamLimit(searchParams);
  const offset = getParamOffset(searchParams);
  const sortBy = getParamSortBy(searchParams);
  const orderBy = getParamOrderBy(searchParams);

  useEffect(() => setLimit(limit), [limit, setLimit]);
  useEffect(() => setOffset(offset), [offset, setOffset]);
  useEffect(() => setSortBy(sortBy), [sortBy, setSortBy]);
  useEffect(() => setOrderBy(orderBy), [orderBy, setOrderBy]);

  const { isLoading: isLoadingAuctionSearch } = useSWRImmutable(
    shouldFetchFilters
      ? [`http://localhost:8080/api/v1/auctions/search/${searchId}`, searchId]
      : null,
    async ([url]) => {
      const result = await axios.get(url);
      const auctionSearch = result.data;
      const filters = buildFilters(auctionSearch.criteria.criterias);
      setInitialFilters(filters);
      return auctionSearch;
    },
    { revalidateOnFocus: false }
  );

  if (isLoadingAuctionSearch || !initialFilters) {
    return <div>Loading ...</div>;
  }

  return <App />;
};

const AppWithoutSearch = () => {
  const [searchParams] = useSearchParams();
  const { setLimit, setOffset, setSortBy, setOrderBy } = useAppState();
  const limit = getParamLimit(searchParams);
  const offset = getParamOffset(searchParams);
  const sortBy = getParamSortBy(searchParams);
  const orderBy = getParamOrderBy(searchParams);

  useEffect(() => setLimit(limit), [limit, setLimit]);
  useEffect(() => setOffset(offset), [offset, setOffset]);
  useEffect(() => setSortBy(sortBy), [sortBy, setSortBy]);
  useEffect(() => setOrderBy(orderBy), [orderBy, setOrderBy]);

  return <App />;
};

const router = createBrowserRouter([
  {
    path: "/",
    element: <AppWithoutSearch />,
  },
  {
    path: "/api/v1/auctions",
    element: <AppWithoutSearch />,
  },
  {
    path: "/api/v1/auctions/search/:searchId/results",
    element: <AppWithSearch />,
  },
]);

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <AppStateProvider>
      <RouterProvider router={router} />
    </AppStateProvider>
  </React.StrictMode>
);
