## Avro
- defined by a schema, which is written in JSON
- you can see Avro as JSON with a schema attached to it
- Advantage
    - data is fully typed
    - compressed automatically
    - Schema comes along
- Disadvantage
    - can't print without using Avro tool (since it's compressed and serialized)
    - Not all languages supports Avro

## Avro Primitive Types
- null
- boolean
- int : 32-bit signed
- long : 64-bit signed
- float : single precision (32-bit) floating-point number 
- double : double precision (64-bit) floating-point number
- byte : sequence of 8-bit unsigned bytes
- string : unicode character sequence

## Avro Record Schemas
- Common fields
    - Name : Name of the schema
    - Namespace : equivalent of package in java
    - Doc : Documentation
    - Aliases : Optional other name for the schema
    - Fields
        - Name : Name of the field
        - Doc : Documentation
        - Type : Data type of the field
        - Deafult : Default value

## Example
- Customer
    - first name
    - last name
    - age
    - height
    - weight
    - automated email turned on ?
- looks like below
    ```
    {
        "type": "record",
        "namespace": "com.example",
        "name": "Customer",
        "doc": "Avro Schema for our Customer",     
        "fields": [
        { "name": "first_name", "type": "string", "doc": "First Name of Customer" },
        { "name": "last_name", "type": "string", "doc": "Last Name of Customer" },
        { "name": "age", "type": "int", "doc": "Age at the time of registration" },
        { "name": "height", "type": "float", "doc": "Height at the time of registration in cm" },
        { "name": "weight", "type": "float", "doc": "Weight at the time of registration in kg" },
        { "name": "automated_email", "type": "boolean", "default": true, "doc": "Field indicating if the user is enrolled in marketing emails" }
        ]
    }
    ```
