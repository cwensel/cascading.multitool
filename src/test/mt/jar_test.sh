# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "jar.inc"

before () {
  TMP_HADOOP=`mktemp -d /tmp/mt-jar-hadoop-spec.XXXXXX`
  mkdir -p $TMP_HADOOP/bin/
  touch $TMP_HADOOP/bin/hadoop

  module_depends hadoop
}

after () {
  rm -rf $TMP_HADOOP
}

it_runs_silently_if_mt_jar_path_is_set () {
  mt_jar_path=/
  OUTPUT=`PATH=$PATH:$TMP_HADOOP/bin module_depends jar`
  test "$OUTPUT" = ""
}

it_runs_silently_if_it_finds_multitool_jar () {
  TMPDIR=`mktemp -d /tmp/mt-jar-spec.XXXXXX`
  touch $TMPDIR/multitool-test.jar
  HERE_PATH=`dirname $(cd ${0%/*}/../../../.. && echo $PWD/${0##*/})`
  MT_PATH=.

  OUTPUT=`cd $TMPDIR && PATH=$PATH:$TMP_HADOOP/bin . $HERE_PATH/bin/functions/jar.inc`

  rm -rf $TMPDIR
  test "$OUTPUT" = ""
}

it_complains_if_it_cannot_find_multitool_jar () {
  TMPDIR=`mktemp -d /tmp/mt-jar-spec.XXXXXX`
  mt_jar_avoid_exit=1
  HERE_PATH=`dirname $(cd ${0%/*}/../../../.. && echo $PWD/${0##*/})`
  MT_PATH=.

  OUTPUT=`cd $TMPDIR && PATH=$PATH:$TMP_HADOOP/bin . $HERE_PATH/bin/functions/jar.inc`

  rm -rf $TMPDIR
  test "$OUTPUT" = "ERROR multitool.jar not found"
}
