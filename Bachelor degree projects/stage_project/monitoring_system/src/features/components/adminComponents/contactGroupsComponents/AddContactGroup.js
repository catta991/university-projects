import { Button } from "react-bootstrap";
import { Modal } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { useState } from "react";

function AddContactGroup({ show, handleClose, onSaveHandle }) {
  const [name, setName] = useState("");
  const [alias, setAlias] = useState("");

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" keyboard={false}>
      <Modal.Header>
        <Modal.Title>Add new contact group</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <form>
          <div className="form-group">
            <label htmlFor="name">Name</label>
            <input
              id="name"
              type="text"
              className="form-control"
              placeholder="Contact group name"
              required
              style={{ textIndent: "0px" }}
              onChange={(e) => setName(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label htmlFor="alias">Alias</label>
            <input
              id="alias"
              type="text"
              className="form-control"
              placeholder="Contact group alias"
              required
              style={{ textIndent: "0px" }}
              onChange={(e) => setAlias(e.target.value)}
            />
          </div>
        </form>
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="outline-danger"
          onClick={() => {
            setAlias("");
            setName("");
            handleClose();
          }}
        >
          Close
        </Button>

        <Button
          variant="outline-success"
          disabled={name.length === 0 || alias.length === 0}
          onClick={() => {
            const contactGroupName = name;
            const contactGroupAlias = alias;
            setAlias("");
            setName("");
            onSaveHandle({ name: contactGroupName, alias: contactGroupAlias });
          }}
        >
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default AddContactGroup;
