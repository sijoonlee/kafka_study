## Logical types
- decimals (bytes)
- date (int) : number of days since unix epoch (Jan 1st, 1970)
- timestamp-millis (long) : the number of milliseconds since unix epoch (00:00:00.000 UTC Jan 1st, 1970)
- time-millis (long) : the number of milliseconds after midnight, 00:00:00.000

## Syntax
```
{"name":"signup_ts", "type":"long", "logicalType":"timestampe-millis"}
```

## Note
- logical types are new, not fully supported
- unions have problem with it