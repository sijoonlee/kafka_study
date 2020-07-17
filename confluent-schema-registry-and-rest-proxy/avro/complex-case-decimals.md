## Floats and Doubles
- These are floating *binary* point types
- for example, 10001.1100101
- used for scientific computations (to be fast)

## Decimal
- It is a floating *decimal* point type
- for example, 123.11145
- used for money (to be exact)

## Avro's logical type - decimal
- underlying type is 'bytes'
- currently error prone
- before it's fixed, use string instead and parse it