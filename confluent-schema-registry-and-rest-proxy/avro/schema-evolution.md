## Backward compatibility
- New schema can be used to read old data
- new field in new schema has default value

## Forward compatibility
- Old schema can be used to read new data
- New field in new schema will be ignored
- Deleting a field without default results in non-forward compatible

## Full compatibility
- backward and forward compatible
- only add fields with defaults
- only delete fields with defaults

## No compatibility
- Adding/Removing elements from an Enum
- Changing the type of a field (for ex, string -> int)
- Renameing a required field

## Tip
- Make the primary key required
- Give default values to all fields that could be removed in the future
- Be careful when using Enums
- Don't rename fields. Add aliases instead
- Don't delete a required field