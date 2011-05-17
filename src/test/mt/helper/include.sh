# Copyright (c) 2011 Concurrent, Inc.

if [ -z "$MT_PATH" ]
then
  MT_PATH=`dirname $0`/../../../..
  MT_PATH=`cd $MT_PATH && pwd`
fi

. $MT_PATH/bin/functions/_module.inc
