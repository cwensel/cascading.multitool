# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "run.inc"

before () {
  color=always
  
  HADOOP_BIN=echo
  mt_jar_path=/
  module_depends _route run
}

it_routes () {
  tested=false
  mt_run () {
    tested=true
  }
  route_perform
  test "$tested" = "true"
}

it_exits_if_no_arguments_are_specified () {
  OUTPUT=`mt_run`

  test "$OUTPUT" != ""
}

it_runs_with_all_specified_arguments () {
  OUTPUT=`mt_run test=true`

  test "$OUTPUT" = "jar / test=true"
}
