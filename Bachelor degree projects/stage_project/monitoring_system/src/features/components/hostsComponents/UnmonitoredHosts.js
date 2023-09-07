import DataTable from "react-data-table-component";
import { useState, useMemo, useEffect, useImperativeHandle } from "react";
import "../../../css/table.css";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import statusIcon from "../../utilFunctions/statusClassName";
import FilterComponentUnmonitoredHost from "./FilterComponentUnmonitoredHosts";
import AdminNavbar from "../adminComponents/AdminNavbar";
import { getUnmonitoredHosts } from "../../utilFunctions/fetchFunction";
import isExpiredToken from "../../utilFunctions/isExipredToken";
import MoreInfoHost from "./MoreInfoHost";
import { useAddHostToMonitoringMutation } from "../../reduxApi/hostApi";
import { toast } from "react-toastify";
function UnmonitoredHosts({
  openRefreshToken,
  showRefToken,
  setLoadingData,
  setLoadingPage,
}) {
  const navigate = useNavigate();
  const [addToMonitoring] = useAddHostToMonitoringMutation();
  let user = useSelector((state) => state.user);
  let [tableLoading, setTableLoading] = useState(true);
  let [tableData, setTableData] = useState([]);
  let [isError, setIsError] = useState(false);
  const [filterText, setFilterText] = useState("");
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);
  const [showMoreInfoModal, setShowMoreInfoModal] = useState(false);
  const [modalData, setModalData] = useState("");
  const [selectedHost, setSelectedHost] = useState(null);
  const [selectedContactGroups, setSelectedContactGroups] = useState([]);

  let selected = [];

  const callGetUnmonitoredHost = async () => {
    const data = await getUnmonitoredHosts(user).catch((err) =>
      console.log("in catch")
    );

    let app = [];
    console.log("get", data);

    if (data !== undefined) {
      for (let i = 0; i < data.length; i++) {
        // creo degli oggetti di supporto che mi servono per visualizzare
        // i vari dati nella tabella
        // da aggiungere anche i gruppi

        let contactGroups = null;

        if (data[i].contactgroups !== null)
          contactGroups = data[i].contactgroups.join(", ");

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
          creationDate: data[i].creation_date,
        });
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
      width: "210px",
    },
    {
      name: "Alias",
      selector: (row) => row.alias,
      sortable: true,
      width: "210px",
    },

    {
      name: "Status",
      selector: (row) => row.status,
      center: "true",
      width: "210px",
      cell: (row) => statusIcon(row.status),
    },
    {
      name: "CreationDate",
      selector: (row) => row.creationDate,
      sortable: true,
      center: "true",
      width: "210px",
    },

    {
      name: "More info",
      selector: (row) => row.moreInfo,
      center: "true",
      width: "210px",
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
      callGetUnmonitoredHost(user);
    }
    return () => {};
  }, []);

  const filteredItems = tableData.filter(
    (item) =>
      item.hostName &&
      item.hostName.toLowerCase().includes(filterText.toLowerCase())
  );

  const handleCloseMoreInfo = () => {
    setModalData("");
    setShowMoreInfoModal(false);
  };

  const contactGroupChangheHandler = (val) => {
    if (val !== undefined && val !== null)
      setSelectedContactGroups(val.split(","));
  };

  const onClickRow = (row) => {
    console.log("selected rows", row.selectedRows);
    setSelectedHost(row);
    //console.log("click", selected);
    // da sistemare
  };

  const onAddHandler = () => {
    // mi serve l'intero array delle selected rows

    console.log("on add groups", selectedContactGroups);

    console.log("on add host", selectedHost);

    if (
      selectedContactGroups.length === 0 ||
      selectedHost.selectedRows.length === 0
    ) {
      toast.error("no contactgroups or host selected");
    } else {
      let body = [];

      for (let i = 0; i < selectedHost.selectedRows.length; i++) {
        if (
          selectedHost.selectedRows[i].contactGroups !== null &&
          selectedHost.selectedRows[i].contactGroups.length > 0
        ) {
          body.push({
            hostName: selectedHost.selectedRows[i].hostName,
            contactGroups: [
              selectedHost.selectedRows[i].contactGroups,
              ...selectedContactGroups,
            ],
          });
        } else {
          body.push({
            hostName: selectedHost.selectedRows[i].hostName,
            contactGroups: [...selectedContactGroups],
          });
        }
      }

      console.log(body);

      setLoadingData("adding hosts");
      setLoadingPage(true);
      addToMonitoring(body)
        .unwrap()
        .then((risp) => {
          setLoadingPage(false);
          toast.success(risp.message);
          navigate("/monitored/hosts");
        })
        .catch((err) => {
          setLoadingPage(false);
          toast.error(err.data.error);
        });
    }

    // navigate("/monitored/hosts");
  };

  const subHeaderComponentMemo = useMemo(() => {
    const handleClear = () => {
      if (filterText) {
        setResetPaginationToggle(!resetPaginationToggle);
        setFilterText("");
      }
    };

    const refetchHandler = () => {
      console.log("refetch");
      callGetUnmonitoredHost(user);
    };

    return (
      <>
        <FilterComponentUnmonitoredHost
          onFilter={(e) => setFilterText(e.target.value)}
          onClear={handleClear}
          filterText={filterText}
          refetch={refetchHandler}
          onAdd={onAddHandler}
          contactGroupChangheHandler={contactGroupChangheHandler}
        />
      </>
    );
  }, [filterText, resetPaginationToggle, selectedHost, selectedContactGroups]);

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
        <AdminNavbar currentPage={"unmonitoredHosts"} />
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
            selectableRows
            onSelectedRowsChange={(row) => onClickRow(row)}
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

      <MoreInfoHost
        show={showMoreInfoModal}
        handleClose={handleCloseMoreInfo}
        data={modalData}
      />
    </>
  );
}

export default UnmonitoredHosts;
