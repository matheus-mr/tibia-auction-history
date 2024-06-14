import TextField from "@mui/material/TextField";
import { Grid } from "@mui/material";

export default function SkillsFilter({
  axeFighting = "",
  onChangeAxeFighting,
  clubFighting = "",
  onChangeClubFighting,
  swordFighting = "",
  onChangeSwordFighting,
  fistFighting = "",
  onChangeFistFighting,
  distanceFighting = "",
  onChangeDistanceFighting,
  magicLevel = "",
  onChangeMagicLevel,
  shielding = "",
  onChangeShielding,
  fishing = "",
  onChangeFishing,
}) {
  return (
    <Grid container spacing={1} wrap="no-wrap">
      <Grid item>
        <TextField
          className="skills-filter-option"
          label="Axe Fighting"
          value={axeFighting}
          onChange={onChangeAxeFighting}
          variant="standard"
        />
      </Grid>
      <Grid item>
        <TextField
          className="skills-filter-option"
          label="Club Fighting"
          value={clubFighting}
          onChange={onChangeClubFighting}
          variant="standard"
        />
      </Grid>
      <Grid item>
        <TextField
          className="skills-filter-option"
          label="Sword Fighting"
          value={swordFighting}
          onChange={onChangeSwordFighting}
          variant="standard"
        />
      </Grid>
      <Grid item>
        <TextField
          className="skills-filter-option"
          label="Fist Fighting"
          value={fistFighting}
          onChange={onChangeFistFighting}
          variant="standard"
        />
      </Grid>
      <Grid item>
        <TextField
          className="skills-filter-option"
          label="Distance Fighting"
          value={distanceFighting}
          onChange={onChangeDistanceFighting}
          variant="standard"
        />
      </Grid>
      <Grid item>
        <TextField
          className="skills-filter-option"
          label="Magic Level"
          value={magicLevel}
          onChange={onChangeMagicLevel}
          variant="standard"
        />
      </Grid>
      <Grid item>
        <TextField
          className="skills-filter-option"
          label="Shielding"
          value={shielding}
          onChange={onChangeShielding}
          variant="standard"
        />
      </Grid>
      <Grid item>
        <TextField
          className="skills-filter-option"
          label="Fishing"
          value={fishing}
          onChange={onChangeFishing}
          variant="standard"
        />
      </Grid>
    </Grid>
  );
}
