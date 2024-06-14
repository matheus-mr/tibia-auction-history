import Grid from "@mui/material/Grid";
import BasicAutoComplete from "../common/basic-auto-complete";

export default function MountsFilter({
  mounts = [],
  storeMounts = [],
  selectedMounts = [],
  onChangeSelectedMounts,
  selectedStoreMounts = [],
  onChangeSelectedStoreMounts,
}) {
  return (
    <div className="mounts-filter">
      <Grid container columns={2} spacing={2}>
        <Grid item xs={1}>
          <BasicAutoComplete
            id="mounts"
            label="Mounts"
            options={mounts}
            selectedOptions={selectedMounts}
            onChangeSelectedOptions={onChangeSelectedMounts}
          />
        </Grid>
        <Grid item xs={1}>
          <BasicAutoComplete
            id="store-mounts"
            label="Store Mounts"
            options={storeMounts}
            selectedOptions={selectedStoreMounts}
            onChangeSelectedOptions={onChangeSelectedStoreMounts}
          />
        </Grid>
      </Grid>
    </div>
  );
}
