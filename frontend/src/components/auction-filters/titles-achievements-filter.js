import Grid from "@mui/material/Grid";
import BasicAutoComplete from "../common/basic-auto-complete";

export default function TitlesAndAchievementsFilter({
  titles = [],
  achievements = [],
  selectedTitles = [],
  onChangeSelectedTitles,
  selectedAchievements = [],
  onChangeSelectedAchievements,
}) {
  return (
    <div className="titles-achievements-filter">
      <Grid container columns={2} spacing={2}>
        <Grid item xs={1}>
          <BasicAutoComplete
            id="titles"
            label="Titles"
            options={titles}
            selectedOptions={selectedTitles}
            onChangeSelectedOptions={onChangeSelectedTitles}
          />
        </Grid>
        <Grid item xs={1}>
          <BasicAutoComplete
            id="achievements"
            label="Achievements"
            options={achievements}
            selectedOptions={selectedAchievements}
            onChangeSelectedOptions={onChangeSelectedAchievements}
          />
        </Grid>
      </Grid>
    </div>
  );
}
