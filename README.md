# Assets HTTP API - Delete All Assets from Folder

## Context
This application is used to delete all assets out of a single folder, while ignoring any folders

### Algorithm
1. Retrieve list of all items in the folder
2. Loop through all items, ignoring folders
3. Delete each one, one at a time

## Application
The application is a single module maven Java standalone application.
### Compilation
1. Checkout code and go to the root folder
2. Execute `mvn clean package`
3. All code should be compiled

### Source Files
The application uses two source files:
* Server configuration which has information about the server.
* Folder configuration which has information about the folder to delete assets from

#### Server Configuration
Example:
```
<XML goes here>
```

* Field1: [required] Field 1 description

### Folder Configuration
```
<XML goes here>
```

* Field1: [required] Field 1 description

## Delete assets in folder application
Use the AEM HTTP Assets API to delete all assets within a single folder.

###  Usage
 ```
usage: java -jar delete-all-assets-from-single-folder-1.0.jar
-s <arg>   file path for server configuration.
-f <arg>   file path for folder configuration.
```

### Example
```
mvn clean package
cd delete-all-assets-from-single-folder/target/
java -jar delete-all-assets-from-single-folder-1.0.jar -s server.xml -f folder.xml
```

### Logs
The logs will be stored on `./logs` from where the jar was launched.
