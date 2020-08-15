## Query Explain Plans
- show the query plan of a query
ex) find the id of a running query
    - CTAS_xxx ; create table as select
    - CSAS_xxx ; create stream as select
    - explain CSAS_SOME_STREAM; - will show the detail, topology
- display the execution plan for a SQL expression
    - show the execution plan
    - runtime information and metrics
    - visualizing the plan