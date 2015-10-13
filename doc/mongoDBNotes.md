[TOC]

# Commands
## Basic Commands
show dbs
use test
show collections
db.collectionName.findOne()
db.getCollection('collectionName').count()

Find all indexes:
db.system.indexes.find()
Setup index(Key 值为要创建的索引字段，1为指定按升序创建索引，如果你想按降序来创建索引指定为-1即可):
db.collectionName.ensureIndex({KEY:1})

## Update
post = {title:title}
db.blog.insert(post)
db.blog.find()
post.comment=[]
db.blog.update({"title":"title"},post)

joe={"name","joe"}
joe.relationships={"friends":2,"enemies":3}
db.users.insert(joe)
joe = db.users.findOne();
joe.username=joe.name;
delete joe.name
db.users.update({"name":"joe"},joe)
db.users.update({"name":"joe"},{"$set":{"age":20}})

{ "_id" : ObjectId("55b61a2634a254ddb211bf32"), "relationships" : { "friends" : 2, "enemies" : 3 }, "username" : "joe" }

## Dump & Restore
mongodump -p 31000	mongodump will create a dump
mongorestore
./bin/mongorestore -h 127.0.0.1:27017 -d dbName /path/to/dbName

## Advanced commands
getLastError

## replication replSet
mongod --replSet replSetName -f mongod.conf --fork
