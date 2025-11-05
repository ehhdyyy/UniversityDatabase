# University Database 
Java + JDBC demo with PostgreSQL

## Tech Stack
- Gradle 9.2+ (via wrapper)
- JDK 21
- Postgresql (to run psql)


## Important Notes

### 1. PostgreSQL
Install PostgreSQL and `psql`  
Ensure server is running 

### 2. Create the Database (one time)
Connect as a superuser (usually postgres) and create DB + app user:

```bash  
psql -U postgres
```

```sql  
--- inside psql
CREATE DATABASE universitydb;

--- Matches jdbc username and password set in 'applications.properties'
CREATE USER admin WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE universitydb TO admin;

---connects to 'universitydb'
\c universitydb 

--allows jdbc schema public privileges 
GRANT ALL PRIVILEGES ON SCHEMA public TO admin;
ALTER SCHEMA public OWNER TO admin;
```

### 3. Build & Run (Gradle Wrapper)

**Always run from backend/ (where ```build.gradle``` lives)**
```bash

# shows Gradle + JVM versions
./gradlew -v            # macOS/Linux
.\gradlew.bat -v        # Windows

# compile & run 
./gradlew run            # macOS/Linux
.\gradlew.bat run        # Windows

```


Typical dev loop
```bash
# edit code…
./gradlew run          # compiles changed files and runs Main
# or to verify everything and produce JAR:
./gradlew clean build
```

### 4. Testing Code
  Open multiple terminals, preferrably one with the database and another for running Main.java.


