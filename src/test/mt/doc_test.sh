# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "doc.inc"

before () {
  HADOOP_BIN=echo
  mt_jar_path=/
  module_depends _route doc
}

it_prints_an_about_message () {
  about=`module_annotate doc about`
  test "$about" = "describes a Cascading.Multitool command"
}
