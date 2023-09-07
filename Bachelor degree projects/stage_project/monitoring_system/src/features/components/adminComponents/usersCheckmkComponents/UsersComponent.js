import DataTable from "react-data-table-component";
import { useState, useMemo, useEffect } from "react";
import FilterComponent from "./FilterComponent";
import "../../../../css/table.css";
import {
  useDeleteUserMutation,
  useGetUsersQuery,
} from "../../../reduxApi/usersApi";
import DetailsComponent from "./DetailsComponent";
import AdminNavbar from "../AdminNavbar";
import { toast, ToastContainer } from "react-toastify";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { addData } from "../../../slice/shareDataSlice";
import isExpiredToken from "../../../utilFunctions/isExipredToken";
import RefreshToken from "../../refreshTokenComponent/RefreshToken";
import { getAllUsers } from "../../../utilFunctions/fetchFunction";
import ConfirmDelete from "../../confirmDeleteComponent/ConfirmDelete";

// per maggiore chiarezza mettere le indicazioni sui gruppi e i ruoli in una finestra modale
// che si apre premendo sulla lente di ingrandimento

function UsersComponent({
  openRefreshToken,
  showRefToken,
  setLoadingData,
  setLoadingPage,
}) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  let user = useSelector((state) => state.user);
  let [tableLoading, setTableLoading] = useState(true);
  let [tableData, setTableData] = useState([]);
  let [isError, setIsError] = useState(false);

  const callGetAllUsers = async () => {
    const data = await getAllUsers(user).catch((err) =>
      console.log("in catch")
    );

    let app = [];
    console.log(data);

    if (data !== undefined) {
      for (let i = 0; i < data.length; i++) {
        // creo degli oggetti di supporto che mi servono per visualizzare
        // i vari dati nella tabella
        // da aggiungere anche i gruppi
        app.push({
          id: i + 1,
          name: data[i].name,
          username: data[i].username,
          email: data[i].email,
          roles: data[i].roles.join(", "),
          contactGroups: data[i].contactGroups.join(", "),
        });
      }
      console.log("users", data);
      setTableLoading(false);
      setTableData(app);
      setIsError(false);
    } else {
      setTableLoading(false);
      setTableData([]);
      setIsError(true);
    }
  };

  const columns = [
    {
      name: "Name",
      selector: (row) => row.name,
      sortable: true,
      grow: 10,
    },
    {
      name: "Username",
      selector: (row) => row.username,
      sortable: true,
      width: "150px",
    },

    {
      name: "Email",
      selector: (row) => row.email,
      center: "true",
      grow: 20,
    },
    {
      name: "More info",
      selector: (row) => row.moreInfo,
      center: "true",
      width: "150px",
      cell: (row) => (
        <button
          className="btn"
          onClick={() => moreInfoHandler(row)}
          data-toggle="modal"
          data-target="#exampleModal"
        >
          <i
            className="bi  bi-zoom-in"
            title="more info"
            style={{ fontSize: "17px" }}
          />
        </button>
      ),
    },
    {
      name: "Modify",
      selector: (row) => row.modify,
      center: "true",
      width: "120px",
      cell: (row) => (
        <button
          className="btn"
          onClick={() => onClickHandler(row)}
          disabled={
            user.decodedAccessToken.roles[0] === "ROLE_ADMIN" &&
            (row.roles === "superadmin" ||
              (row.roles === "admin" &&
                row.username !== user.decodedAccessToken.sub))
          }
        >
          <i
            className="bi  bi-pencil-fill"
            title="modify user"
            style={{ fontSize: "17px" }}
          />
        </button>
      ),
    },
    {
      name: "Delete",
      selector: (row) => row.delete,
      center: "true",
      width: "180px",
      cell: (row) => (
        <button
          className="btn btn-outline-danger"
          onClick={() => openConfirmDeleteHandler(row)}
          disabled={
            (user &&
              user.decodedAccessToken.roles[0] === "ROLE_SUPER_ADMIN" &&
              row.roles === "superadmin") ||
            (user.decodedAccessToken.roles[0] === "ROLE_ADMIN" &&
              (row.roles === "superadmin" || row.roles === "admin"))
          }
        >
          Delete
        </button>
      ),
    },
  ];

  const [deleteUser] = useDeleteUserMutation();
  const [showMoreInfo, setShowMoreInfo] = useState(false);
  const [moreInfoData, setMoreInfoData] = useState();
  const [openConfirmDelete, setOpenConfirmDelete] = useState(false);
  const [deleteConfirmData, setDeleteConfirmData] = useState("");

  useEffect(() => {
    if (
      user !== null &&
      isExpiredToken(user.decodedAccessToken.exp) &&
      showRefToken === false
    ) {
      openRefreshToken(true);
    } else {
      console.log("in else refetch");
      callGetAllUsers(user);
    }

    return () => {
      console.log("unmount");

      setTableData([]);
      setTableLoading(true);
    };
  }, []);

  console.log("fuori use effect");

  const [filterText, setFilterText] = useState("");
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);

  const filteredItems = tableData.filter(
    (item) =>
      item.username &&
      item.username.toLowerCase().includes(filterText.toLowerCase())
  );

  // stampa come oggetto il contenuto della riga
  const onClickHandler = (row) => {
    dispatch(addData(row));
    navigate("/modifyUser");
  };

  const addNewUserHandler = () => {
    navigate("/addUser");
  };

  const deleteHandler = (username) => {
    console.log("in delete handler");
    if (isExpiredToken(user.decodedAccessToken.exp)) openRefreshToken(true);
    else {
      setOpenConfirmDelete(false);
      setLoadingData("Deleting user");
      setLoadingPage(true);
      deleteUser(username)
        .unwrap()
        .then((risp) => {
          setLoadingPage(false);
          toast.success(risp.message);
          callGetAllUsers(user);
        })
        .catch((err) => {
          setLoadingPage(false);
          toast.error(err.data.message);
        });
    }
  };

  const moreInfoHandler = (row) => {
    setMoreInfoData(row);
    setShowMoreInfo(true);
  };

  const handleCloseMoreInfo = () => {
    setShowMoreInfo(false);
    setMoreInfoData("");
  };

  const openConfirmDeleteHandler = (row) => {
    setDeleteConfirmData(row);
    setOpenConfirmDelete(true);
  };

  const closeConfirmDeleteHandler = () => {
    setOpenConfirmDelete(false);
  };

  const subHeaderComponentMemo = useMemo(() => {
    const handleClear = () => {
      if (filterText) {
        setResetPaginationToggle(!resetPaginationToggle);
        setFilterText("");
      }
    };

    const refetchHandler = () => {
      callGetAllUsers(user);
    };

    return (
      <>
        <FilterComponent
          onFilter={(e) => setFilterText(e.target.value)}
          onClear={handleClear}
          filterText={filterText}
          addNewUserHandler={addNewUserHandler}
          refetch={refetchHandler}
        />
      </>
    );
  }, [filterText, resetPaginationToggle]);

  console.log(isError);
  const retrivingDataError = (
    <div
      id="loginError"
      className="alert alert-danger"
      role="alert"
      style={{ marginLeft: "300px", width: "970px", textAlign: "center" }}
    >
      Ops something went wrong in retriving data, please check your internet
      connection or contact the IT support.
    </div>
  );

  // TODO: cancellare toast container e fare delle prove dei mex
  return (
    <>
      <div className="box">
        <AdminNavbar currentPage={"users"} />
        <div className="table-container">
          <DataTable
            columns={columns}
            data={filteredItems}
            pagination
            paginationResetDefaultPage={resetPaginationToggle}
            subHeader
            subHeaderComponent={subHeaderComponentMemo}
            paginationRowsPerPageOptions={[8, 15, 20]}
            paginationPerPage={8}
            striped
            progressPending={tableLoading}
            responsive
            highlightOnHover
            customStyles={{
              subHeader: {
                style: {
                  borderTopRightRadius: "20px",
                  borderTopLeftRadius: "20px",
                },
              },
              headCells: {
                style: {
                  fontSize: "17px",
                },
              },
              cells: {
                style: {
                  fontSize: "14px",
                  wordWrap: "break-word",
                },
              },
              pagination: {
                style: {
                  marginBottom: "70px",
                  borderBottomRightRadius: "20px",
                  borderBottomLeftRadius: "20px",
                },
              },
            }}
          />
        </div>
        {isError && retrivingDataError}

        <ConfirmDelete
          show={openConfirmDelete}
          handleClose={closeConfirmDeleteHandler}
          data={deleteConfirmData}
          onYesHandle={deleteHandler}
        />

        <DetailsComponent
          data={moreInfoData}
          show={showMoreInfo}
          handleClose={handleCloseMoreInfo}
        />
      </div>
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </>
  );
}

export default UsersComponent;
