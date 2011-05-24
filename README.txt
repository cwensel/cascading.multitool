Welcome

  This is the Cascading.Multitool (Multitool) application.

  Multitool provides a simple command line interface for building data processing jobs.

  Think of Multitool as 'grep' and 'sed' for Hadoop that also supports joins between multiple data-sets.

  For example, with "$HADOOP_HOME/bin/" in your PATH, the following command,

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

  This step is not necessary if you wish to run Multitool directly from the uncompressed distribution folder or
  Multitool was pre-installed with your Hadoop distribution. Type,

    > which multitool

  to see if it is already been added to your PATH.

  Installed for all users into "/usr/local/bin":

    > sudo ./bin/multitool install

  or for the current user only into "~/.multitool":

    > ./bin/multitool install

  For detailed instructions:

    > ./bin/multitool help install

  Choose the method that best suites your environment.

  If you are running Multitool on AWS Elastic MapReduce, you need to follow the Elastic MapReduce instructions
  on the AWS site, which typically expect the multitool-<release-date>.jar to be uploaded to AWS S3.


Using

  The environment variable HADOOP_HOME should always be set to use Multitool.

  To run from the command line with the jar, Hadoop should be in the path:

    > hadoop jar multitool-<release-date>.jar <args>

  If no args are given, a comprehensive list of commands will be printed.

  Or if Multitool has been installed from above:

    > multitool source=data/artist.100.txt cut=0 sink=output

  This will cut the first fields out of the file 'artists.100.txt' and save the results to 'output'.

  For a more complex example:

  > ./bin/multitool source=data/topic.100.txt cut=0 \
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

  To build Multitool, you must download the source code from GitHub:

   https://github.com/concurrentinc/cascading.multitool/tarball/master

  or clone the repo:

   https://github.com/concurrentinc/cascading.multitool

  This release will pull all dependencies from the relevant maven repos,
  including conjars.org.

  To build a jar,

  > ant retrieve jar

  To test,

  > ant test

License

  See LICENSE.txt
