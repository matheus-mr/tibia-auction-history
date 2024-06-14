import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import Chip from "@mui/material/Chip";

import "./outfits-filter.css";

const OutfitFilter = ({
  autoCompleteId,
  autoCompleteLabel,
  outfits,
  selectedOutfits,
  onSelectOutfit,
  onDeleteOutfit,
  onClickAddon,
}) => {
  return (
    <>
      <Autocomplete
        id={autoCompleteId}
        options={outfits}
        renderInput={(params) => (
          <TextField {...params} variant="standard" label={autoCompleteLabel} />
        )}
        onChange={onSelectOutfit}
      />
      {selectedOutfits.map(({ name, firstAddon, secondAddon }) => (
        <Grid key={name + "grid"} item>
          <Chip
            key={name + "chip"}
            value={name}
            label={name}
            onDelete={onDeleteOutfit(name)}
          />
          <Chip
            key={name + "firstAddonButton"}
            label={"Addon 1"}
            clickable
            onClick={onClickAddon(name, "firstAddon")}
            variant={firstAddon ? "filled" : "outlined"}
          />
          <Chip
            key={name + "secondAddonButton"}
            label={"Addon 2"}
            clickable
            onClick={onClickAddon(name, "secondAddon")}
            variant={secondAddon ? "filled" : "outlined"}
          />
        </Grid>
      ))}
    </>
  );
};

export default function OutfitsFilter({
  outfits = [],
  storeOutfits = [],
  selectedOutfits = [],
  onSelectOutfit,
  onDeleteOutfit,
  onClickAddon,
  selectedStoreOutfits = [],
  onSelectStoreOutfit,
  onDeleteStoreOutfit,
  onClickStoreAddon,
}) {
  return (
    <div className="outfits-filter">
      <Grid container columns={2} spacing={2}>
        <Grid item xs={1}>
          <Grid container direction={"column"}>
            <OutfitFilter
              autoCompleteId="outfits"
              autoCompleteLabel="Outfits"
              outfits={outfits}
              selectedOutfits={selectedOutfits}
              onSelectOutfit={onSelectOutfit}
              onDeleteOutfit={onDeleteOutfit}
              onClickAddon={onClickAddon}
            />
          </Grid>
        </Grid>
        <Grid item xs={1}>
          <Grid container direction={"column"}>
            <OutfitFilter
              autoCompleteId="store-outfits"
              autoCompleteLabel="Store Outfits"
              outfits={storeOutfits}
              selectedOutfits={selectedStoreOutfits}
              onSelectOutfit={onSelectStoreOutfit}
              onDeleteOutfit={onDeleteStoreOutfit}
              onClickAddon={onClickStoreAddon}
            />
          </Grid>
        </Grid>
      </Grid>
    </div>
  );
}
