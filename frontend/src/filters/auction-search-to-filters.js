import { VOCATION_KEY_TO_LABEL } from "../constants";
import { getDefaultFilters } from "./default-filters";

export const buildFilters = (criterias) => {
  return criterias.reduce((accumulator, currentValue) => {
    const isLogicOperator = ["and", "or"].includes(
      currentValue.operator.toLowerCase()
    );

    return {
      ...accumulator,
      ...(isLogicOperator
        ? buildFilters(currentValue.criterias)
        : buildCriteria(accumulator, currentValue)),
    };
  }, getDefaultFilters());
};

const buildCriteria = (currentFilters, criteria) => {
  switch (criteria.field) {
    case "name": {
      return {
        name: criteria.values[0].replace("|", " "),
      };
    }
    case "level":
    case "axeFighting":
    case "clubFighting":
    case "swordFighting":
    case "fistFighting":
    case "distanceFighting":
    case "magicLevel":
    case "shielding":
    case "fishing": {
      return {
        [criteria.field]: criteria.values[0],
      };
    }
    case "vocation": {
      return {
        vocations: criteria.values.map((vocationKey) => ({
          key: vocationKey,
          name: VOCATION_KEY_TO_LABEL[vocationKey],
        })),
      };
    }
    case "world": {
      return {
        worlds: criteria.values,
      };
    }
    case "items":
    case "storeItems":
    case "mounts":
    case "storeMounts":
    case "imbuements":
    case "completedQuestLines":
    case "titles":
    case "achievements": {
      return {
        [criteria.field]: criteria.values,
      };
    }
    case "outfits": {
      return {
        outfits: [
          ...currentFilters.outfits,
          criteria.criterias.reduce((accumulator, currentValue) => {
            const field = currentValue.field;
            const firstValue = currentValue.values[0];
            switch (field) {
              case "name": {
                return { ...accumulator, name: firstValue };
              }
              case "hasFirstAddon": {
                return {
                  ...accumulator,
                  firstAddon: firstValue,
                };
              }
              case "hasSecondAddon": {
                return {
                  ...accumulator,
                  secondAddon: firstValue,
                };
              }
              default: {
                throw Error("Unknown outfit field: " + field);
              }
            }
          }, []),
        ],
      };
    }
    default: {
      throw Error("Unknown criteria field: " + criteria.field);
    }
  }
};
