# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "hadoop.inc"

it_detects_hadoop_if_HADOOP_HOME_is_set () {
  TMPDIR=`mktemp -d /tmp/mt-jar-spec.XXXXXX`
  mkdir -p $TMPDIR/bin/
  touch $TMPDIR/bin/hadoop

  OUTPUT=`PATH=/usr/bin/:/bin:/usr/sbin:/sbin HADOOP_HOME=$TMPDIR include_dependencies hadoop`

  ERROR_MESSAGE="HADOOP_HOME is set, but $TMPDIR/bin/hadoop was not found."
  ERROR_MESSAGE="${mt_log_red}ERROR$mt_log_clear $ERROR_MESSAGE$mt_log_clear"

  rm -rf $TMPDIR

  test "$OUTPUT" != "$ERROR_MESSAGE"
}

it_exits_if_hadoop_is_not_in_HADOOP_HOME () {
  HADOOP_HOME=/var
  PATH=/usr/bin/:/bin:/usr/sbin:/sbin
  mt_run_avoid_exit=1
  OUTPUT=`include_dependencies hadoop`

  ERROR_MESSAGE="HADOOP_HOME is set, but /var/bin/hadoop was not found."
  ERROR_MESSAGE="${mt_log_red}ERROR$mt_log_clear $ERROR_MESSAGE$mt_log_clear"

  test "$OUTPUT" = "$ERROR_MESSAGE"
}

it_exits_if_HADOOP_HOME_is_not_set() {
  unset HADOOP_HOME
  PATH=/usr/bin/:/bin:/usr/sbin:/sbin
  mt_run_avoid_exit=1
  OUTPUT=`include_dependencies hadoop`

  ERROR_MESSAGE="HADOOP_HOME was not set and hadoop is not in your PATH"
  ERROR_MESSAGE="${mt_log_red}ERROR$mt_log_clear $ERROR_MESSAGE$mt_log_clear"

  test "$OUTPUT" = "$ERROR_MESSAGE"
}
