import FilterComponentsPlugin from "./FilterComponentsPlugin";
import DataTable from "react-data-table-component";
import {
  getPluginLinuxInfo,
  getPluginWidowsInfo,
} from "../../utilFunctions/getPluginInfo";
import { useState, useMemo } from "react";
import "../../../css/tablePlugin.css";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { useSelector, useDispatch } from "react-redux";
import { useInstallPluginsMutation } from "../../reduxApi/hostApi";
function PluginComponent({ setLoadingData, setLoadingPage }) {
  const dispatch = useDispatch();
  let host = useSelector((state) => state.shareData);

  const [installPluginMutation] = useInstallPluginsMutation();

  console.log("host", host);

  const columns = [
    {
      name: "name",
      selector: (row) => row.name,
      sortable: true,
      width: "300px",
      center: "true",
    },
    {
      name: "size",
      selector: (row) => row.size,
      sortable: true,
      width: "100px",
      center: "true",
    },
  ];

  const [filterText, setFilterText] = useState("");
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);

  const navigate = useNavigate();
  let pluginList = [];

  const filteredItems = (
    host.os === "linux" ? getPluginLinuxInfo() : getPluginWidowsInfo()
  ).filter(
    (item) =>
      item.name && item.name.toLowerCase().includes(filterText.toLowerCase())
  );

  const onClickRow = (row) => {
    // row è un oggetto a noi interessa row.selectedRows per l'installazione dei plugin
    console.log("in on click row", row);
    pluginList = row.selectedRows;
    console.log("ffff", pluginList);
  };

  const onBackHandler = () => {
    console.log("on back");
    navigate(-1);
  };

  const disable = (row) => host.pluginsTag.split(",").includes(row.name);

  const subHeaderComponentMemo = useMemo(() => {
    const handleClear = () => {
      if (filterText) {
        setResetPaginationToggle(!resetPaginationToggle);
        setFilterText("");
      }
    };

    const installPlugin = (row) => {
      console.log("install ", pluginList);
      if (pluginList.length === 0) toast.error("no plugin selected");
      else {
        setLoadingData("Installing plugins");
        setLoadingPage(true);

        let pluginsName = [];

        for (let i = 0; i < pluginList.length; i++) {
          pluginsName.push(pluginList[i].name);
        }
        console.log("names", pluginsName);

        installPluginMutation({
          hostName: host.hostName,
          plugins: { pluginsName },
          agent_install_mode: row.agent_install_mode,
        })
          .unwrap()
          .then((risp) => {
            setLoadingPage(false);

            console.log(risp);
            navigate(-1);
          })
          .catch((err) => {
            setLoadingPage(false);

            toast.error(err.data.error);
            navigate(-1);
          });
      }
    };

    return (
      <>
        <FilterComponentsPlugin
          onFilter={(e) => setFilterText(e.target.value)}
          onClear={handleClear}
          filterText={filterText}
          onBack={onBackHandler}
          installPlugin={installPlugin}
        />
      </>
    );
  }, [filterText, resetPaginationToggle]);

  return (
    <>
      <div className="box">
        <div className="table-plugin-container">
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
            selectableRows
            selectableRowDisabled={disable}
            onSelectedRowsChange={onClickRow}
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
    </>
  );
}

export default PluginComponent;
