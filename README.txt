Welcome

 This is the Cascading.Multitool (Multitool) module.

 It provides a simple command line interface for building data processing jobs.

 Think of Multitool as 'grep' and 'sed' for Hadoop, that also supports joins.

 For example, with "$HADOOP_HOME/bin/hadoop" in your PATH, the following command,

   > hadoop jar multitool-<release-date>.jar source=input.txt select=Monday sink=outputDir

 will start a Hadoop job to read in the source file "input.txt", grep all lines with
 the word "Monday" and output the results into the directory "outputDir".

 Multitool will inherit the underlying Hadoop configuration, so if the default FileSystem
 is HDFS, all paths will be relative to the cluster filesystem, not local. Using fully
 qualified urls will override the defaults (file://some/path or s3n:/bucket/file).

 This application is built with Cascading.

 Cascading is a feature rich API for defining and executing complex,
 scale-free, and fault tolerant data processing workflows on a Hadoop
 cluster. It can be found at the following location:

   http://www.cascading.org/

Installing

  Multitool may be used three ways after the distribution file as been uncompressed:

  Installed for all users:

  > ./bin/install-mt

  or for detailed instructions:

  > ./bin/install-mt --help

  the install process will install the "bin/mt" script into "/usr/local/bin" allowing:

  > mt [args]

  to execute "bin/hadoop" with the proper multitool.jar file.

  Run from the uncompressed directory:

  > ./bin/mt [args]

  Or as shown above, directly with "bin/hadoop":

  > hadoop jar multitool-<release-date>.jar [args]

  Choose the installation method that best suites your environment.

Using

  To run from the command line, Hadoop should be in the path:

  > hadoop jar multitool.jar <args>

  If no args are given, a comprehensive list of commands will be printed.

  For example (see above for configuring environment):

  > ant jar
  > hadoop jar build/multitool.jar source=data/artist.100.txt cut=0 sink=output

  This will compile a new jar, and cut the first fields out of the file 'artists.100.txt'
  and save the results to 'output'.

  For a more complex example:

  > ./bin/mt data/topic.100.txt cut=0 \
    "pgen=(\b[12][09][0-9]{2}\b)" group=0 count=0 group=1 \
    sink=output sink.replace=true sink.parts=1

  This will find all years in the input file, count them, and sort them by counts.

Examples

  copying:
    args = source=input.txt sink=outputDir

  copying while removing the first header line, and overwriting output:
    args = source=input.txt source.skipheader=true sink=outputDir sink.replace=true

  filter out data:
    args = source=input.txt "reject=some words" sink=outputDir

Building

 This release requires at least Cascading 1.2.x and will pull all dependencies from
 the relevant maven repos, including conjars.org.

 To build a jar,

 > ant retrieve jar

 To test,

 > ant test

License

  See LICENSE.txt
