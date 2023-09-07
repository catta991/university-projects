import { Button } from "react-bootstrap";
import { Modal } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

function DetailsComponent({ data, show, handleClose }) {
  return (
    <Modal show={show} onHide={handleClose} backdrop="static" keyboard={false}>
      <Modal.Header>
        <Modal.Title>{data && data.username}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>Role: {data && data.roles} </p>
        <p>Contact Groups: {data && data.contactGroups}</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="outline-danger" onClick={handleClose}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default DetailsComponent;
