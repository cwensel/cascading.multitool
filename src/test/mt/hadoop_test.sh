# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "hadoop.inc"

before () {
  module_depends hadoop
}

it_detects_hadoop_if_HADOOP_HOME_is_set () {
  TMPDIR=`mktemp -dt mt_hadoop-spec.XXXXXX`
  mkdir -p $TMPDIR/bin/
  touch $TMPDIR/bin/hadoop
  PATH=/usr/bin/:/bin:/usr/sbin:/sbin
  HADOOP_HOME=$TMPDIR
  tested=true

  module_exit () {
    tested=
  }

  mt_hadoop
  rm -rf $TMPDIR

  test "$tested" = "true"
}

it_exits_if_hadoop_is_not_in_HADOOP_HOME () {
  HADOOP_HOME=/var
  PATH=/usr/bin/:/bin:/usr/sbin:/sbin

  module_exit () {
    [ "$*" = "HADOOP_HOME is set, but $HADOOP_HOME/bin/hadoop was not found." ] && tested=true
  }

  mt_hadoop
  test "$tested" = "true"
}

it_exits_if_HADOOP_HOME_is_not_set () {
  unset HADOOP_HOME
  PATH=/usr/bin/:/bin:/usr/sbin:/sbin

  module_exit () {
    [ "$*" = "HADOOP_HOME was not set and hadoop is not in your PATH" ] && tested=true
  }

  mt_hadoop
  test "$tested" = "true"
}
