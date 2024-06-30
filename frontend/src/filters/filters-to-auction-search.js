export const buildAuctionSearch = (filters) => {
  return {
    operator: "AND",
    criterias: [
      buildNameCriteria(filters.name),
      buildIntegerCriteria(filters.level, "level"),
      buildVocationsCriteria(filters.vocations),
      buildArrayCriteria(filters.worlds, "world"),
      buildIntegerCriteria(filters.axeFighting, "axeFighting"),
      buildIntegerCriteria(filters.clubFighting, "clubFighting"),
      buildIntegerCriteria(filters.swordFighting, "swordFighting"),
      buildIntegerCriteria(filters.fistFighting, "fistFighting"),
      buildIntegerCriteria(filters.distanceFighting, "distanceFighting"),
      buildIntegerCriteria(filters.magicLevel, "magicLevel"),
      buildIntegerCriteria(filters.shielding, "shielding"),
      buildIntegerCriteria(filters.fishing, "fishing"),
      buildArrayCriteria(filters.items, "items"),
      buildArrayCriteria(filters.storeItems, "storeItems"),
      buildArrayCriteria(filters.mounts, "mounts"),
      buildArrayCriteria(filters.storeMounts, "storeMounts"),
      buildArrayCriteria(filters.imbuements, "imbuements"),
      buildArrayCriteria(filters.completedQuestLines, "completedQuestLines"),
      buildArrayCriteria(filters.titles, "titles"),
      buildArrayCriteria(filters.achievements, "achievements"),
      buildOutfitsCriteria(filters.outfits),
      buildOutfitsCriteria(filters.storeOutfits),
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

const buildVocationsCriteria = (vocations) => {
  if (!vocations.length) {
    return null;
  }

  return {
    field: "vocation",
    operator: "in",
    values: vocations.map((vocation) => vocation.key),
  };
};

const buildOutfitsCriteria = (outfits) => {
  if (!outfits.length) {
    return null;
  }

  return {
    operator: "AND",
    criterias: outfits.map(({ name, firstAddon, secondAddon }) => ({
      field: "outfits",
      operator: "elemMatch",
      criterias: [
        { field: "name", operator: "eq", values: [name] },
        { field: "hasFirstAddon", operator: "eq", values: [firstAddon] },
        { field: "hasSecondAddon", operator: "eq", values: [secondAddon] },
      ],
    })),
  };
};

const buildIntegerCriteria = (value, field) => {
  if (!value) {
    return null;
  }

  return {
    field,
    operator: "gte",
    values: [parseInt(value)],
  };
};

const buildArrayCriteria = (arr, field) => {
  if (!arr.length) {
    return null;
  }

  return {
    field,
    operator: "in",
    values: arr,
  };
};

const isEmptyString = (str) => str === null || str.match(/^\s*$/) !== null;
