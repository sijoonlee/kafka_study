### buffer.memery = 33554432 (32MB) by default

### max.block.ms = 6000 (60s) by default

### Execptions are thrown when
- The producer has filled up its buffer
- The broker doesn't accept new data
- 60 seconds of max.block.ms has elapsed

### Why does it happen
- the producer produces faster than the broker can take
- the producer keeps messages in buffer (32MB)
- if the buffer is full, .send() method will be blocked
- when the amound of time .send() being blocked reachs max.block.ms (60s), the execption is thrown
