# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "jar.inc"

before () {
  TMP_HADOOP=`mktemp -dt mt_jar-hadoop-spec.XXXXXX`
  mkdir -p $TMP_HADOOP/bin/
  touch $TMP_HADOOP/bin/hadoop
  HADOOP_HOME=$TMP_HADOOP

  TMP_JAR=`mktemp -dt mt_jar-spec.XXXXXX`
  touch $TMP_JAR/multitool-test.jar

  module_depends jar
}

after () {
  rm -rf $TMP_HADOOP $TMP_JAR
}

it_runs_silently_if_mt_jar_path_is_set () {
  mt_jar_path=/
  tested=true

  module_exit () {
    tested=
  }

  mt_jar
  test "$tested" = "true"
}

it_runs_silently_if_it_finds_multitool_jar () {
  MT_PATH=$TMP_JAR
  tested=true

  module_exit () {
    tested=
  }
  
  mt_jar
  test "$tested" = "true"
  test "$mt_jar_path" = "$TMP_JAR/multitool-test.jar"
}

it_complains_if_it_cannot_find_multitool_jar () {
  rm $TMP_JAR/multitool-test.jar
  MT_PATH=$TMP_JAR

  module_exit () {
    [ "$*" = "multitool.jar not found" ] && tested=true
  }
  
  mt_jar
  test "$tested" = "true"
}
