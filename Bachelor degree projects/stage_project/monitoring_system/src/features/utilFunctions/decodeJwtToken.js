import jwt_decode from "jwt-decode";

// funzione di utility per decodificare il token in caso di login effettuato con successo

export default function decodeToken(jwtToken) {
  return jwt_decode(jwtToken);
}
