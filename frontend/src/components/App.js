import { useCallback, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import useSWRImmutable from "swr/immutable";
import AuctionFilters from "./auction-filters/AuctionFilters";
import AuctionTable from "./auction-table/AuctionTable";
import { buildAuctionSearch } from "../filters/filters-to-auction-search";
import { VOCATION_KEY_TO_LABEL } from "../constants";
import { useAppState } from "./AppStateContext";

import "./App.css";

function App() {
  const { state, setSimpleFieldValue, resetFilters } = useAppState();

  const navigate = useNavigate();

  const {
    data: auctions,
    isLoading: isLoadingAuctions,
    error: isErrorAuctions,
  } = useSWRImmutable(
    getAuctionKey(
      state.searchId,
      state.rowsPerPage,
      state.currentPage,
      state.sortBy,
      state.orderBy
    ),
    auctionFetcher,
    { revalidateOnFocus: false }
  );

  const { error: isErrorDomain, isLoading: isLoadingDomain } = useSWRImmutable(
    [`${process.env.REACT_APP_API_URL}/api/v1/auctions/domain`],
    async ([url]) => {
      const result = await axios.get(url);
      const resultData = {
        ...result.data,
        vocations: result.data.vocations.map((vocationKey) => ({
          key: vocationKey,
          name: VOCATION_KEY_TO_LABEL[vocationKey],
        })),
      };
      setSimpleFieldValue("domain", resultData);
      return resultData;
    },
    { revalidateOnFocus: false }
  );

  const onClickSearchButton = useCallback(() => {
    const createAuctionSearch = async () => {
      const result = await axios.post(
        `${process.env.REACT_APP_API_URL}/api/v1/auctions/search`,
        buildAuctionSearch(state.filters)
      );
      setSimpleFieldValue("searchId", result.data);
    };

    createAuctionSearch();
  }, [setSimpleFieldValue, state.filters]);

  const onClickResetFiltersButton = useCallback(() => {
    resetFilters();
  }, [resetFilters]);

  useEffect(
    () =>
      navigate(
        getAuctionResultsPath(
          state.searchId,
          state.rowsPerPage,
          state.currentPage,
          state.sortBy,
          state.orderBy
        )
      ),
    [
      navigate,
      state.searchId,
      state.rowsPerPage,
      state.currentPage,
      state.sortBy,
      state.orderBy,
    ]
  );

  if (isErrorDomain) return <div>Something went wrong, please try again.</div>;

  if (isLoadingDomain || !state.domain) {
    return <div>Loading ...</div>;
  }

  return (
    <>
      <AuctionFilters
        onClickSearchButton={onClickSearchButton}
        onClickResetFiltersButton={onClickResetFiltersButton}
      />
      <AuctionTable
        auctions={auctions}
        isLoadingAuctions={isLoadingAuctions}
        isErrorAuctions={isErrorAuctions}
      />
    </>
  );
}

const auctionFetcher = async ([url]) => {
  const result = await axios.get(url);
  return result.data;
};

const getAuctionKey = (searchId, rowsPerPage, page, sortBy, orderBy) => [
  process.env.REACT_APP_API_URL +
    getAuctionResultsPath(searchId, rowsPerPage, page, sortBy, orderBy),
  searchId,
  rowsPerPage,
  page,
  sortBy,
  orderBy,
];

const getAuctionResultsPath = (
  searchId,
  rowsPerPage,
  currentPage,
  sortBy,
  orderBy
) =>
  `${
    searchId
      ? `/api/v1/auctions/search/${searchId}/results`
      : "/api/v1/auctions"
  }?limit=${rowsPerPage}&offset=${
    currentPage * rowsPerPage
  }&sortBy=${sortBy}&orderBy=${orderBy}`;

export default App;
