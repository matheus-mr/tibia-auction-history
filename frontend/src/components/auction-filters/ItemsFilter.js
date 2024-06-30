import Grid from "@mui/material/Grid";
import BasicAutoComplete from "../common/BasicAutoComplete";
import { useAppState } from "../AppStateContext";

export default function ItemsFilter() {
  const {
    setSimpleFilterValueFromAutoCompleteEvent,
    getFilterValue,
    getDomainValue,
  } = useAppState();

  return (
    <Grid container columns={2} spacing={1}>
      <Grid item xs={1}>
        <BasicAutoComplete
          id="items"
          label="Items"
          options={getDomainValue("items")}
          selectedOptions={getFilterValue("items")}
          onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
            "items"
          )}
        />
      </Grid>
      <Grid item xs={1}>
        <BasicAutoComplete
          id="store-items"
          label="Store Items"
          options={getDomainValue("storeItems")}
          selectedOptions={getFilterValue("storeItems")}
          onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
            "storeItems"
          )}
        />
      </Grid>
    </Grid>
  );
}
