export const getSearchParamOrDefault = (paramName, defaultValue) => (params) =>
  params.has(paramName) ? params.get(paramName) : defaultValue;
