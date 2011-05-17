# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "help.inc"

before () {
  module_depends _route help
}

it_routes () {
  tested=false
  mt_help () {
    tested=true
  }
  route_perform help
  test "$tested" = "true"
}

it_has_usage () {
  about=`module_annotate help about`
  test "$about" = "display this screen"
}

it_formats_the_module_list () {
  _MODULE_abouttest=testing
  OUTPUT=`MODULES=test mt_help_module_list`
  test "$OUTPUT" = "  test - testing"
}
