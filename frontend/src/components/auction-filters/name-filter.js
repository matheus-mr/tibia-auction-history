import TextField from "@mui/material/TextField";

export default function NameFilter({ name, onChangeName }) {
  return (
    <TextField
      value={name}
      onChange={onChangeName}
      label="Name"
      variant="standard"
    />
  );
}
