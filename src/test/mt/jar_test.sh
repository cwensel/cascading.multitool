# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "jar.inc"

before () {
  mt_jar_avoid_exit=1

  TMP_HADOOP=`mktemp -d /tmp/mt-hadoop.XXXXXX`
  mkdir -p $TMP_HADOOP/bin/
  touch $TMP_HADOOP/bin/hadoop

  HADOOP_HOME=$TMP_HADOOP PATH=/usr/bin:/bin:/usr/sbin:/sbin module_depends hadoop

  TMP_JAR=`mktemp -d /tmp/mt-jar.XXXXXX`
  touch $TMP_JAR/multitool-test.jar
}

after () {
  rm -rf $TMP_HADOOP $TMP_JAR
}

it_runs_silently_if_mt_jar_path_is_set () {
  module_depends jar
  mt_jar_path=/
  OUTPUT=`mt_jar`
  test "$OUTPUT" = ""
}

it_runs_silently_if_it_finds_multitool_jar () {
  HERE_PATH=`dirname $(cd ${0%/*}/../../../.. && echo $PWD/${0##*/})`
  MT_PATH=.

  OUTPUT=`cd $TMP_JAR && . $HERE_PATH/bin/functions/jar.inc && mt_jar`

  test "$OUTPUT" = ""
}

it_complains_if_it_cannot_find_multitool_jar () {
  rm $TMP_JAR/multitool-test.jar
  HERE_PATH=`dirname $(cd ${0%/*}/../../../.. && echo $PWD/${0##*/})`
  MT_PATH=.

  OUTPUT=`cd $TMPDIR && . $HERE_PATH/bin/functions/jar.inc && mt_jar`

  test "$OUTPUT" = "ERROR multitool.jar not found"
}
