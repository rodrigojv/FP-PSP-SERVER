const getSnapshots = require("./getSnapshotsImpl");

exports.handler = (event, context, callback) => {
  const { surveyId } = event.pathParameters;
  const { queryStringParameters: indicatorsFilter } = event;
  const params = {
    surveyId,
    indicatorsFilter
  };
  try {
    getSnapshots(params)
      .then(function(response) {
        let respProxy = {
          statusCode: 200,
          body: JSON.stringify(response)
        };
        callback(null, respProxy);
      })
      .catch(function(err) {
        console.error("Error: " + err);
        callback(null, getErrorReport(err));
      });
  } catch (error) {
    callback(error);
  }
};

function getErrorReport(error) {
  let errorReport;
  if (error.response) {
    // The request was made and the server responded with a status code
    // that falls out of the range of 2xx
    console.log(error.response.data);
    console.log(error.response.status);
    console.log(error.response.headers);
    errorReport = {
      statusCode: error.response.status,
      body: JSON.stringify(error.response.data)
    };
  } else if (error.request) {
    // The request was made but no response was received
    // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
    // http.ClientRequest in node.js
    console.log(error.request);
    errorReport = {
      statusCode: 500,
      body: JSON.stringify(error.request)
    };
  } else {
    // Something happened in setting up the request that triggered an Error
    console.log("Error", error.message);
    errorReport = {
      statusCode: 500,
      body: JSON.stringify({ errorMessage: error.message })
    };
  }
  return errorReport;
}
