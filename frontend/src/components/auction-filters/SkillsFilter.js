import TextField from "@mui/material/TextField";
import { Grid } from "@mui/material";
import { useAppState } from "../AppStateContext";

const MIN_SKILL = 1;
const MAX_SKILL = 150;

function SkillFilter({ label, field }) {
  const { setSimpleFilterValueFromEvent, getFilterValue } = useAppState();

  const handleChange = (event) => {
    const newValue = event.target.value;

    if (newValue === "") {
      setSimpleFilterValueFromEvent(field)(event);
      return;
    }

    const numericValue = Number(newValue);

    if (numericValue >= MIN_SKILL && numericValue <= MAX_SKILL) {
      setSimpleFilterValueFromEvent(field)(event);
    }
  };

  return (
    <TextField
      label={label}
      value={getFilterValue(field)}
      onChange={handleChange}
      type="number"
      fullWidth
    />
  );
}

export default function SkillsFilter() {
  return (
    <Grid container spacing={1} wrap="nowrap">
      <Grid item xs={2}>
        <SkillFilter label="Axe Fighting" field={"axeFighting"} />
      </Grid>
      <Grid item xs={2}>
        <SkillFilter label="Club Fighting" field={"clubFighting"} />
      </Grid>
      <Grid item xs={2}>
        <SkillFilter label="Sword Fighting" field={"swordFighting"} />
      </Grid>
      <Grid item xs={2}>
        <SkillFilter label="Fist Fighting" field={"fistFighting"} />
      </Grid>
      <Grid item xs={2}>
        <SkillFilter label="Distance Fighting" field={"distanceFighting"} />
      </Grid>
      <Grid item xs={2}>
        <SkillFilter label="Magic Level" field={"magicLevel"} />
      </Grid>
      <Grid item xs={2}>
        <SkillFilter label="Shielding" field={"shielding"} />
      </Grid>
      <Grid item xs={2}>
        <SkillFilter label="Fishing" field={"fishing"} />
      </Grid>
    </Grid>
  );
}
