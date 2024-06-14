import { useEffect, useReducer } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import useSWRImmutable from "swr/immutable";
import AuctionFilters from "./components/auction-filters/auction-filters";
import AuctionTable from "./components/auction-table/auction-table";
import {
  getNestedPropertyValue,
  setNestedPropertyValue,
} from "./utils/object-utils";

import "./App.css";
import { buildAuctionSearch } from "./filters/filters-to-auction-search";

function App({
  initialSearchId,
  initialLimit,
  initialOffset,
  initialSortBy,
  initialOrderBy,
  initialFilters,
}) {
  const [state, dispatch] = useReducer(reducer, {
    searchId: initialSearchId,
    rowsPerPage: initialLimit,
    currentPage: Math.floor(initialOffset / initialLimit),
    sortBy: initialSortBy,
    orderBy: initialOrderBy,
    totalCount: null,
    filters: initialFilters,
  });

  const navigate = useNavigate();

  const setSimpleFieldValue = (fieldName, fieldValue) => {
    return dispatch({ type: "set_simple_field_value", fieldName, fieldValue });
  };

  const setSimpleFilterValueFromEvent = (filterName) => (event) => {
    return setSimpleFieldValue("filters." + filterName, event.target.value);
  };

  const setSimpleFilterValueFromAutoCompleteEvent =
    (filterName) => (event, value, reason) =>
      setSimpleFieldValue("filters." + filterName, value);

  const getFilterValue = (field) =>
    getNestedPropertyValue(state, "filters." + field);

  const { data: auctions, isLoading: isLoadingAuctions } = useSWRImmutable(
    [
      "http://localhost:8080" +
        getAuctionResultsPath(
          state.searchId,
          state.rowsPerPage,
          state.currentPage,
          state.sortBy,
          state.orderBy
        ),
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

  const {
    data: domain,
    error: errorDomain,
    isLoading: isLoadingDomain,
  } = useSWRImmutable(
    [`http://localhost:8080/api/v1/auctions/domain`],
    async ([url]) => {
      const result = await axios.get(url);
      setSimpleFieldValue("domain", result.data);
      return result.data;
    },
    { revalidateOnFocus: false }
  );

  const onClickSearch = () => {
    const createAuctionSearch = async () => {
      const result = await axios.post(
        "http://localhost:8080/api/v1/auctions/search",
        buildAuctionSearch(state.filters)
      );
      setSimpleFieldValue("searchId", result.data);
    };

    createAuctionSearch();
  };

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

  if (errorDomain) return <div>Something went wrong, please try again.</div>;

  if (isLoadingDomain) {
    return <div>Loading ...</div>;
  }

  return (
    <>
      <AuctionFilters
        domain={domain}
        onClickSearch={onClickSearch}
        name={getFilterValue("name")}
        onChangeName={setSimpleFilterValueFromEvent("name")}
        level={getFilterValue("level")}
        onChangeLevel={setSimpleFilterValueFromEvent("level")}
        selectedVocations={getFilterValue("vocations")}
        onChangeSelectedVocations={setSimpleFilterValueFromAutoCompleteEvent(
          "vocations"
        )}
        axeFighting={getFilterValue("axeFighting")}
        onChangeAxeFighting={setSimpleFilterValueFromEvent("axeFighting")}
        clubFighting={getFilterValue("clubFighting")}
        onChangeClubFighting={setSimpleFilterValueFromEvent("clubFighting")}
        swordFighting={getFilterValue("swordFighting")}
        onChangeSwordFighting={setSimpleFilterValueFromEvent("swordFighting")}
        fistFighting={getFilterValue("fistFighting")}
        onChangeFistFighting={setSimpleFilterValueFromEvent("fistFighting")}
        distanceFighting={getFilterValue("distanceFighting")}
        onChangeDistanceFighting={setSimpleFilterValueFromEvent(
          "distanceFighting"
        )}
        magicLevel={getFilterValue("magicLevel")}
        onChangeMagicLevel={setSimpleFilterValueFromEvent("magicLevel")}
        shielding={getFilterValue("shielding")}
        onChangeShielding={setSimpleFilterValueFromEvent("shielding")}
        fishing={getFilterValue("fishing")}
        onChangeFishing={setSimpleFilterValueFromEvent("fishing")}
        selectedItems={getFilterValue("items")}
        onChangeSelectedItems={setSimpleFilterValueFromAutoCompleteEvent(
          "items"
        )}
        selectedStoreItems={getFilterValue("storeItems")}
        onChangeSelectedStoreItems={setSimpleFilterValueFromAutoCompleteEvent(
          "storeItems"
        )}
        selectedMounts={getFilterValue("mounts")}
        onChangeSelectedMounts={setSimpleFilterValueFromAutoCompleteEvent(
          "mounts"
        )}
        selectedStoreMounts={getFilterValue("storeMounts")}
        onChangeSelectedStoreMounts={setSimpleFilterValueFromAutoCompleteEvent(
          "storeMounts"
        )}
        selectedImbuements={getFilterValue("imbuements")}
        onChangeSelectedImbuements={setSimpleFilterValueFromAutoCompleteEvent(
          "imbuements"
        )}
        selectedCompletedQuestLines={getFilterValue("completedQuestLines")}
        onChangeSelectedCompletedQuestLines={setSimpleFilterValueFromAutoCompleteEvent(
          "completedQuestLines"
        )}
        selectedTitles={getFilterValue("titles")}
        onChangeSelectedTitles={setSimpleFilterValueFromAutoCompleteEvent(
          "titles"
        )}
        selectedAchievements={getFilterValue("achievements")}
        onChangeSelectedAchievements={setSimpleFilterValueFromAutoCompleteEvent(
          "achievements"
        )}
        selectedWorlds={getFilterValue("worlds")}
        onChangeSelectedWorlds={setSimpleFilterValueFromAutoCompleteEvent(
          "worlds"
        )}
        selectedOutfits={getFilterValue("outfits")}
        onSelectOutfit={(event, value, reason) =>
          dispatch({
            type: "outfit_selected",
            filterName: "outfits",
            outfitName: value,
            reason,
          })
        }
        onDeleteOutfit={(outfitName) => () =>
          dispatch({
            type: "outfit_deleted",
            filterName: "outfits",
            outfitName,
          })}
        onClickAddon={(outfitName, addon) => () =>
          dispatch({
            type: "on_toggle_addon",
            filterName: "outfits",
            outfitName,
            addon,
          })}
        selectedStoreOutfits={getFilterValue("storeOutfits")}
        onSelectStoreOutfit={(event, value, reason) =>
          dispatch({
            type: "outfit_selected",
            filterName: "storeOutfits",
            outfitName: value,
            reason,
          })
        }
        onDeleteStoreOutfit={(outfitName) => () =>
          dispatch({
            type: "outfit_deleted",
            filterName: "storeOutfits",
            outfitName,
          })}
        onClickStoreAddon={(outfitName, addon) => () =>
          dispatch({
            type: "on_toggle_addon",
            filterName: "storeOutfits",
            outfitName,
            addon,
          })}
      />
      <AuctionTable
        auctions={isLoadingAuctions ? [["Loading Data..."]] : auctions}
        totalCount={state.totalCount}
        currentPage={state.currentPage}
        onChangeRowsPerPage={(newValue) =>
          setSimpleFieldValue("rowsPerPage", newValue)
        }
        onChangePage={(newValue) =>
          setSimpleFieldValue("currentPage", newValue)
        }
        onChangeSortBy={(newValue) => setSimpleFieldValue("sortBy", newValue)}
        onChangeOrderBy={(newValue) => setSimpleFieldValue("orderBy", newValue)}
      />
    </>
  );
}

function reducer(state, action) {
  switch (action.type) {
    case "set_simple_field_value": {
      return setNestedPropertyValue(state, action.fieldName, action.fieldValue);
    }
    case "outfit_selected": {
      if (action.reason !== "selectOption") {
        return { ...state };
      }

      const filterPath = "filters." + action.filterName;
      const currentSelectedOutfits = getNestedPropertyValue(state, filterPath);
      const newSelectedOutfits = [
        ...currentSelectedOutfits,
        { name: action.outfitName, firstAddon: false, secondAddon: false },
      ];
      return setNestedPropertyValue(state, filterPath, newSelectedOutfits);
    }
    case "outfit_deleted": {
      const filterPath = "filters." + action.filterName;
      const currentSelectedOutfits = getNestedPropertyValue(state, filterPath);
      const newSelectedOutfits = currentSelectedOutfits.filter(
        ({ name }) => name !== action.outfitName
      );
      return setNestedPropertyValue(state, filterPath, newSelectedOutfits);
    }
    case "on_toggle_addon": {
      const filterPath = "filters." + action.filterName;
      const currentSelectedOutfits = getNestedPropertyValue(state, filterPath);
      const newSelectedOutfits = currentSelectedOutfits.map((outfit) => {
        if (outfit.name !== action.outfitName) {
          return outfit;
        }

        return {
          ...outfit,
          [action.addon]: !outfit[action.addon],
        };
      });
      return setNestedPropertyValue(state, filterPath, newSelectedOutfits);
    }
    default:
      throw Error("Unknown action type: " + action.type);
  }
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
