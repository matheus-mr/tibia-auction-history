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
  disableCloseOnSelect = true,
  getOptionLabel,
  getOptionKey,
}) {
  return (
    <Autocomplete
      multiple={multiple}
      id={id}
      options={options}
      renderInput={(params) => <TextField {...params} label={label} />}
      value={selectedOptions}
      onChange={onChangeSelectedOptions}
      limitTags={limitTags}
      disableCloseOnSelect={disableCloseOnSelect}
      getOptionKey={getOptionKey ? getOptionKey : (option) => option}
      getOptionLabel={getOptionLabel ? getOptionLabel : (option) => option}
    />
  );
}
