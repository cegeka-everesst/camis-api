# CamisAPI

## How to run
Pass as Command Line Argument when running SyncTimesheetApp Main
(all arguments are mandatory)

`--input=... --operation=... --baseUrl=https:// --clientId=... --clientSecret=...` 

The output file generated will contain all success and error messages associated by the chosen operation.

### Input
`--input=...`

The CSV file contains the input timesheet entries to be processed.
The file should have a header and follow the following format :

`date;CAMIS resourceId;employeeName;workOrder;hoursLogged`

### Operation
`--operation`
The operation to execute, there are two possible operations
* **checkWorkOrders :** 
  checks whether each employee in the input csv has access to the specified work order for the first and last date that the employee has registered hours for. 
* **syncTimeSheets :**

### Connection parameters
* `--baseUrl` : the base url towards the Camis REST API
* `--clientId` : the clientId, provided to you by the Camis Horizon administrator
* `--clientSecret` : the clientSecret, provided to you by the Camis Horizon administrator


## Prerequisites
Java 17