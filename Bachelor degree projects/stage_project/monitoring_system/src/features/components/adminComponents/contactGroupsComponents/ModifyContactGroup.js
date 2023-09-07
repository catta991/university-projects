import { Button } from "react-bootstrap";
import { Modal } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { useState } from "react";

function ModifyContactGroup({ data, show, handleClose, onSaveHandle }) {
  const [inputValue, setInputValue] = useState("");

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" keyboard={false}>
      <Modal.Header>
        <Modal.Title>Modify Alias</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="input-group mb-3">
          <input
            type="text"
            className="form-control"
            placeholder="New alias"
            required
            onChange={(e) => setInputValue(e.target.value)}
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
          disabled={inputValue.length === 0}
          onClick={() => {
            const value = inputValue;
            setInputValue("");
            onSaveHandle({ name: data.name, alias: value });
          }}
        >
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ModifyContactGroup;
