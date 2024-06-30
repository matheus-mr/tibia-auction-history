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
  const { state, setSimpleFieldValue } = useAppState();

  const navigate = useNavigate();

  const {
    data: auctions,
    isLoading: isLoadingAuctions,
    error: isErrorAuctions,
  } = useSWRImmutable(
    [
      "http://localhost:8080" +
        getAuctionResultsPath(
          state.searchId,
          state.rowsPerPage,
          state.currentPage,
          state.sortBy,
          state.orderBy
        ),
      state.searchId,
      state.rowsPerPage,
      state.currentPage,
      state.sortBy,
      state.orderBy,
    ],
    async ([url]) => {
      const result = await axios.get(url);
      setSimpleFieldValue(
        "totalCount",
        getTotalCountFromHeaders(result.headers)
      );
      return result.data;
    },
    { revalidateOnFocus: false }
  );

  const { error: isErrorDomain, isLoading: isLoadingDomain } = useSWRImmutable(
    [`http://localhost:8080/api/v1/auctions/domain`],
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

  const onClickSearch = useCallback(() => {
    const createAuctionSearch = async () => {
      const result = await axios.post(
        "http://localhost:8080/api/v1/auctions/search",
        buildAuctionSearch(state.filters)
      );
      setSimpleFieldValue("searchId", result.data);
    };

    createAuctionSearch();
  }, [setSimpleFieldValue, state.filters]);

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
      <AuctionFilters onClickSearch={onClickSearch} />
      <AuctionTable
        auctions={auctions}
        isLoadingAuctions={isLoadingAuctions}
        isErrorAuctions={isErrorAuctions}
      />
    </>
  );
}

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

const getTotalCountFromHeaders = (headers) => {
  const totalCountStr = headers["x-total-count"];
  return totalCountStr !== undefined ? parseInt(totalCountStr) : null;
};

export default App;
