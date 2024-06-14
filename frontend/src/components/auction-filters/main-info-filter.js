import VocationFilter from "./vocation-filter";
import NameFilter from "./name-filter";
import LevelFilter from "./level-filter";

import "./main-info-filter.css";
import WorldFilter from "./worlds-filter";
import { Grid } from "@mui/material";

export default function MainInfoFilter({
  name = "",
  onChangeName,
  level = "",
  onChangeLevel,
  vocations = [],
  selectedVocations = [],
  onChangeSelectedVocations,
  worlds = [],
  selectedWorlds = [],
  onChangeSelectedWorlds,
}) {
  return (
    <Grid container spacing={1} wrap="no-wrap">
      <Grid item xs={1.5}>
        <NameFilter name={name} onChangeName={onChangeName} />
      </Grid>
      <Grid item xs={1.5}>
        <LevelFilter level={level} onChangeLevel={onChangeLevel} />
      </Grid>
      <Grid item xs={4}>
        <VocationFilter
          vocations={vocations}
          selectedVocations={selectedVocations}
          onChangeSelectedVocations={onChangeSelectedVocations}
        />
      </Grid>
      <Grid item xs={4}>
        <WorldFilter
          worlds={worlds}
          selectedWorlds={selectedWorlds}
          onChangeSelectedWorlds={onChangeSelectedWorlds}
        />
      </Grid>
    </Grid>
  );
}
