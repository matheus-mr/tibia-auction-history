import MUIDataTable from "mui-datatables";

const columns = [
  { label: "Name", name: "name" },
  { label: "Level", name: "level" },
  { label: "Vocation", name: "vocation" },
  { label: "World", name: "world" },
  { label: "Auction End", name: "auctionEnd" },
];

const options = {
  filter: false,
  search: false,
  print: false,
  download: false,
  selectableRows: "none",
  rowsPerPageOptions: [10, 50, 100],
};

export default function AuctionTable({
  auctions,
  totalCount,
  currentPage,
  onChangeRowsPerPage,
  onChangePage,
  onChangeSortBy,
  onChangeOrderBy,
}) {
  const onTableChange = (action, tableState) => {
    // console.log(action);
    // console.log(tableState);
    switch (action) {
      case "changeRowsPerPage":
        onChangeRowsPerPage(tableState.rowsPerPage);
        break;
      case "changePage":
        onChangePage(tableState.page);
        break;
      case "sort":
        onChangeSortBy(tableState.sortOrder.name);
        onChangeOrderBy(String(tableState.sortOrder.direction).toUpperCase());
        break;
      default:
        break;
    }
  };

  return (
    <>
      <MUIDataTable
        columns={columns}
        data={auctions}
        options={{
          ...options,
          onTableChange,
          count: totalCount,
          serverSide: true,
          page: currentPage,
        }}
        title="Tibia Auction History"
      />
    </>
  );
}
