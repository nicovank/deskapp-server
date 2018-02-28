# ResLife DeskApp

## The variables

Variables like the SQL Path, and other paths are currently dispatched in different files.
Eventually they will be neatly stored in config files.
For now, here is where you can find them:

 - `app/src/Communication/Messages.js`
 - `WEB-INF/src/com/oswego/deskapp/sql/SQLConnection.java`

## Run the project as a developer:

 - Make sure your SQL Database is up and running, with the necessary tables (see `SQL.md`)
 - Run the following commands to clone both repositories:

```sh
$ cd < TOMCAT_INSTALLATION_PATH > /webapps
$ mkdir reslife
$ cd reslife
$ git clone https://github.com/nicovank/reslife-deskapp-server.git WEB-INF
$ git clone https://github.com/nicovank/reslife-deskapp.git app
```

 - Once both repositories are cloned, open the IDEA project in IntelliJ, 
 modify any necessary path (SQL, others) and build the sources.
 You might need to add some libraries to the Classpath, namely Tomcat's `servlet-api.jar`.
 A configuration should already exist so that all output files go directly in `WEB-INF/classes`.
 The server is now all set-up.

 - Finally, run the following commands to get the browser app up and running:

```sh
cd app
npm start
```

This will open a new tab in your default browser and (hopefully) display the app.

You are now all set! Source files are available at `WEB-INF/src` and `app/src`