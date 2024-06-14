export const buildAuctionSearch = (filters) => {
  return {
    operator: "AND",
    criterias: [
      buildNameCriteria(filters.name),
      buildLevelCriteria(filters.level),
      buildVocationsCriteria(filters.vocations),
    ].filter((criteria) => criteria !== null),
  };
};

const buildNameCriteria = (name) => {
  if (typeof name !== "string" || isEmptyString(name)) {
    return null;
  }

  return {
    field: "name",
    operator: "regex",
    values: [name.split(" ").join("|")],
  };
};

const buildLevelCriteria = (level) => {
  if (!level) {
    return null;
  }

  return {
    field: "level",
    operator: "gte",
    values: [level],
  };
};

const buildVocationsCriteria = (vocations) => {
  if (!vocations) {
    return null;
  }

  return {
    field: "vocation",
    operator: "in",
    values: vocations,
  };
};

const isEmptyString = (str) => str === null || str.match(/^\s*$/) !== null;
