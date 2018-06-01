var qs = require("qs");
var axios = require("axios");
var request = require("request");
const config = require("./config");

function getSnapshotsBySurvey(accessToken, surveyId, indictarosFilter) {
  const instance = axios.create({
    baseURL: config.BASE_URL,
    headers: { Authorization: `Bearer ${accessToken}` }
  });

  return instance
    .get(`/api/v1/snapshots/survey/${surveyId}`)
    .then(resp => filter(resp.data, indictarosFilter))
    .catch(error => Promise.reject(error));
}

function isNumeric(num) {
  return !isNaN(num);
}

function areValuesEqual(value1, value2) {
  if (isNumeric(value1) && isNumeric(value2)) {
    return parseFloat(value1) === parseFloat(value2);
  }
  return value1 === value2;
}

function filter(snapshots, indicatorsFilter) {
  if (!indicatorsFilter || Object.keys(indicatorsFilter).length === 0) {
    return snapshots;
  }
  const { match, ...indicators } = indicatorsFilter;
  const matchAll = !match || match === "all";
  return snapshots.filter(snapshot => {
    const totalIndicatorsEqual = Object.keys(snapshot)
      .filter(key => Object.keys(indicators).includes(key))
      .reduce((totalEqual, key) => {
        if (areValuesEqual(indicators[key], snapshot[key])) {
          return totalEqual + 1;
        }
        return 0;
      }, 0);
    if (matchAll) {
      return totalIndicatorsEqual === Object.keys(indicators).length;
    }
    return totalIndicatorsEqual > 0;
  });
}

function getCreds() {
  return {
    username: config.username,
    password: config.password,
    grant_type: config.grant_type,
    clientId: config.clientId,
    clientSecret: config.clientSecret
  };
}
// TODO Agregar logica para leer
// el token de env y ver si aún es válido
// si no es válido realizar el login
// con las credenciales
function login(creds = getCreds()) {
  const { clientId, clientSecret, ...restCreds } = creds;
  const options = {
    method: "POST",
    auth: { username: clientId, password: clientSecret },
    data: qs.stringify(restCreds),
    url: config.AUTH_URL
  };

  return axios(options);
}

function getToken(token) {
  const instance = axios.create({
    baseURL: config.BASE_URL,
    headers: { Authorization: `Bearer ${token}` }
  });

  return instance.get(`/oauth/decode`).catch(error => Promise.reject(error));
}

function getSnapshotsImpl({ surveyId, indicatorsFilter }) {
  return login().then(resp =>
    getSnapshotsBySurvey(resp.data.access_token, surveyId, indicatorsFilter)
  );
}

module.exports = getSnapshotsImpl;
