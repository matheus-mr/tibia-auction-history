import { Grid } from "@mui/material";
import VocationFilter from "./VocationFilter";
import NameFilter from "./NameFilter";
import LevelFilter from "./LevelFilter";
import WorldFilter from "./WorldFilter";

export default function MainInfoFilter() {
  return (
    <Grid container spacing={1} wrap="nowrap">
      <Grid item xs={1.5}>
        <NameFilter />
      </Grid>
      <Grid item xs={1.5}>
        <LevelFilter />
      </Grid>
      <Grid item xs={true}>
        <VocationFilter />
      </Grid>
      <Grid item xs={true}>
        <WorldFilter />
      </Grid>
    </Grid>
  );
}
