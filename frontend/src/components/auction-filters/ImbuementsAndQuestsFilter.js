import Grid from "@mui/material/Grid";
import BasicAutoComplete from "../common/BasicAutoComplete";
import { useAppState } from "../AppStateContext";

export default function ImbuementsAndQuestsFilter() {
  const {
    setSimpleFilterValueFromAutoCompleteEvent,
    getFilterValue,
    getDomainValue,
  } = useAppState();

  return (
    <Grid container columns={2} spacing={1}>
      <Grid item xs={1}>
        <BasicAutoComplete
          id="imbuements"
          label="Imbuements"
          options={getDomainValue("imbuements")}
          selectedOptions={getFilterValue("imbuements")}
          onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
            "imbuements"
          )}
        />
      </Grid>
      <Grid item xs={1}>
        <BasicAutoComplete
          id="completedQuestLines"
          label="Completed Quest Lines"
          options={getDomainValue("completedQuestLines")}
          selectedOptions={getFilterValue("completedQuestLines")}
          onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
            "completedQuestLines"
          )}
        />
      </Grid>
    </Grid>
  );
}
