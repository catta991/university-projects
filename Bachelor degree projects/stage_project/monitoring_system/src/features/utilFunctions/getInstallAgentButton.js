export function getIntsallAgentButton(row, user, handler) {
  let button = null;

  if (row.tagAgent === null) {
    button = (
      <button
        className="btn btn-outline-secondary"
        onClick={() => handler(row.hostName)}
        data-toggle="modal"
        data-target="#exampleModal"
        disabled={user && user.decodedAccessToken.roles[0] === "ROLE_USER"}
      >
        Install Agent
      </button>
    );
  } else {
    button = (
      <button
        className="btn btn-outline-success"
        onClick={() => handler(row.hostName)}
        data-toggle="modal"
        data-target="#exampleModal"
        disabled={true}
      >
        Already installed
      </button>
    );
  }

  return button;
}
