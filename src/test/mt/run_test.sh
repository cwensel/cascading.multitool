# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "run.inc"

before () {
  include_dependencies log run
}

it_detects_hadoop_if_HADOOP_HOME_is_set () {
  HADOOP_HOME=/usr/local/lib/hadoop mt_run_detect_hadoop
}

it_exits_if_hadoop_is_not_in_HADOOP_HOME () {
  HADOOP_HOME=/var
  PATH=/usr/bin/:/bin:/usr/sbin:/sbin
  mt_run_avoid_exit=1
  OUTPUT=`mt_run_detect_hadoop`

  ERROR_MESSAGE="HADOOP_HOME is set, but /var/bin/hadoop was not found."
  ERROR_MESSAGE="${mt_log_red}ERROR$mt_log_reset_code $ERROR_MESSAGE$mt_log_reset_code"

  test "$OUTPUT" = "$ERROR_MESSAGE"
}

it_exits_if_HADOOP_HOME_is_not_set() {
  unset HADOOP_HOME
  PATH=/usr/bin/:/bin:/usr/sbin:/sbin
  mt_run_avoid_exit=1
  OUTPUT=`mt_run_detect_hadoop`

  ERROR_MESSAGE="HADOOP_HOME was not set and hadoop is not in your PATH"
  ERROR_MESSAGE="${mt_log_red}ERROR$mt_log_reset_code $ERROR_MESSAGE$mt_log_reset_code"

  test "$OUTPUT" = "$ERROR_MESSAGE"
}


it_displays_the_version_and_jar_information () {
  mt_jar_path=/mt_jar_path
  HADOOP_BIN='echo 1\n2\n3\n4'
  OUTPUT=`mt_run_show_version`

  EXPECTED=`echo Cascading.Multitool: $mt_jar_path\\\n3\\\n4 jar $mt_jar_path`

  test "$OUTPUT" = "$EXPECTED"
}

it_displays_usage_and_jar_information() {
  mt_jar_path=/mt_jar_path
  HADOOP_BIN='echo 1\n2\n3\n4'
  OUTPUT=`mt_run_show_usage`

  EXPECTED=`echo $mt_run_usage_doc\\\n3\\\n4 jar $mt_jar_path`
  
  test "$OUTPUT" = "$EXPECTED"
}

it_exits_if_multitool_jar_is_not_found () {
  mt_jar_path=""
  MT_PATH=/some_mt_path
  mt_run_avoid_exit=1

  OUTPUT=`mt_run`

  ERROR_MESSAGE="Could not find a multitool jar file in /some_mt_path"
  ERROR_MESSAGE="${mt_log_red}ERROR$mt_log_reset_code $ERROR_MESSAGE$mt_log_reset_code"

  test "$OUTPUT" = "$ERROR_MESSAGE"
}

it_exits_if_no_arguments_are_specified () {
  mt_jar_path=/some_jar_path
  mt_run_avoid_exit=1

  OUTPUT=`mt_run`

  ERROR_MESSAGE="No arguments specified"
  ERROR_MESSAGE="${mt_log_red}ERROR$mt_log_reset_code $ERROR_MESSAGE$mt_log_reset_code"

  test "$OUTPUT" = "$ERROR_MESSAGE"
}

it_runs_with_all_specified_arguments () {
  mt_jar_path=/some_jar_path
  mt_run_avoid_exit=1
  HADOOP_BIN="echo"

  OUTPUT=`mt_run test=true`

  test "$OUTPUT" = "jar /some_jar_path test=true"
}
