# Add value to parameter

This document describes the steps to follow to add parameter using an endpoint.

## Step 1
Authenticate to the backend to get a token.

## Step 2
Invoke the endpoint passing the JSON of the parameter object

```bash
curl -X POST \
  https://[ENVIRONMENT].backend.povertystoplight.org/api/v1/parameters \
  -H 'authorization: Bearer [ACCESS_TOKEN]' \
  -H 'content-type: application/json' \
  -d '{
        "keyParameter": "minimum_priority",
        "description": "Minimum priority amount for the life map",
        "typeParameter": "Integer",
        "value": "3"
    }'
```
