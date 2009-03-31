Welcome

 This is the Cascading.Multitool module.

 It provides a simple command line interface for building data processing jobs.

 For example the following command:

   > hadoop jar multitool.jar source=input.txt select=Monday sink=outputDir

 will start a Hadoop job to read in the source file "input.txt", grep all lines with
 the word "Monday" and output the results into the sink "outputDir".

 Multitool will inherit the underlying Hadoop configuration, so if the default FileSystem
 is HDFS, all paths will be relative to the cluster filesystem, not local. Using fully
 qualified urls will override the defaults (file://some/path or s3n:/bucket/file).

 This application is built with Cascading.

 Cascading is a feature rich API for defining and executing complex,
 scale-free, and fault tolerant data processing workflows on a Hadoop
 cluster. It can be found at the following location:

   http://www.cascading.org/


Building

 This release requires at least Cascading 1.0.7 built for your version
 of Hadoop. See the project site for downloads.

 To build a jar,

 > ant -Dcascading.home=... -Dhadoop.home=... jar

 To test,

 > ant -Dcascading.home=... -Dhadoop.home=... test

 where "..." is the install path of each of the dependencies.

 Optionally, a build.properties file can be created in the project root
 that defines the *.home properties above.

Using

  To run from the command line, Hadoop should be in the path:

  > hadoop jar multitool.jar <args>

  If no args are given, a comprehensive list of commands will be printed.

Examples

  copying:
    args = source=input.txt sink=outputDir

  copying while removing the first header line, and overwriting output:
    args = source=input.txt source.skipheader=true sink=outputDir sink.replace=true

  filter out data:
    args = source=input.txt "reject=some words" sink=outputDir


License

  Copyright (c) 2009 Concurrent, Inc.

  This work has been released into the public domain
  by the copyright holder. This applies worldwide.

  In case this is not legally possible:
  The copyright holder grants any entity the right
  to use this work for any purpose, without any
  conditions, unless such conditions are required by law.