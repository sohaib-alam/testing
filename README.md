# Intentionally vulnerable Java scanner fixture

For isolated scanner testing only. Do not deploy this project to production.

## Expected findings

| Fixture | Expected finding | Appropriate scanner |
| --- | --- | --- |
| VulnerableApp.java: args[3] -> SQL concatenation -> executeQuery | SQL injection, CWE-89 | Java SAST with command-line taint sources |
| pom.xml: log4j-core 2.14.1 | Vulnerable dependency, including CVE-2021-44228 | Software composition analysis (SCA) |
| pom.xml: commons-collections 3.2.1 | Outdated dependency with unsafe deserialization gadget classes | SCA; presence alone does not demonstrate a reachable deserialization exploit |
| Dockerfile: openjdk:8u202-jdk | Old JDK patch level | Container/image scanner or Dockerfile policy |
| .java-version: 1.8.0_202 | Old JDK version declaration | Runtime-version policy, if this format is supported |
| native/overflow.c: strcpy | Stack buffer overflow, CWE-120/CWE-121 | C/C++ SAST; Java-only scanning will generally miss this |

Java source/target 1.8 controls language and bytecode compatibility. It does not prove which JDK runs the application. The pinned 8u202 patch level is deliberately old; Java 8 as a whole is not a reliable vulnerability indicator because vendor support and patch levels vary.

Java arrays are bounds checked. A Java ArrayIndexOutOfBoundsException or ByteBuffer BufferOverflowException is not a native memory corruption vulnerability. This fixture uses JNI to supply a real native buffer overflow.

## Scan first

Extract the whole project and scan Java, C, pom.xml, and Dockerfile. Enable dependency scanning as well as SAST. Findings depend on enabled rules, taint-source configuration, and advisory data; not every scanner detects every item.

## Optional build

With Maven and a compatible installed JDK:

```sh
mvn package
```

The SQL example accepts a JDBC URL, credentials, and username. Add your lab database JDBC driver to the classpath and create a users table with id and username columns before running it. No database is bundled.

For the JNI library on Linux with GCC and JAVA_HOME set to your installed JDK:

```sh
gcc -shared -fPIC -Wall -Wextra -O0 -g \
  -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" \
  native/overflow.c -o libscannerfixture.so
java -Djava.library.path=. -cp target/classes lab.NativeOverflow hello
```

No exploit payloads or external callbacks are required for source scanning. The historical Docker image tag may no longer resolve; this does not prevent scanning the source fixture. A .java-version file is a version-manager declaration, not an automatic installer.

## Advisory references

- https://logging.apache.org/security.html
- https://issues.apache.org/jira/browse/SLING-5814

## Validation performed

Maven XML and ZIP integrity were checked. Java compilation, Maven dependency resolution, and JNI compilation/runtime execution were not performed because this environment has no JDK or Maven installed.
