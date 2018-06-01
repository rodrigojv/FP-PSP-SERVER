var getSnapshots = require("./getSnapshotsImpl");

const queryString = "security=2&registeredToVoteAndVotesInElections=2";

const indicatorsFilter = {
  // security: "2",
  // registeredToVoteAndVotesInElections: "2",
  // regularityOfMeals: "NONE",
  // lat: "54.979645",
  stableIncome: 0,
  entrepreneurialSpirit: 2,
  knowledgeAndSkillsToGenerateIncome: 2,
  match: "any"
};

const params = {
  surveyId: 11,
  indicatorsFilter
};

getSnapshots(params)
  .then(resp => console.log(resp))
  .catch(error => console.log({ error }));
