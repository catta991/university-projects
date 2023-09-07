export default function statusIcon(hostStatus) {
  let icon = "";

  switch (hostStatus) {
    case "UP":
      icon = (
        <i
          className="bi bi-circle-fill"
          title="UP"
          style={{ color: "green" }}
        ></i>
      );
      break;
    case "DOWN":
      icon = (
        <i
          className="bi bi-circle-fill"
          title="DOWN"
          style={{ color: "red" }}
        ></i>
      );
      break;
    default:
      icon = (
        <i
          className="bi bi-circle-fill"
          title="UNREACH"
          style={{ color: "yellow" }}
        ></i>
      );
  }

  return icon;
}
