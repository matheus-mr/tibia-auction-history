import Autocomplete from "@mui/material/Autocomplete";
import TextField from "@mui/material/TextField";

export default function BasicAutoComplete({
  id,
  options,
  label,
  selectedOptions,
  onChangeSelectedOptions,
  limitTags = 2,
  multiple = true,
}) {
  return (
    <Autocomplete
      multiple={multiple}
      id={id}
      options={options}
      renderInput={(params) => (
        <TextField {...params} variant="standard" label={label} />
      )}
      value={selectedOptions}
      onChange={onChangeSelectedOptions}
      limitTags={limitTags}
    />
  );
}
