Issue Magement Service
-------------------------

 
 - [Detailed description] (https://docs.google.com/presentation/d/1aOidSFxUFvHt6FAF6jjW6RWqGaiYezb9f8GgZmGxYno/edit) in google docs.


> HTTP REST Web Service based on:

> [Spray] (http://spray.io/home) ver 1.0 - low-level network IO on top of Scala and Akka. Used: <b>base, can & server</b> modules.

> [Lift JSON] (https://github.com/lift/lift/tree/master/framework/lift-base/lift-json/) ver 2.5.0 - scala parsing and formatting utilities for JSON.

> [Akka] (http://akka.io/) ver 2.0.2 - toolkit and runtime for building highly concurrent, distributed, and fault tolerant event-driven applications on the JVM.

> [casbah] (https://github.com/mongodb/casbah) ver 2.1.5 - scala toolkit for MongoDB.

> [salat] (https://github.com/novus/salat) ver 1.9.1 - simple serialization library for case classes. Supports bidirectional serialization for MongoDB's DBObject (using casbah).

> [Scalaz] (http://code.google.com/p/scalaz/) ver 6.0.4 - Type Classes and Pure Functional Data Structures for Scala.

> [Specs2] (http://etorreborre.github.com/specs2/) ver 1.9 - library for writing executable software specifications for scala.

> [mongoDB] (http://www.mongodb.org) ver 2.2.0 - high-performance, open source NoSQL database. 

------------------------

To build and run this project:

    > git clone https://github.com/immediatus/issue-management-servise
    
    > cd issue-management-servise
    
    > ./sbt.sh

In sbt shell: (> test or > run)

Warning:
  Be sure that MongoDB is installed!
  MongoDB installation guide: http://www.mongodb.org/display/DOCS/Quickstart
  MondoDB downlods: http://www.mongodb.org/downloads