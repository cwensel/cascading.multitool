# Copyright (c) 2011 Concurrent, Inc.

module_depends log

mt_hadoop()
{
  if [ -z "$HADOOP_BIN" ]
  then
    HADOOP_BIN=`which hadoop 2>/dev/null || echo`

    if [ -z "$HADOOP_BIN" ]
    then
      if [ -n "$HADOOP_HOME" ]
      then
        HADOOP_BIN=$HADOOP_HOME/bin/hadoop

        if [ ! -e "$HADOOP_BIN" ]
        then
          module_exit "HADOOP_HOME is set, but $HADOOP_BIN was not found."
        fi
      else
        module_exit "HADOOP_HOME was not set and hadoop is not in your PATH"
      fi
    fi
  fi
}
