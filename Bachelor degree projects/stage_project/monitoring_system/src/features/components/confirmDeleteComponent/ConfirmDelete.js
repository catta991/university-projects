import { Button } from "react-bootstrap";
import { Modal } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { useState } from "react";

function ConfirmDelete({ data, show, handleClose, onYesHandle }) {
  let name = "";

  if (data.hostName !== null && data.hostName !== undefined) {
    name = data.hostName;
  } else if (data.username !== null && data.username !== undefined) {
    name = data.username;
  } else if (data.name !== null && data.name !== undefined) {
    name = data.name;
  }

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      keyboard={false}
      centered
    >
      <Modal.Body>
        <div> Are you sure to delete {name}? </div>
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="outline-danger"
          onClick={() => {
            handleClose();
          }}
        >
          No
        </Button>

        <Button
          variant="outline-success"
          onClick={() => {
            //console.log(data);
            onYesHandle(name);
          }}
        >
          Yes
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ConfirmDelete;
