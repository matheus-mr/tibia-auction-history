import BasicAutoComplete from "../common/basic-auto-complete";

export default function VocationFilter({
  vocations,
  selectedVocations,
  onChangeSelectedVocations,
}) {
  return (
    <BasicAutoComplete
      id="vocations"
      label="Vocation"
      options={vocations}
      selectedOptions={selectedVocations}
      onChangeSelectedOptions={onChangeSelectedVocations}
      limitTags={5}
    />
  );
}
