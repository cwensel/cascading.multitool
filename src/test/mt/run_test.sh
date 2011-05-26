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
  mt_run () {
    tested=true
  }
  route_perform
  test "$tested" = "true"
}

it_exits_if_no_arguments_are_specified () {
  module_exit () {
    [ "$*" = "No arguments specified" ] && tested=true
  }
  route_perform
  test "$tested" = "true"
}

it_runs_with_all_specified_arguments () {
  do_run_stub () {
    test "$*" = "jar $mt_jar_path test=true" || exit 1
  }
  HADOOP_BIN=do_run_stub
  route_perform test=true
}
