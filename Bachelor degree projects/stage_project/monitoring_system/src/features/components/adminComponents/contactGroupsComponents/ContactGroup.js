import DataTable from "react-data-table-component";
import { useState, useMemo, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import "../../../../css/tableContactGroup.css";
import FilterComponentContactGroups from "./FilterComponentContactGroups";
import isExpiredToken from "../../../utilFunctions/isExipredToken";
import AdminNavbar from "../AdminNavbar";
import ModifyContactGroup from "./ModifyContactGroup";
import AddContactGroup from "./AddContactGroup";
import {
  useGetContactGroupsQuery,
  useUpdateContactGroupMutation,
  useDeleteContactGroupMutation,
  useAddContactGroupMutation,
} from "../../../reduxApi/contactGroupApi";

import { toast, ToastContainer } from "react-toastify";
import { getAllContactGroups } from "../../../utilFunctions/fetchFunction";
import ConfirmDelete from "../../confirmDeleteComponent/ConfirmDelete";

function ContactGroup({
  openRefreshToken,
  showRefToken,
  setLoadingData,
  setLoadingPage,
}) {
  let user = useSelector((state) => state.user);
  const [tableLoading, setTableLoading] = useState(true);
  const [tableData, setTableData] = useState([]);
  let [isError, setIsError] = useState(false);

  const callGetContactGroups = async () => {
    const data = await getAllContactGroups(user).catch((err) => console.log());

    console.log("aggiorno dati");
    console.log("data", data);
    let app = [];
    if (data !== undefined) {
      for (let i = 0; i < data.length; i++) {
        // creo degli oggetti di supporto che mi servono per visualizzare
        // i vari dati nella tabella
        // da aggiungere anche i gruppi
        app.push({
          id: i + 1,
          name: data[i].name,
          alias: data[i].alias,
        });
      }

      setTableLoading(false);
      setTableData(app);
      setIsError(false);
    } else {
      setTableLoading(false);
      setTableData([]);
      setIsError(true);
    }
  };

  const [filterText, setFilterText] = useState("");
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);

  const [showModifyModal, setShowModifyModal] = useState(false);
  const [showAddModal, setShowAddModal] = useState(false);
  const [modalData, setModalData] = useState("");
  const [openConfirmDelete, setOpenConfirmDelete] = useState(false);
  const [deleteConfirmData, setDeleteConfirmData] = useState("");

  const [updateContactGroup] = useUpdateContactGroupMutation();
  const [deleteContactGroup] = useDeleteContactGroupMutation();
  const [addContactGroup] = useAddContactGroupMutation();

  const columns = [
    {
      name: "Name",
      selector: (row) => row.name,
      sortable: true,
      width: "300px",
    },
    {
      name: "Alias",
      selector: (row) => row.alias,
      sortable: true,
      width: "200px",
    },
    {
      name: "Modify",
      selector: (row) => row.modify,
      center: "true",
      width: "170px",
      cell: (row) => (
        <button className="btn">
          <i
            className="bi  bi-pencil-fill"
            title="modify contact group"
            style={{ fontSize: "17px" }}
            onClick={() => {
              setShowModifyModal(true);
              setModalData(row);
            }}
          />
        </button>
      ),
    },
    {
      name: "Delete",
      selector: (row) => row.delete,
      center: "true",
      width: "170px",
      cell: (row) => (
        <button
          className="btn btn-outline-danger"
          onClick={() => openConfirmDeleteHandler(row)}
        >
          Delete
        </button>
      ),
    },
  ];

  useEffect(() => {
    if (
      user !== null &&
      isExpiredToken(user.decodedAccessToken.exp) &&
      showRefToken === false
    ) {
      openRefreshToken(true);
    } else callGetContactGroups(user);

    if (showRefToken === false) {
      callGetContactGroups(user);
    }
    return () => {};
  }, []);

  const filteredItems = tableData.filter(
    (item) =>
      item.name && item.name.toLowerCase().includes(filterText.toLowerCase())
  );

  const deleteHandler = (name) => {
    console.log("in delete handler");
    if (isExpiredToken(user.decodedAccessToken.exp)) openRefreshToken(true);
    else {
      // console.log(row);
      setOpenConfirmDelete(false);
      setLoadingData("Deleting contact group");
      setLoadingPage(true);
      deleteContactGroup({ name: name })
        .unwrap()
        .then((risp) => {
          setLoadingPage(false);
          toast.success(risp.message);
          callGetContactGroups(user);
        })
        .catch((err) => {
          setLoadingPage(false);
          toast.error(err.data.error);
        });
    }
  };

  const handleCloseModal = () => {
    setModalData("");
    setShowModifyModal(false);
  };

  const handleCloseAddModal = () => {
    setShowAddModal(false);
  };

  const handleSaveAddModal = (newContactGroup) => {
    setShowAddModal(false);
    setLoadingData("Adding contact group");
    setLoadingPage(true);
    addContactGroup(newContactGroup)
      .unwrap()
      .then((risp) => {
        setLoadingPage(false);

        toast.success(risp.message);
        callGetContactGroups(user);
      })
      .catch((err) => {
        setLoadingPage(false);
        toast.error(err.data.error);
      });

    // chiamata per cambiamenti
  };

  const handleSaveUpdate = (newAlias) => {
    console.log(newAlias);
    setShowModifyModal(false);
    setModalData("");
    setLoadingData("Updating contact group");
    setLoadingPage(true);

    updateContactGroup(newAlias)
      .unwrap()
      .then((risp) => {
        setLoadingPage(false);

        toast.success(risp.message);
        callGetContactGroups(user);
      })
      .catch((err) => {
        setLoadingPage(false);
        console.log(err);
        toast.error(err.data.error);
      });

    // chiamata per cambiamenti
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
      console.log("refetch data");
      callGetContactGroups(user);
    };

    return (
      <>
        <FilterComponentContactGroups
          onFilter={(e) => setFilterText(e.target.value)}
          onClear={handleClear}
          filterText={filterText}
          refetch={refetchHandler}
          onAdd={setShowAddModal}
        />
      </>
    );
  }, [filterText, resetPaginationToggle]);

  const retrivingDataError = (
    <div
      id="loginError"
      className="alert alert-danger"
      role="alert"
      style={{ width: "900px", textAlign: "center" }}
    >
      Ops something went wrong in retriving data, please check your internet
      connection or contact the IT support..
    </div>
  );

  return (
    <>
      <div className="box">
        <AdminNavbar currentPage={"contactGroups"} />
        <div className="table-container-contact">
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
      </div>
      {isError && retrivingDataError}

      <ConfirmDelete
        show={openConfirmDelete}
        handleClose={closeConfirmDeleteHandler}
        data={deleteConfirmData}
        onYesHandle={deleteHandler}
      />

      <ModifyContactGroup
        show={showModifyModal}
        handleClose={handleCloseModal}
        onSaveHandle={handleSaveUpdate}
        data={modalData}
      />
      <AddContactGroup
        show={showAddModal}
        handleClose={handleCloseAddModal}
        onSaveHandle={handleSaveAddModal}
      />
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

export default ContactGroup;
