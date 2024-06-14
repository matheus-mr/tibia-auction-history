import Grid from "@mui/material/Grid";
import BasicAutoComplete from "../common/basic-auto-complete";

export default function ItemsFilter({
  items = [],
  storeItems = [],
  selectedItems = [],
  onChangeSelectedItems,
  selectedStoreItems = [],
  onChangeSelectedStoreItems,
}) {
  return (
    <div className="items-filter">
      <Grid container columns={2} spacing={2}>
        <Grid item xs={1}>
          <BasicAutoComplete
            id="items"
            label="Items"
            options={items}
            selectedOptions={selectedItems}
            onChangeSelectedOptions={onChangeSelectedItems}
          />
        </Grid>
        <Grid item xs={1}>
          <BasicAutoComplete
            id="store-items"
            label="Store Items"
            options={storeItems}
            selectedOptions={selectedStoreItems}
            onChangeSelectedOptions={onChangeSelectedStoreItems}
          />
        </Grid>
      </Grid>
    </div>
  );
}
