import BasicAutoComplete from "../common/basic-auto-complete";

export default function WorldFilter({
  worlds,
  selectedWorlds,
  onChangeSelectedWorlds,
}) {
  return (
    <BasicAutoComplete
      id="worlds"
      label="World"
      options={worlds}
      selectedOptions={selectedWorlds}
      onChangeSelectedOptions={onChangeSelectedWorlds}
    />
  );
}
