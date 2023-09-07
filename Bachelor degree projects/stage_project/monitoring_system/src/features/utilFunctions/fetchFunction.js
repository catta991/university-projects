export async function getSingleUserInfo(loggedUser) {
  console.log("logged user", loggedUser);

  const response = await fetch(
    "http://localhost:8092/auth/user/getSingleUser/" +
      loggedUser.decodedAccessToken.sub,
    {
      headers: {
        "Content-Type": "application/json",
        Auth: "Bearer " + loggedUser.accessToken,
        Pwd: loggedUser.password,
      },
    }
  ).then((resp) => resp.json());

  return response;
}

export async function getAllUsers(loggedUser) {
  console.log("logged user", loggedUser);

  const response = await fetch("http://localhost:8092/auth/user/checkMk", {
    headers: {
      "Content-Type": "application/json",
      Auth: "Bearer " + loggedUser.accessToken,
      Pwd: loggedUser.password,
    },
  }).then((resp) => resp.json());

  return response;
}

export async function getAllContactGroups(loggedUser) {
  const response = await fetch("http://localhost:8092/auth/contactGroups", {
    headers: {
      "Content-Type": "application/json",
      Auth: "Bearer " + loggedUser.accessToken,
      Pwd: loggedUser.password,
    },
  }).then((resp) => resp.json());

  return response;
}

/*
export async function getContactGroupsSuperAdmin(loggedUser) {
  console.log("logged user", loggedUser);

  const response = await fetch(
    "http://localhost:8092/auth/userContactGroup",

    {
      headers: {
        "Content-Type": "application/json",
        Auth: "Bearer " + loggedUser.accessToken,
        Pwd: loggedUser.password,
      },
    }
  ).then((resp) => resp.json());

  return response;
}*/

export async function getRoles(loggedUser) {
  const response = await fetch(
    "http://localhost:8092/auth/user/roles",

    {
      headers: {
        "Content-Type": "application/json",
        Auth: "Bearer " + loggedUser.accessToken,
        Pwd: loggedUser.password,
      },
    }
  ).then((resp) => resp.json());

  return response;
}

export async function getMonitoredHosts(loggedUser) {
  const response = await fetch(
    "http://localhost:8092/auth/host/getHosts",

    {
      headers: {
        "Content-Type": "application/json",
        Auth: "Bearer " + loggedUser.accessToken,
        Pwd: loggedUser.password,
      },
    }
  ).then((resp) => resp.json());

  return response;
}

export async function getUnmonitoredHosts(loggedUser) {
  const response = await fetch(
    "http://localhost:8092/auth/host/unmonitored/hosts",

    {
      headers: {
        "Content-Type": "application/json",
        Auth: "Bearer " + loggedUser.accessToken,
        Pwd: loggedUser.password,
      },
    }
  ).then((resp) => resp.json());

  return response;
}

export async function getSecretCode(loggedUser) {
  console.log("loggeduser", loggedUser);

  const response = await fetch(
    "http://localhost:8092/auth/user/get/secret/code",

    {
      headers: {
        "Content-Type": "application/json",
        Auth: "Bearer " + loggedUser.access_token,
      },
    }
  ).then((resp) => resp.json());

  return response;
}
