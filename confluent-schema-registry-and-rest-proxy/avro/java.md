## Generic Record
- A Generic record is used to create an avro object from a schema
- the schema is referenced as a file / a string
- it's not recommended way of creating Avro object since it can fail at runtime

## Specific Record
- A Specific record is obtained using code generation from an Avro schema
- There are different plugins for different build tools (gradle, maven, sbt, etc)
- How it works
    ```
    Avro Schema -> Maven Plugin -> Generated Code
    ```