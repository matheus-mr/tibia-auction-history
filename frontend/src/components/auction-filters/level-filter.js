import TextField from "@mui/material/TextField";

export default function LevelFilter({ level, onChangeLevel }) {
  return (
    <TextField
      label="Level"
      inputProps={{ inputMode: "numeric", pattern: "[0-9]*" }}
      value={level}
      onChange={onChangeLevel}
      variant="standard"
    />
  );
}
