# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "run.inc"

before () {
  color=always
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

it_has_usage () {
  about=`module_annotate run about`
  test "$about" = "run Cascading.Multitool"
}

it_exits_if_no_arguments_are_specified () {
  mt_jar_path=/some_jar_path
  mt_run_avoid_exit=1

  OUTPUT=`route_perform`

  ERROR_MESSAGE="No arguments specified"
  ERROR_MESSAGE="${mt_log_red}ERROR$mt_log_clear $ERROR_MESSAGE$mt_log_clear"

  test "$OUTPUT" = "$ERROR_MESSAGE"
}

it_runs_with_all_specified_arguments () {
  mt_jar_path=/some_jar_path
  mt_run_avoid_exit=1
  HADOOP_BIN="echo"

  OUTPUT=`mt_run test=true`

  test "$OUTPUT" = "jar /some_jar_path test=true"
}
