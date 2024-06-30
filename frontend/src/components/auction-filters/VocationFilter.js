import { useAppState } from "../AppStateContext";
import BasicAutoComplete from "../common/BasicAutoComplete";

export default function VocationFilter() {
  const {
    setSimpleFilterValueFromAutoCompleteEvent,
    getFilterValue,
    getDomainValue,
  } = useAppState();

  return (
    <BasicAutoComplete
      id="vocations"
      label="Vocation"
      options={getDomainValue("vocations")}
      selectedOptions={getFilterValue("vocations")}
      onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
        "vocations"
      )}
      limitTags={5}
      getOptionKey={(vocation) => vocation.key}
      getOptionLabel={(vocation) => vocation.name}
    />
  );
}
