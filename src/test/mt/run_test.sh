# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "run.inc"

before () {
  color=always
  include_dependencies log hadoop run
}

it_displays_the_version_and_jar_information () {
  mt_jar_path=/mt_jar_path
  HADOOP_BIN="echo 1\n\2\n3\n4"
  OUTPUT=`mt_run_show_version`

  echo "$OUTPUT" | grep Multitool
}

it_displays_usage_and_jar_information() {
  mt_jar_path=/mt_jar_path
  HADOOP_BIN="echo 1\n\2\n3\n4"
  OUTPUT=`mt_run_show_usage`

  echo "$OUTPUT" | grep Usage
}

it_exits_if_multitool_jar_is_not_found () {
  mt_jar_path=""
  MT_PATH=/some_mt_path
  mt_run_avoid_exit=1

  OUTPUT=`mt_run`

  ERROR_MESSAGE="Could not find a multitool jar file in /some_mt_path"
  ERROR_MESSAGE="${mt_log_red}ERROR$mt_log_clear $ERROR_MESSAGE$mt_log_clear"

  test "$OUTPUT" = "$ERROR_MESSAGE"
}

it_exits_if_no_arguments_are_specified () {
  mt_jar_path=/some_jar_path
  mt_run_avoid_exit=1

  OUTPUT=`mt_run`

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
