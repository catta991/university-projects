import { Button } from "react-bootstrap";
import { Modal } from "react-bootstrap";
import MultiSelect from "react-multiple-select-dropdown-lite";
import "react-multiple-select-dropdown-lite/dist/index.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { useEffect, useState } from "react";
import { getAllContactGroups } from "../../utilFunctions/fetchFunction";
import { useSelector } from "react-redux";

function ModifyHost({ data, show, handleClose, onSaveHandle }) {
  let user = useSelector((state) => state.user);

  console.log("ff", data);

  const [inputValue, setInputValue] = useState("");
  const [checkedField, setCheckedField] = useState("");
  const [contactGroupMultiselect, setContactGroupMultiselect] = useState([]);
  const [contactgroups, setContactGroupState] = useState(
    data.contactGroups !== "none" ? data.contactGroups : []
  );

  console.log("contact groups", data.contactGroups);
  const callGetContactGroups = async () => {
    const data = await getAllContactGroups(user);

    console.log("data", data);

    let contactGroupApp = [];

    for (let i = 0; i < data.length; i++) {
      contactGroupApp.push({
        label: data[i].name,
        value: data[i].name,
      });
    }

    console.log("use effect", contactGroupApp);

    setContactGroupMultiselect(contactGroupApp);
  };

  useEffect(() => {
    if (user && user.decodedAccessToken.roles[0] !== "ROLE_USER")
      callGetContactGroups();

    return () => {};
  }, []);

  let groups = [];
  const groupChangheHandler = (val) => {
    console.log("val ", typeof val);
    groups = [];
    if (val !== "") groups = val.split(",");
    console.log("gropus " + groups);
    setContactGroupState(groups);
  };

  // handler per leggere il contenuto di un file ottenuto mediante questo form

  /*
<input
            type="file"
            className="form-control"
            placeholder={data.alias}
            style={{ marginTop: "30px", marginLeft: "-107px" }}
            onChange={(e) => selectFileHandler(e)}
          />
  */

  /*
  const selectFileHandler = (e) => {
    e.preventDefault();
    const reader = new FileReader();
    reader.onload = (e) => {
      const text = e.target.result;
      setInputValue(text);
      console.log(text);
    };
    reader.readAsText(e.target.files[0]);
  };*/

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" keyboard={false}>
      <Modal.Header>
        <Modal.Title>Modify Host {data.hostName} </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="input-group mb-3">
          <label>Write new alias</label>
          <input
            type="text"
            className="form-control"
            placeholder={data.alias}
            style={{ marginTop: "30px", marginLeft: "-107px" }}
            onChange={(e) => setInputValue(e.target.value)}
          />

          <label style={{ marginTop: "25px", marginRight: "300px" }}>
            Select contact groups
          </label>

          <MultiSelect
            onChange={groupChangheHandler}
            defaultValue={
              data.contactGroups && data.contactGroups.replaceAll(" ", "")
            }
            options={contactGroupMultiselect}
            style={{
              marginTop: "5px",
              marginRight: "200px",
            }}
          />
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="outline-danger"
          onClick={() => {
            setInputValue("");
            handleClose();
          }}
        >
          Close
        </Button>

        <Button
          variant="outline-success"
          onClick={() => {
            let value = "";
            if (inputValue === "") value = data.alias;
            else value = inputValue;

            let selectedFiled = "";

            if (checkedField === "") selectedFiled = data.agent_install_mode;
            else selectedFiled = checkedField;
            setInputValue("");
            console.log(checkedField);
            onSaveHandle({
              hostName: data.hostName,
              alias: value,
              contactgroups: contactgroups,
              os_family: data.os,
              agent_install_mode: data.agent_install_mode,
              plugins_tag: data.pluginsTag,
            });
          }}
        >
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ModifyHost;
