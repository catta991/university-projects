import { Button } from "react-bootstrap";
import { Modal } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { useState } from "react";

function SshSettingsModal({ data, show, handleClose, onSaveHandle }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [key, setKey] = useState("");
  const [checked, setChecked] = useState(null);

  console.log("checked", checked);

  const onChangeRadioButtonHandler = (text) => {
    console.log(text);
    setUsername("");
    setPassword("");
    setKey("");
    setChecked(text);
    setCheckedProperty(text);
    setBody(text);
  };
  const setCheckedProperty = (text) => {
    if (text === "key") {
      return (
        <>
          <div className="form-check" style={{ marginLeft: "5px" }}>
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault1"
              onChange={() => onChangeRadioButtonHandler("key")}
              defaultChecked
            />
            <label className="form-check-label" for="flexRadioDefault1">
              Key
            </label>
          </div>
          <div className="form-check" style={{ marginLeft: "5px" }}>
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault2"
              onChange={() => onChangeRadioButtonHandler("password")}
            />
            <label className="form-check-label" for="flexRadioDefault2">
              Password
            </label>
          </div>
        </>
      );
    } else {
      return (
        <>
          <div className="form-check" style={{ marginLeft: "5px" }}>
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault1"
              onChange={() => onChangeRadioButtonHandler("key")}
            />
            <label className="form-check-label" for="flexRadioDefault1">
              Key
            </label>
          </div>
          <div className="form-check" style={{ marginLeft: "5px" }}>
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault2"
              defaultChecked
              onChange={() => onChangeRadioButtonHandler("password")}
            />
            <label className="form-check-label" for="flexRadioDefault2">
              Password
            </label>
          </div>
        </>
      );
    }
  };

  const setBody = (accessType) => {
    if (accessType === "password") {
      return (
        <>
          <div className="input-group mb-3">
            <label>Insert user name</label>
            <input
              type="text"
              className="form-control"
              placeholder="Insert user name"
              style={{ marginTop: "30px", marginLeft: "-120px" }}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div className="input-group mb-3">
            <label style={{ marginTop: "0px" }}>Insert password</label>
            <input
              id="insertPassword"
              type="text"
              className="form-control"
              placeholder="Insert password"
              style={{ marginTop: "30px", marginLeft: "-113px" }}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
        </>
      );
    } else {
      return (
        <>
          <div className="input-group mb-3">
            <label>Insert user name</label>
            <input
              type="text"
              className="form-control"
              placeholder="Insert user name"
              style={{ marginTop: "30px", marginLeft: "-120px" }}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div className="input-group mb-3">
            <label>Insert SSH key</label>
            <input
              type="text"
              className="form-control"
              placeholder="Insert SSH key"
              style={{ marginTop: "30px", marginLeft: "-107px" }}
              onChange={(e) => setKey(e.target.value)}
            />
          </div>
        </>
      );
    }
  };

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" keyboard={false}>
      <Modal.Header>
        <Modal.Title>
          Set SHH credentials for host {data && data.hostName}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <label style={{ marginRight: "60px", width: "1000px" }}>
          Select SSH auth mode
        </label>
        {setCheckedProperty(
          checked === null ? data && data.agent_install_mode : checked
        )}
        {setBody(checked === null ? data && data.agent_install_mode : checked)}
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="outline-danger"
          onClick={() => {
            setUsername("");
            setPassword("");
            setKey("");
            setChecked(null);
            handleClose();
          }}
        >
          Close
        </Button>

        <Button
          variant="outline-success"
          onClick={() => {
            const usernameApp = username;
            const passwordApp = password;
            const keyApp = key;
            setUsername("");
            setPassword("");
            setKey("");
            onSaveHandle({
              ipaddress: data.ipAddress,
              hostName: data.hostName,
              plugins_tag: data.pluginsTag,
              os_family: data.os,
              username: usernameApp,
              password: passwordApp,
              key: keyApp.replaceAll(" ", "\n"),
              agent_install_mode:
                checked === null ? data && data.agent_install_mode : checked,
            });
          }}
          disabled={
            (((checked === null && data.agent_install_mode === "password") ||
              (checked !== null && checked === "password")) &&
              (password === "" || username === "")) ||
            (((checked === null && data.agent_install_mode === "key") ||
              (checked !== null && checked === "key")) &&
              (key === "" || username === ""))
          }
        >
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default SshSettingsModal;
