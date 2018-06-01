# AWS LAMBDA endpoints

The server exposes info about snapshots through AWS Lambda/API Gateway.

Base API URL: https://d3w0k61hzb.execute-api.eu-west-2.amazonaws.com/beta

## /snapshots/{surveyId}

Description: Gets snapshots indicators from a provided survey

```
curl --header "x-api-key: [YOUR_API_KEY]" https://d3w0k61hzb.execute-api.eu-west-2.amazonaws.com/beta/snapshots/1
```

**Path parameters**

_surveyId_: the id of the survey

**Query URL Parameters**

Can be any of the indicators properties names for that survey. This can vary for each survey.

The indicators properties values can be any of the following:

```
0 (RED), 1 (YELLOW), 2 (GREEN), "NONE"
```

Example:

```
curl --header "x-api-key: [YOUR_API_KEY]" https://d3w0k61hzb.execute-api.eu-west-2.amazonaws.com/beta/snapshots/11?security=2&registeredToVoteAndVotesInElections=2
```
