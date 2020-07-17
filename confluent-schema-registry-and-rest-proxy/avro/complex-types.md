## Complex Types
- Enums
- Arrays
- Maps
- Unions
- Calling other schemas as type

## Enums
- values are enumerated
- once an enum is set, changing the enum values is forbidden if you want to maintain compatibility
- example
    - bronze
    - silver
    - gold
- {"type": "enum", "name":"CustomerLevel", "symbols":["bronze", "silver", "gold]}

## Array
- Arrays use the type name "array" and support a single attribute:
- items: the schema of the array's items.
- For example, an array of strings is declared with:
    ```
    {
    "type": "array",
    "items" : "string",
    "default": []
    }
    ```

## Map
- key value pair with string keys
- For example, a map from string to long is declared with:
    ```
    {"type": "map", "values": "long"}
    ```

## Union
- allow a field value to take different types
- example
    ```
    ["string", "int", "boolean"]
    ```
- if defaults are set, the default must be of the type of first item in union
- The most common use case for union is to define an optional value
    ```
    ["name":"middle_name", "type":["null", "string"]], "default":null]
    ```
    *note that default value is null, not "null"*


## Example
```
[
  {
      "type": "record",
      "namespace": "com.example",
      "name": "CustomerAddress",
      "fields": [
        { "name": "address", "type": "string" },
        { "name": "city", "type": "string" },
        { "name": "postcode", "type": ["string", "int"] },
        { "name": "type", "type": "enum", "symbols": ["PO BOX", "RESIDENTIAL", "ENTERPRISE"] }
      ]
  },
  {
     "type": "record",
     "namespace": "com.example",
     "name": "Customer",
     "fields": [
       { "name": "first_name", "type": "string" },
       { "name": "middle_name", "type": ["null", "string"], "default": null },       
       { "name": "last_name", "type": "string" },
       { "name": "age", "type": "int" },
       { "name": "height", "type": "float" },
       { "name": "weight", "type": "float" },
       { "name": "automated_email", "type": "boolean", "default": true },
       { "name": "customer_emails", "type": "array", "items": "string", "default": []},
       { "name": "customer_address", "type": "com.example.CustomerAddress" }
     ]
  }
]
```