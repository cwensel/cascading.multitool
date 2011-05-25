# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "version.inc"

before () {
  module_exit () {
    module_exited=true
  }
  HADOOP_BIN=echo
  mt_jar_path=/
  module_depends _route version
}

it_routes () {
  mt_hadoop () {
    hadooped=true
  }
  mt_jar () {
    [ "$hadooped" = "true" ] && jarred=true
  }
  mt_version () {
    [ "$jarred" = "true" ] && tested=true
  }
  route_perform version
  test "$tested" = "true"
}

it_has_usage () {
  about=`module_annotate version about`
  test "$about" = "displays version information"
}
