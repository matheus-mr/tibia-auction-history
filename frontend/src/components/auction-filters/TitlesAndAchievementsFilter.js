import Grid from "@mui/material/Grid";
import BasicAutoComplete from "../common/BasicAutoComplete";
import { useAppState } from "../AppStateContext";

export default function TitlesAndAchievementsFilter() {
  const {
    setSimpleFilterValueFromAutoCompleteEvent,
    getFilterValue,
    getDomainValue,
  } = useAppState();

  return (
    <Grid container columns={2} spacing={1}>
      <Grid item xs={1}>
        <BasicAutoComplete
          id="titles"
          label="Titles"
          options={getDomainValue("titles")}
          selectedOptions={getFilterValue("titles")}
          onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
            "titles"
          )}
        />
      </Grid>
      <Grid item xs={1}>
        <BasicAutoComplete
          id="achievements"
          label="Achievements"
          options={getDomainValue("achievements")}
          selectedOptions={getFilterValue("achievements")}
          onChangeSelectedOptions={setSimpleFilterValueFromAutoCompleteEvent(
            "achievements"
          )}
        />
      </Grid>
    </Grid>
  );
}
