import React, {
  createContext,
  useReducer,
  useContext,
  useMemo,
  useCallback,
} from "react";
import {
  getNestedPropertyValue,
  setNestedPropertyValue,
} from "../utils/object-utils";
import { getDefaultFilters } from "../filters/default-filters";

const AppStateContext = createContext();

const initialState = {
  rowsPerPage: 10,
  currentPage: 0,
  sortBy: "auctionEnd",
  orderBy: "DESC",
  filters: getDefaultFilters(),
};

export function AppStateProvider({ children }) {
  const [state, dispatch] = useReducer(reducer, initialState);

  const setSimpleFieldValue = useCallback((fieldName, fieldValue) => {
    dispatch({ type: "set_simple_field_value", fieldName, fieldValue });
  }, []);

  const setSimpleFilterValueFromEvent = useCallback(
    (filterName) => (event) => {
      setSimpleFieldValue("filters." + filterName, event.target.value);
    },
    [setSimpleFieldValue]
  );

  const setSimpleFilterValueFromAutoCompleteEvent = useCallback(
    (filterName) => (event, value, reason) => {
      setSimpleFieldValue("filters." + filterName, value);
    },
    [setSimpleFieldValue]
  );

  const setLimit = useCallback(
    (value) => {
      setSimpleFieldValue("limit", value);
    },
    [setSimpleFieldValue]
  );

  const setOffset = useCallback(
    (value) => {
      setSimpleFieldValue("offset", value);
    },
    [setSimpleFieldValue]
  );

  const setSortBy = useCallback(
    (value) => {
      setSimpleFieldValue("sortBy", value);
    },
    [setSimpleFieldValue]
  );

  const setOrderBy = useCallback(
    (value) => {
      setSimpleFieldValue("orderBy", value);
    },
    [setSimpleFieldValue]
  );

  const getFilterValue = useCallback(
    (field) => {
      return getNestedPropertyValue(state, "filters." + field);
    },
    [state]
  );

  const getDomainValue = useCallback(
    (field) => {
      return getNestedPropertyValue(state, "domain." + field);
    },
    [state]
  );

  const contextValue = useMemo(
    () => ({
      state,
      dispatch,
      setSimpleFieldValue,
      setSimpleFilterValueFromEvent,
      setSimpleFilterValueFromAutoCompleteEvent,
      setLimit,
      setOffset,
      setSortBy,
      setOrderBy,
      getFilterValue,
      getDomainValue,
    }),
    [
      state,
      setSimpleFieldValue,
      setSimpleFilterValueFromEvent,
      setSimpleFilterValueFromAutoCompleteEvent,
      setLimit,
      setOffset,
      setSortBy,
      setOrderBy,
      getFilterValue,
      getDomainValue,
    ]
  );

  return (
    <AppStateContext.Provider value={contextValue}>
      {children}
    </AppStateContext.Provider>
  );
}

export function useAppState() {
  return useContext(AppStateContext);
}

function reducer(state, action) {
  switch (action.type) {
    case "set_simple_field_value": {
      return setNestedPropertyValue(state, action.fieldName, action.fieldValue);
    }
    case "set_rows_per_page": {
      const newRowsPerPage = action.rowsPerPage;
      const totalCount = state.totalCount;
      const isOutOfBounds = newRowsPerPage * state.currentPage > totalCount;
      if (isOutOfBounds) {
        return {
          ...state,
          rowsPerPage: newRowsPerPage,
          currentPage: Math.max(0, Math.ceil(totalCount / newRowsPerPage) - 1),
        };
      } else {
        return { ...state, rowsPerPage: newRowsPerPage };
      }
    }
    case "outfit_selected": {
      if (action.reason !== "selectOption") {
        return { ...state };
      }

      const filterPath = "filters." + action.filterName;
      const currentSelectedOutfits = getNestedPropertyValue(state, filterPath);

      if (
        currentSelectedOutfits.filter(({ name }) => name === action.outfitName)
          .length
      ) {
        return { ...state };
      }

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

const setSimpleFieldValue = (dispatch) => (fieldName, fieldValue) => {
  return dispatch({ type: "set_simple_field_value", fieldName, fieldValue });
};

// eslint-disable-next-line no-unused-vars
const setSimpleFilterValueFromEvent = (dispatch) => (filterName) => (event) => {
  return setSimpleFieldValue(dispatch)(
    "filters." + filterName,
    event.target.value
  );
};

// eslint-disable-next-line no-unused-vars
const setSimpleFilterValueFromAutoCompleteEvent =
  (dispatch) => (filterName) => (event, value, reason) =>
    setSimpleFieldValue(dispatch)("filters." + filterName, value);

// eslint-disable-next-line no-unused-vars
const getFilterValue = (state) => (field) =>
  getNestedPropertyValue(state, "filters." + field);

// eslint-disable-next-line no-unused-vars
const setLimit = (dispatch) => (value) =>
  setSimpleFieldValue(dispatch)("limit", value);

// eslint-disable-next-line no-unused-vars
const setOffset = (dispatch) => (value) =>
  setSimpleFieldValue(dispatch)("offset", value);

// eslint-disable-next-line no-unused-vars
const setSortBy = (dispatch) => (value) =>
  setSimpleFieldValue(dispatch)("sortBy", value);

// eslint-disable-next-line no-unused-vars
const setOrderBy = (dispatch) => (value) =>
  setSimpleFieldValue(dispatch)("orderBy", value);
