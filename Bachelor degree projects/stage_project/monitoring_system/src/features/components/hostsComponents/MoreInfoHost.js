import { Button } from "react-bootstrap";
import { Modal } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

function MoreInfoHost({ data, show, handleClose }) {
  return (
    <Modal show={show} onHide={handleClose} backdrop="static" keyboard={false}>
      <Modal.Header>
        <Modal.Title>{data && data.hostName}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>Operating system: {data && data.os} </p>
        <p>
          Contact Groups:{" "}
          {data && (data.contactGroups !== null ? data.contactGroups : "none")}
        </p>
        <p>Plugins: {data && data.pluginsTag.replaceAll(",", " ")}</p>
        <p>IP address: {data && data.ipAddress}</p>
        <p>Agent install mode: {data && data.agent_install_mode}</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="outline-danger" onClick={handleClose}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default MoreInfoHost;
