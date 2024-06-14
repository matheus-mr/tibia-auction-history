import { getDefaultFilters } from "./default-filters";

export const buildFilters = (auctionSearch) => {
  return auctionSearch.criteria.criterias.reduce(
    (accumulator, currentValue) => ({
      ...accumulator,
      ...buildCriteria(currentValue),
    }),
    getDefaultFilters()
  );
};

const buildCriteria = (criteria) => {
  switch (criteria.field) {
    case "name": {
      return {
        name: criteria.values[0].replace("|", " "),
      };
    }
    default: {
      throw Error("Unknown criteria field: " + criteria.field);
    }
  }
};
