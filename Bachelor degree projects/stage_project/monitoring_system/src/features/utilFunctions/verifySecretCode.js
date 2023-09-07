//let bcrypt = require("bcrypt");
import bcrypt from "bcryptjs";

export async function verifySecretCode(encriptedCode, clearCode) {
  const response = await bcrypt.compare(encriptedCode, clearCode);

  return response;
}
