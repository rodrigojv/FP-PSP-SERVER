# Updating terms and conditions

This document describes the steps to follow to update the terms and conditions using an endpoint.

## Step 1.
Create a html file with the new term and conditions content.

```bash
$ touch test.html
$ echo "<body>new content</body>" >> test.html
```
## Step 2
Authenticate to the backend to get a token.
Invoke the endpoint to get the ID of the term and condtitions you want to update.

```bash
curl -i -H "Authorization: Bearer [ACCESS_TOKEN]" http://localhost:8080/api/v1/termcondpol
```
## Step 3
Invoke the endpoint passing the ID of the term and conditions entry and also the path to your html file using the `-F` to send as a `multipart/form` request. 

```bash
curl -X PUT -F "html_file=@test.html" -H "Authorization: Bearer [ACCESS_TOKEN" https://[ENVIRONMENT].backend.povertystoplight.org/api/v1/termcondpol/[TERM_ID]
```
