# Copyright (c) 2011 Concurrent, Inc.

if [ -z "$MT_PATH" ]
then
  MT_PATH=`dirname $0`/../../../..
  MT_PATH=`cd $MT_PATH && pwd`
fi

include_dependencies () {
  if [ "$#" -gt "0" ]
  then
    for dep in "$@"
    do
      . $MT_PATH/bin/functions/$dep.inc
    done
  fi
}
