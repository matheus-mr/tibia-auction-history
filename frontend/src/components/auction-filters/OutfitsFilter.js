import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import Chip from "@mui/material/Chip";
import { useCallback } from "react";
import { useAppState } from "../AppStateContext";

function OutfitFilter({ autoCompleteId, autoCompleteLabel, fieldName }) {
  const { dispatch, getFilterValue, getDomainValue } = useAppState();
  const outfits = getDomainValue(fieldName);
  const selectedOutfits = getFilterValue(fieldName);

  const onSelectOutfit = useCallback(
    (event, value, reason) =>
      dispatch({
        type: "outfit_selected",
        filterName: fieldName,
        outfitName: value,
        reason,
      }),
    [fieldName, dispatch]
  );
  const onDeleteOutfit = useCallback(
    (outfitName) => () =>
      dispatch({
        type: "outfit_deleted",
        filterName: fieldName,
        outfitName,
      }),
    [fieldName, dispatch]
  );
  const onClickAddon = useCallback(
    (outfitName, addon) => () =>
      dispatch({
        type: "on_toggle_addon",
        filterName: fieldName,
        outfitName,
        addon,
      }),
    [fieldName, dispatch]
  );

  return (
    <Grid container direction={"column"}>
      <Autocomplete
        id={autoCompleteId}
        options={outfits}
        renderInput={(params) => (
          <TextField {...params} label={autoCompleteLabel} />
        )}
        onChange={onSelectOutfit}
      />
      <Grid container>
        {selectedOutfits.map(({ name, firstAddon, secondAddon }) => (
          <Grid key={name + "grid"} container spacing={1}>
            <Grid item>
              <Chip
                key={name + "chip"}
                value={name}
                label={name}
                onDelete={onDeleteOutfit(name)}
              />
            </Grid>
            <Grid item>
              <Chip
                key={name + "firstAddonButton"}
                label={"Addon 1"}
                clickable
                onClick={onClickAddon(name, "firstAddon")}
                variant={firstAddon ? "filled" : "outlined"}
              />
            </Grid>
            <Grid item>
              <Chip
                key={name + "secondAddonButton"}
                label={"Addon 2"}
                clickable
                onClick={onClickAddon(name, "secondAddon")}
                variant={secondAddon ? "filled" : "outlined"}
              />
            </Grid>
          </Grid>
        ))}
      </Grid>
    </Grid>
  );
}

export default function OutfitsFilter() {
  return (
    <Grid container columns={2} spacing={1}>
      <Grid item xs={1}>
        <OutfitFilter
          autoCompleteId="outfits"
          autoCompleteLabel="Outfits"
          fieldName={"outfits"}
        />
      </Grid>
      <Grid item xs={1}>
        <OutfitFilter
          autoCompleteId="store-outfits"
          autoCompleteLabel="Store Outfits"
          fieldName={"storeOutfits"}
        />
      </Grid>
    </Grid>
  );
}
