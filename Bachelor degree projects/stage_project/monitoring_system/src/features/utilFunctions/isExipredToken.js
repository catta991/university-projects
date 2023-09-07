export default function isExpiredToken(expirationTime) {
  const d = new Date();

  let number = d.getTime().toString();

  number = number.substring(0, 10);

  number = parseInt(number);

  return number >= expirationTime;
}
