export const setNestedPropertyValue = (obj, path, value) => {
  const [first, ...rest] = path.split(".");
  return {
    ...obj,
    [first]: rest.length
      ? setNestedPropertyValue(obj[first], rest.join("."), value)
      : value,
  };
};

export const getNestedPropertyValue = (obj, path) => {
  const [first, ...rest] = path.split(".");
  return rest.length
    ? getNestedPropertyValue(obj[first], rest.join("."))
    : obj[first];
};
