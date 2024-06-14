import React, { useState } from "react";
import ReactDOM from "react-dom/client";
import {
  createBrowserRouter,
  RouterProvider,
  useParams,
  useSearchParams,
} from "react-router-dom";
import axios from "axios";
import useSWRImmutable from "swr/immutable";
import App from "./App";

import "./index.css";
import { buildFilters } from "./filters/auction-search-to-filters";
import { getDefaultFilters } from "./filters/default-filters";

const getParamOrDefault = (paramName, defaultValue) => (params) =>
  params.has(paramName) ? params.get(paramName) : defaultValue;
const getParamLimit = getParamOrDefault("limit", 10);
const getParamOffset = getParamOrDefault("offset", 0);
const getParamSortBy = getParamOrDefault("sortBy", "auctionEnd");
const getParamOrderBy = getParamOrDefault("orderBy", "DESC");

const AppWithSearch = () => {
  const params = useParams();
  const [searchParams] = useSearchParams();
  const searchId = params.searchId;
  const [filters, setFilters] = useState();

  const { isLoading: isLoadingAuctionSearch } = useSWRImmutable(
    [`http://localhost:8080/api/v1/auctions/search/${searchId}`, searchId],
    async ([url]) => {
      const result = await axios.get(url);
      const auctionSearch = result.data;
      const filters = buildFilters(auctionSearch);
      setFilters(filters);
      return auctionSearch;
    },
    { revalidateOnFocus: false }
  );

  if (isLoadingAuctionSearch || filters === undefined) {
    return <div>Loading ...</div>;
  }

  return (
    <App
      initialSearchId={searchId}
      initialLimit={getParamLimit(searchParams)}
      initialOffset={getParamOffset(searchParams)}
      initialSortBy={getParamSortBy(searchParams)}
      initialOrderBy={getParamOrderBy(searchParams)}
      initialFilters={filters}
    />
  );
};

const AppWithoutSearch = () => {
  const [searchParams] = useSearchParams();

  return (
    <App
      initialLimit={getParamLimit(searchParams)}
      initialOffset={getParamOffset(searchParams)}
      initialSortBy={getParamSortBy(searchParams)}
      initialOrderBy={getParamOrderBy(searchParams)}
      initialFilters={getDefaultFilters()}
    />
  );
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
    <RouterProvider router={router} />
  </React.StrictMode>
);
