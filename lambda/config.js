const { grant_type, clientId, clientSecret } = require("./creds_config.json");

module.exports = {
  AUTH_URL: process.env.AUTH_URL,
  BASE_URL: process.env.BASE_URL,
  username: process.env.USERNAME,
  password: process.env.PASSWORD,
  accessToken: process.env.ACCESS_TOKEN,
  grant_type: process.env.GRANT_TYPE || grant_type,
  clientId: process.env.CLIENT_ID || clientId,
  clientSecret: process.env.CLIENT_SECRET || clientSecret
};
