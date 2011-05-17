# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "doc.inc"

before () {
  mt_hadoop_avoid_exit=1
  mt_jar_avoid_exit=1
  HADOOP_BIN=echo
  mt_jar_path=/
  module_depends _route doc
}

it_prints_an_about_message () {
  about=`module_annotate doc about`
  test "$about" = "describes a Cascading.Multitool command"
}
