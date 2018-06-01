var qs = require("qs");
var axios = require("axios");
var request = require("request");

const PWD = "password";
const data = {
  username: "Transmit Enterprise",
  password: "t1mnsat753",
  grant_type: "password"
};
const authUrl = `https://platform.backend.povertystoplight.org/oauth/token`;
const baseUrl = `https://platform.backend.povertystoplight.org`;
const options = {
  method: "POST",
  auth: { username: "barClientIdPassword", password: "secret" },
  data: qs.stringify(data),
  url: authUrl
};

function getAllFamilies(accessToken) {
  console.log({ accessToken });
  const instance = axios.create({
    baseURL: baseUrl,
    headers: { Authorization: `Bearer ${accessToken}` }
  });

  return instance
    .get("/api/v1/families")
    .then(resp =>
      console.log(
        "families",
        resp.data.filter(fam => !fam.organization || fam.organization === null)
      )
    )
    .catch(error => console.log(error));
}

function countFamilies(accessToken) {
  console.log({ accessToken });
  const instance = axios.create({
    baseURL: baseUrl,
    headers: { Authorization: `Bearer ${accessToken}` }
  });

  return instance
    .get("/api/v1/families/counter")
    .then(resp => console.log("families counter", resp.data))
    .catch(error => console.log(error));
}

function getSnapshotsBySurvey(accessToken, surveyId) {
  console.log({ accessToken });
  const instance = axios.create({
    baseURL: baseUrl,
    headers: { Authorization: `Bearer ${accessToken}` }
  });

  return instance
    .get(`/api/v1/snapshots/survey/${surveyId}`)
    .then(resp => console.log("surveys", resp.data.length))
    .catch(error => console.log(error));
}
