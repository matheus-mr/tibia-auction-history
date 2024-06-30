import { useAppState } from "../AppStateContext";
import BasicAutoComplete from "../common/BasicAutoComplete";

export default function WorldFilter() {
  const {
    setSimpleFilterValueFromAutoCompleteEvent,
    getFilterValue,
    getDomainValue,
  } = useAppState();

  return (
    <BasicAutoComplete
      id="worlds"
      label="World"
      options={getDomainValue("worlds")}
      selectedOptions={getFilterValue("worlds")}
      onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
        "worlds"
      )}
    />
  );
}
