# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "parameters.inc"

before () {
  mt_jar_avoid_exit=1
  HADOOP_BIN=echo
  mt_jar_path=/
  module_depends parameters
}

it_prints_an_about_message () {
  about=`module_annotate parameters about`
  test "$about" = "prints a description of a Cascading.Multitool parameters"
}
