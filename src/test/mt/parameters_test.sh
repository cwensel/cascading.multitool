# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "parameters.inc"

before () {
  TMP_HADOOP=`mktemp -d /tmp/mt-parameters-spec.XXXXXX`
  mkdir -p $TMP_HADOOP/bin/
  touch $TMP_HADOOP/bin/hadoop
}

after () {
  rm -rf $TMP_HADOOP
}

it_prints_an_about_message () {
  HADOOP_HOME=$TMP_HADOOP PATH=/usr/bin:/bin:/usr/sbin:/sbin module_depends parameters
  about=`module_annotate parameters about`
  test "$about" = "prints a description of a Cascading.Multitool parameters"
}
