# Redis Distributed Lock

`Redisson` distributed lock that relies on `Redis`.

```java
    Config config=new Config();
    config.useSingleServer().setAddress("redis://xxxx:xxxx").setPassword("xxxxxx");
    RedissonClient redissonClient=Redisson.create(config);
    Lock lock=new RedisLock(client,"lock_key");
    lock.lock();
    try{
        ...
    }finally{
        lock.unLock();
    }
```