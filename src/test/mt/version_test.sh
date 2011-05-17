# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "version.inc"

before () {
  module_depends _route version
}

it_routes () {
  tested=false
  mt_version () {
    tested=true
  }
  route_perform version
  test "$tested" = "true"
}

it_has_usage () {
  about=`module_annotate version about`
  test "$about" = "displays version information"
}
