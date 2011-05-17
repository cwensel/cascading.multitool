# Copyright (c) 2011 Concurrent, Inc.

module_dependencies log

HADOOP_BIN=$(which hadoop || echo)

if [ -z "$HADOOP_BIN" ]; then
  if [ -n $HADOOP_HOME ]; then
    HADOOP_BIN="$HADOOP_HOME/bin/hadoop"

    if [ ! -e $HADOOP_BIN ]; then
      error "ERROR HADOOP_HOME is set, but $HADOOP_BIN was not found."
      if [ -z "$mt_run_avoid_exit" ]; then
        exit 1
      fi
    fi
  else
    error "ERROR HADOOP_HOME was not set and hadoop is not in your PATH"
    if [ -z "$mt_run_avoid_exit" ]; then
      exit 1
    fi
  fi
fi