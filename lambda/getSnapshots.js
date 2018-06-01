const getSnapshots = require("./getSnapshotsImpl");

exports.handler = (event, context, callback) => {
  // TODO implement
  getSnapshots().then(result => {
    callback(null, result);
  });
};
