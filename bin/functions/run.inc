# Copyright (c) 2011 Concurrent, Inc.

function mt_run_detect_hadoop () {
  HADOOP_BIN=$(which hadoop || echo)

  if [[ -z "$HADOOP_BIN" ]]; then
    if [[ -n $HADOOP_HOME ]]; then
      HADOOP_BIN="$HADOOP_HOME/bin/hadoop"

      if [[ ! -e $HADOOP_BIN ]]; then
        error "ERROR HADOOP_HOME is set, but $HADOOP_BIN was not found."
        if [[ -z "$mt_run_avoid_exit_call" ]]; then
          exit 1
        fi
      fi
    else
      error "ERROR HADOOP_HOME was not set and hadoop is not in your PATH"
      if [[ -z "$mt_run_avoid_exit_call" ]]; then
        exit 1
      fi
    fi
  fi
}

function mt_run_show_version () {
  echo "Cascading.Multitool: $mt_jar_path"
  $HADOOP_BIN jar $mt_jar_path [args] | tail -n +3 | head -n2
}

function mt_run_show_usage () {
  echo $mt_run_usage_doc
  $HADOOP_BIN jar $mt_jar_path [args] | tail -n +3
}

function mt_run () {
  if [[ "$mt_jar_path" == "" ]]; then
    error "ERROR Could not find a multitool jar file in '$MT_PATH'"
    exit 1
  fi

  if [[ $# -eq 0 ]]; then
    error "ERROR No arguments specified"
    exit 1
  fi

  $HADOOP_BIN jar $mt_jar_path $@ 2>&1 | while read line; do
    if echo $line | grep INFO > /dev/null; then
      info $line
    elif echo $line | grep WARN > /dev/null; then
      warn $line
    elif echo $line | grep ERROR > /dev/null; then
      error $line
    elif echo $line | grep Exception > /dev/null; then
      stacktrace $line
    elif [[ ! -z $mt_log_verbose ]]; then
      log $line
    fi
  done
}

mt_run_usage_doc=$(cat <<-USAGE
mt is the wrapper around Cascading.Multitool.
Copyright (c) 2011 Concurrent, Inc. (http://concurrentinc.com)

Usage:
  mt [flags] [options] action=argument [action=argument [...]]

Flags:
  -h|--help       - displays this message
  -v|--version    - displays the version of the Cascading.Multitool jar
  -q|--quiet      - suppresses INFO messages from Cascading and Hadoop
  -nc|--no-color  - suppress colorization
USAGE)
