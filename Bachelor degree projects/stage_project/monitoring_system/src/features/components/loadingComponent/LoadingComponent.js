import { Modal } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../../css/loading.css";

function LoadingComponent({ data, show }) {
  return (
    <Modal
      show={show}
      backdrop="static"
      keyboard={false}
      aria-labelledby="contained-modal-title-vcenter"
      centered
      style={{
        height: "300px",
        width: "450px",
        background: "#20272e",
        color: "#20272e",
        alignContent: "center",
        marginLeft: "35%",
        marginTop: "15%",
      }}
    >
      <div className="wrapper">
        <div className="circle"></div>
        <div className="circle"></div>
        <div className="circle"></div>
        <div className="shadow"></div>
        <div className="shadow"></div>
        <div className="shadow"></div>
        <span>{data}</span>
      </div>
    </Modal>
  );
}

export default LoadingComponent;
