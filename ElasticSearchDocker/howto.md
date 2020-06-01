From 
https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html


1. sudo sysctl -w vm.max_map_count=262144

 
2. Three nodes  
use docker-compose.yml file  
> docker-compose up  
test  
>curl -X GET "localhost:9200/_cat/nodes?v&pretty"  


3. give permission for the mount dir
docker volume ls
docker volume inspect [name]
-->
/var/lib/docker/volumes/elasticsearchdocker_data01/_data
/var/lib/docker/volumes/elasticsearchdocker_data02/_data
/var/lib/docker/volumes/elasticsearchdocker_data03/_data

-->
chmod g+rwx /var/lib/docker/volumes/elasticsearchdocker_data01/_data
chgrp 0 /var/lib/docker/volumes/elasticsearchdocker_data01/_data
chmod g+rwx /var/lib/docker/volumes/elasticsearchdocker_data02/_data
chgrp 0 /var/lib/docker/volumes/elasticsearchdocker_data02/_data
chmod g+rwx /var/lib/docker/volumes/elasticsearchdocker_data03/_data
chgrp 0 /var/lib/docker/volumes/elasticsearchdocker_data03/_data


4. put index 'twitter'
In Postman
Setting Header: 
Key - Content-Type
Value - application/json
Method - PUT
Address - http://localhost:9200/twitter/


