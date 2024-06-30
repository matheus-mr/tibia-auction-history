import Grid from "@mui/material/Grid";
import BasicAutoComplete from "../common/BasicAutoComplete";
import { useAppState } from "../AppStateContext";

export default function MountsFilter() {
  const {
    setSimpleFilterValueFromAutoCompleteEvent,
    getFilterValue,
    getDomainValue,
  } = useAppState();

  return (
    <Grid container columns={2} spacing={1}>
      <Grid item xs={1}>
        <BasicAutoComplete
          id="mounts"
          label="Mounts"
          options={getDomainValue("mounts")}
          selectedOptions={getFilterValue("mounts")}
          onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
            "mounts"
          )}
        />
      </Grid>
      <Grid item xs={1}>
        <BasicAutoComplete
          id="store-mounts"
          label="Store Mounts"
          options={getDomainValue("storeMounts")}
          selectedOptions={getFilterValue("storeMounts")}
          onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
            "storeMounts"
          )}
        />
      </Grid>
    </Grid>
  );
}
