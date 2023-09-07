import DataTable from "react-data-table-component";
import { useState, useMemo, useEffect, useImperativeHandle } from "react";
import "../../../css/table.css";
import isExpiredToken from "../../utilFunctions/isExipredToken";
import FilterComponentHost from "./FilterComponentHost";
import AdminNavbar from "../adminComponents/AdminNavbar";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { getMonitoredHosts } from "../../utilFunctions/fetchFunction";
import ModifyHost from "./ModifyHost";
import MoreInfoHost from "./MoreInfoHost";
import statusIcon from "../../utilFunctions/statusClassName";
import {
  useUpdateHostMutation,
  useInstallAgentMutation,
  useDeleteHostMutation,
  useUpdateSSHCredentialsMutation,
} from "../../reduxApi/hostApi";
import { toast } from "react-toastify";
import { addData } from "../../slice/shareDataSlice";
import UserNavbar from "../userComponents/UserNavbar";
import { getIntsallAgentButton } from "../../utilFunctions/getInstallAgentButton";
import ConfirmDelete from "../confirmDeleteComponent/ConfirmDelete";
import SshSettingsModal from "./SshSettingsModal";

function MonitoredHosts({
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
  let [openConfirmDelete, setOpenConfirmDelete] = useState(false);

  const [updateHost] = useUpdateHostMutation();
  const [installAgent] = useInstallAgentMutation();
  const [deleteHost] = useDeleteHostMutation();
  const [updateSSH] = useUpdateSSHCredentialsMutation();

  const callGetAllHost = async () => {
    console.log("call get all Host");
    const data = await getMonitoredHosts(user).catch((err) =>
      console.log("in catch")
    );

    let app = [];
    console.log("get", data);

    if (data !== undefined) {
      for (let i = 0; i < data.length; i++) {
        // creo degli oggetti di supporto che mi servono per visualizzare
        // i vari dati nella tabella
        // da aggiungere anche i gruppi
        if (
          data[i].contactgroups === null ||
          data[i].contactgroups.length > 0
        ) {
          let contactGroups =
            data[i].contactgroups === null
              ? "none"
              : data[i].contactgroups.join(", ");

          app.push({
            id: i + 1,
            hostName: data[i].hostName,
            alias: data[i].alias,
            status: data[i].status,
            contactGroups: contactGroups,
            os: data[i].os_family,
            ipAddress: data[i].ipaddress,
            tagAgent: data[i].tag_agent,
            pluginsTag: data[i].plugins_tag,
            agent_install_mode: data[i].agent_install_mode,
          });
        }
      }
      console.log("hosts", app);
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
      name: "Host name",
      selector: (row) => row.hostName,
      sortable: true,
      width: "150px",
    },
    {
      name: "Alias",
      selector: (row) => row.alias,
      sortable: true,
      width: "100px",
    },

    {
      name: "Status",
      selector: (row) => row.status,
      center: "true",
      width: "100px",
      cell: (row) => statusIcon(row.status),
    },
    {
      name: "More info",
      selector: (row) => row.moreInfo,
      center: "true",
      width: "130px",
      cell: (row) => (
        <button
          className="btn"
          onClick={() => {
            setShowMoreInfoModal(true);
            setModalData(row);
          }}
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
          onClick={() => {
            console.log(row);
            setShowModifyModal(true);
            setModalData(row);
          }}
          disabled={user.decodedAccessToken.roles[0] === "ROLE_USER"}
        >
          <i
            className="bi  bi-pencil-fill"
            title="modify host"
            style={{ fontSize: "17px" }}
          />
        </button>
      ),
    },
    {
      name: "SSH settings",
      selector: (row) => row.SSHSettings,
      center: "true",
      width: "130px",
      cell: (row) => (
        <button
          className="btn"
          onClick={() => {
            console.log(row);
            setShowSSHSettingsModal(true);
            setModalData(row);
          }}
          disabled={user.decodedAccessToken.roles[0] === "ROLE_USER"}
        >
          <i
            className="bi  bi-gear-fill"
            title="SSH settings"
            style={{ fontSize: "17px" }}
          />
        </button>
      ),
    },
    {
      name: "Install agent",
      selector: (row) => row.installAgent,
      center: "true",
      width: "200px",
      cell: (row) => getIntsallAgentButton(row, user, installAgentHandler),
    },
    {
      name: "Plugins",
      selector: (row) => row.plugins,
      center: "true",
      width: "150px",
      cell: (row) => (
        <button
          className="btn btn-outline-secondary"
          onClick={() => {
            pluginHandler(row);
          }}
          disabled={user.decodedAccessToken.roles[0] === "ROLE_USER"}
        >
          Plugins
        </button>
      ),
    },
    {
      name: "Delete",
      selector: (row) => row.delete,
      center: "true",
      width: "120px",
      cell: (row) => (
        <button
          className="btn btn-outline-danger"
          onClick={() => openConfirmDeleteHandler(row)}
          disabled={user.decodedAccessToken.roles[0] === "ROLE_USER"}
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
    } else {
      console.log("in else refetch");
      callGetAllHost(user);
    }
    return () => {};
  }, []);

  const [filterText, setFilterText] = useState("");
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);
  const [showModifyModal, setShowModifyModal] = useState(false);
  const [showMoreInfoModal, setShowMoreInfoModal] = useState(false);
  const [modalData, setModalData] = useState("");
  const [deleteConfirmData, setDeleteConfirmData] = useState("");
  const [showSSHSettingsModal, setShowSSHSettingsModal] = useState(false);

  const filteredItems = tableData.filter(
    (item) =>
      item.hostName &&
      item.hostName.toLowerCase().includes(filterText.toLowerCase())
  );

  const handleCloseSSHSettingsModal = () => {
    setModalData("");
    setShowSSHSettingsModal(false);
  };

  const handleSaveSSHSettingsModal = (data) => {
    console.log(data.key);
    setModalData("");
    setShowSSHSettingsModal(false);
    setLoadingPage(true);
    setLoadingData("Updating SSH credentials");
    updateSSH(data)
      .unwrap()
      .then((risp) => {
        callGetAllHost(user);
        setLoadingPage(false);
        toast.success(risp.message);
      })
      .catch((err) => {
        setLoadingPage(false);
        toast.error(err.data.error);
      });
  };

  const handleCloseModifyModal = () => {
    setModalData("");
    setShowModifyModal(false);
  };

  const deleteHostHandler = (hostName) => {
    setOpenConfirmDelete(false);
    setLoadingData("Deleting host");
    setLoadingPage(true);

    deleteHost(hostName)
      .unwrap()
      .then((risp) => {
        callGetAllHost(user);
        setLoadingPage(false);
        toast.success(risp.message);
      })
      .catch((err) => {
        setLoadingPage(false);
        toast.error(err.data.error);
      });
  };

  const handleSaveUpdate = (newData) => {
    console.log("new data", newData);
    setModalData("");
    setShowModifyModal(false);
    setLoadingData("Updating host");
    setLoadingPage(true);

    updateHost(newData)
      .unwrap()
      .then((risp) => {
        callGetAllHost(user);
        setLoadingPage(false);
        toast.success(risp.message);
      })
      .catch((err) => {
        setLoadingPage(false);
        toast.error(err.data.error);
      });
  };

  const handleCloseMoreInfo = () => {
    setModalData("");
    setShowMoreInfoModal(false);
  };

  const installAgentHandler = (hostName) => {
    setLoadingData("Installing agent");
    setLoadingPage(true);

    installAgent(hostName)
      .unwrap()
      .then((risp) => {
        callGetAllHost(user);
        setLoadingPage(false);
        toast.success(risp.message);
      })
      .catch((err) => {
        setLoadingPage(false);
        toast.error(err.data.error);
      });
  };

  const pluginHandler = (row) => {
    console.log("plugin handler", row);
    dispatch(addData(row));
    navigate("/plugins");
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
      callGetAllHost(user);
    };

    return (
      <>
        <FilterComponentHost
          onFilter={(e) => setFilterText(e.target.value)}
          onClear={handleClear}
          filterText={filterText}
          refetch={refetchHandler}
        />
      </>
    );
  }, [filterText, resetPaginationToggle]);

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

  return (
    <>
      <div className="box">
        {user && user.decodedAccessToken.roles[0] === "ROLE_USER" ? (
          <UserNavbar />
        ) : (
          <AdminNavbar currentPage={"monitoredHosts"} />
        )}
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
      </div>

      <ConfirmDelete
        show={openConfirmDelete}
        handleClose={closeConfirmDeleteHandler}
        data={deleteConfirmData}
        onYesHandle={deleteHostHandler}
      />

      <ModifyHost
        show={showModifyModal}
        handleClose={handleCloseModifyModal}
        onSaveHandle={handleSaveUpdate}
        data={modalData}
      />
      <MoreInfoHost
        show={showMoreInfoModal}
        handleClose={handleCloseMoreInfo}
        data={modalData}
      />
      <SshSettingsModal
        show={showSSHSettingsModal}
        handleClose={handleCloseSSHSettingsModal}
        onSaveHandle={handleSaveSSHSettingsModal}
        data={modalData}
      />
    </>
  );
}

export default MonitoredHosts;
