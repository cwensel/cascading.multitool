# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "parameters.inc"

before () {
  module_depends parameters
}

it_prints_an_about_message () {
  about=`module_annotate parameters about`
  test "$about" = "prints a description of a Cascading.Multitool parameters"
}
