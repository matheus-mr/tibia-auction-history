import { useCallback } from "react";
import {
  Box,
  IconButton,
  Skeleton,
  Table,
  TableCell,
  TableSortLabel,
  useTheme,
} from "@mui/material";
import { TableRow } from "@mui/material";
import { TableBody } from "@mui/material";
import { TablePagination } from "@mui/material";
import { Paper } from "@mui/material";
import { TableHead } from "@mui/material";
import { TableContainer } from "@mui/material";
import { visuallyHidden } from "@mui/utils";
import { KeyboardArrowLeft, KeyboardArrowRight } from "@mui/icons-material";
import LastPageIcon from "@mui/icons-material/LastPage";
import FirstPageIcon from "@mui/icons-material/FirstPage";
import { useAppState } from "../AppStateContext";

const columns = [
  { label: "Name", id: "name" },
  { label: "Level", id: "level" },
  { label: "Vocation", id: "vocation" },
  { label: "World", id: "world" },
  { label: "Winning Bid", id: "winningBid" },
  { label: "Auction End", id: "auctionEnd" },
];

function TablePaginationActions(props) {
  const theme = useTheme();
  const { count, page, rowsPerPage, onPageChange } = props;

  const handleFirstPageButtonClick = (event) => {
    onPageChange(event, 0);
  };

  const handleBackButtonClick = (event) => {
    onPageChange(event, page - 1);
  };

  const handleNextButtonClick = (event) => {
    onPageChange(event, page + 1);
  };

  const handleLastPageButtonClick = (event) => {
    onPageChange(event, Math.max(0, Math.ceil(count / rowsPerPage) - 1));
  };

  return (
    <Box sx={{ flexShrink: 0, ml: 2.5 }}>
      <IconButton
        onClick={handleFirstPageButtonClick}
        disabled={page === 0}
        aria-label="first page"
      >
        {theme.direction === "rtl" ? <LastPageIcon /> : <FirstPageIcon />}
      </IconButton>
      <IconButton
        onClick={handleBackButtonClick}
        disabled={page === 0}
        aria-label="previous page"
      >
        {theme.direction === "rtl" ? (
          <KeyboardArrowRight />
        ) : (
          <KeyboardArrowLeft />
        )}
      </IconButton>
      <IconButton
        onClick={handleNextButtonClick}
        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
        aria-label="next page"
      >
        {theme.direction === "rtl" ? (
          <KeyboardArrowLeft />
        ) : (
          <KeyboardArrowRight />
        )}
      </IconButton>
      <IconButton
        onClick={handleLastPageButtonClick}
        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
        aria-label="last page"
      >
        {theme.direction === "rtl" ? <FirstPageIcon /> : <LastPageIcon />}
      </IconButton>
    </Box>
  );
}

function TableHeader({ sortBy, orderBy, onClickSort }) {
  return (
    <TableHead>
      <TableRow>
        {columns.map((column) => {
          return (
            <TableCell
              key={column.id}
              sortDirection={
                sortBy === column.id ? orderBy.toLowerCase() : false
              }
            >
              <TableSortLabel
                active={sortBy === column.id}
                direction={sortBy === column.id ? orderBy.toLowerCase() : "asc"}
                onClick={onClickSort(column.id)}
              >
                {column.label}
                {sortBy === column.id ? (
                  <Box component="span" sx={visuallyHidden}>
                    {orderBy === "DESC"
                      ? "sorted descending"
                      : "sorted ascending"}
                  </Box>
                ) : null}
              </TableSortLabel>
            </TableCell>
          );
        })}
      </TableRow>
    </TableHead>
  );
}

function SkeletonTableBody({ rowsPerPage }) {
  return Array.from({ length: rowsPerPage }).map((_, index) => (
    <TableRow key={index}>
      <TableCell component="th" scope="row">
        <Skeleton variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton variant="text" />
      </TableCell>
    </TableRow>
  ));
}

function NoResultsTableBody() {
  return (
    <TableRow>
      <TableCell colSpan={6} align="center">
        No characters found.
      </TableCell>
    </TableRow>
  );
}

function FilledTableBody({ auctions, handleRowClick }) {
  return auctions.map((auction) => (
    <TableRow
      hover
      sx={{ cursor: "pointer" }}
      key={auction.id}
      onClick={() => handleRowClick(auction.id)}
    >
      <TableCell>{auction.name}</TableCell>
      <TableCell>{auction.level}</TableCell>
      <TableCell>{auction.vocation}</TableCell>
      <TableCell>{auction.world}</TableCell>
      <TableCell>{auction.winningBid}</TableCell>
      <TableCell>{auction.auctionEnd}</TableCell>
    </TableRow>
  ));
}

export default function AuctionTable({
  auctions,
  isLoadingAuctions,
  isErrorAuctions,
}) {
  const { state, dispatch, setSimpleFieldValue } = useAppState();
  const { totalCount, currentPage, rowsPerPage, sortBy, orderBy } = state;

  const onClickSort = (columnId) => (event) => {
    const isAsc = orderBy === "ASC";
    setSimpleFieldValue("sortBy", columnId);
    setSimpleFieldValue("orderBy", isAsc ? "DESC" : "ASC");
  };

  const handleRowClick = useCallback((auctionId) => {
    const url = `https://www.tibia.com/charactertrade/?subtopic=pastcharactertrades&page=details&auctionid=${auctionId}`;
    window.open(url, "_blank", "noopener,noreferrer");
  }, []);

  if (isErrorAuctions) {
    return <div>Something went wrong, please try again.</div>;
  }

  return (
    <>
      <TableContainer component={Paper}>
        <Table>
          <TableHeader
            sortBy={sortBy}
            orderBy={orderBy}
            onClickSort={onClickSort}
          />
          <TableBody>
            {isLoadingAuctions ? (
              <SkeletonTableBody rowsPerPage={rowsPerPage} />
            ) : auctions && auctions.length > 0 ? (
              <FilledTableBody
                auctions={auctions}
                handleRowClick={handleRowClick}
              />
            ) : (
              <NoResultsTableBody />
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[10, 50, 100]}
        component="div"
        count={totalCount || 0}
        rowsPerPage={parseInt(rowsPerPage)}
        page={currentPage}
        onPageChange={(event, page) => setSimpleFieldValue("currentPage", page)}
        onRowsPerPageChange={(event) =>
          dispatch({
            type: "set_rows_per_page",
            rowsPerPage: event.target.value,
          })
        }
        ActionsComponent={TablePaginationActions}
      />
    </>
  );
}
