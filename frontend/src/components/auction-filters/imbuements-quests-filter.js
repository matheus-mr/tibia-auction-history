import Grid from "@mui/material/Grid";
import BasicAutoComplete from "../common/basic-auto-complete";

export default function ImbuementsAndQuestsFilter({
  imbuements = [],
  completedQuestLines = [],
  selectedImbuements = [],
  onChangeSelectedImbuements,
  selectedCompletedQuestLines = [],
  onChangeSelectedCompletedQuestLines,
}) {
  return (
    <div className="imbuements-quests-filter">
      <Grid container columns={2} spacing={2}>
        <Grid item xs={1}>
          <BasicAutoComplete
            id="imbuements"
            label="Imbuements"
            options={imbuements}
            selectedOptions={selectedImbuements}
            onChangeSelectedOptions={onChangeSelectedImbuements}
          />
        </Grid>
        <Grid item xs={1}>
          <BasicAutoComplete
            id="completedQuestLines"
            label="Completed Quest Lines"
            options={completedQuestLines}
            selectedOptions={selectedCompletedQuestLines}
            onChangeSelectedOptions={onChangeSelectedCompletedQuestLines}
          />
        </Grid>
      </Grid>
    </div>
  );
}
