# AWS Configurations

These configurations are necessary if image upload functionality is used in the application.  

## User Permissions

Create an AWS IAM user with at least the following permissions: 
(or create a Policy with the permissions and assign it to the user)

* `PutObject` (add an object to a bucket)
* `PutObjectAcl` (set access control list ACL permissions to an object in a bucket, e.g. set for public access)
* `DeleteObject` (delete an object in the bucket)

## Set up AWS Credentials

Set the user credentials in the AWS credentials profile file on your local system, located at:

* _`~/.aws/credentials`_ on Linux, macOS, or Unix
* _`C:\Users\USERNAME\.aws\credentials`_ on Windows

This file should contain lines in the following format:

```
[default]
aws_access_key_id = your_access_key_id
aws_secret_access_key = your_secret_access_key
```

Substitute your own AWS credentials values for the values _your_access_key_id_ and _your_secret_access_key_.


## Set up AWS S3 Bucket properties

Set the AWS S3 bucket properties in application-properties files _`application-dev.properties`_ and/or _`application-default.properties`_ located at _`/src/main/resources/`_

Set up the bucket name:
```
application.aws.bucketName=py.org.fundacionparaguaya.psp.images
```

Set up the bucket region:  
```
application.aws.strRegion=EU_WEST_2
```
for example: `EU_WEST_2` (EU-London region)

