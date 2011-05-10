. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "run.inc"

before () {
  include_dependencies log run
}

it_runs_normally_with_hadoop_home () {
  HADOOP_HOME=/usr/local/lib/hadoop mt_run_detect_hadoop
}

it_exits_if_hadoop_is_not_in_hadoop_home() {
  HADOOP_HOME=/var
  PATH=/usr/bin/:/bin:/usr/sbin:/sbin
  mt_run_avoid_exit_call=1
  OUTPUT=`mt_run_detect_hadoop`

  ERROR_MESSAGE="ERROR$mt_log_reset_code HADOOP_HOME is set, but /var/bin/hadoop was not found.$mt_log_reset_code"
  ERROR_MESSAGE=`echo -e $mt_log_red$ERROR_MESSAGE`

  test "$OUTPUT" = "$ERROR_MESSAGE"
}

it_exits_if_hadoop_home_is_not_set() {
  unset HADOOP_HOME
  PATH=/usr/bin/:/bin:/usr/sbin:/sbin
  mt_run_avoid_exit_call=1
  OUTPUT=`mt_run_detect_hadoop`

  ERROR_MESSAGE="ERROR$mt_log_reset_code HADOOP_HOME was not set and hadoop is not in your PATH$mt_log_reset_code"
  ERROR_MESSAGE=`echo -e $mt_log_red$ERROR_MESSAGE`

  test "$OUTPUT" = "$ERROR_MESSAGE"
}


it_displays_the_version_and_jar_information() {
  mt_jar_path=/mt_jar_path
  HADOOP_BIN='echo 1\n2\n3\n4'
  OUTPUT=`mt_run_show_version`
  EXPECTED=`echo Cascading.Multitool: $mt_jar_path\\\n3\\\n4 jar $mt_jar_path [args]`

  test "$OUTPUT" = "$EXPECTED"
}

it_displays_usage_and_jar_information() {
  mt_jar_path=/mt_jar_path
  HADOOP_BIN='echo 1\n2\n3\n4'
  OUTPUT=`mt_run_show_usage`
  
  
  EXPECTED=`echo $mt_run_usage_doc\\\n3\\\n4 jar $mt_jar_path [args]`
  
  test "$OUTPUT" = "$EXPECTED"
}


