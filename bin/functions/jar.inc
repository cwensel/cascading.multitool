# Copyright (c) 2011 Concurrent, Inc.

module_depends log hadoop

mt_jar ()
{
  if [ -z $mt_jar_path ]
  then
    mt_jar_path=`find $MT_PATH -name multitool*.jar | head -n1`

    if [ -z $mt_jar_path ]
    then
      module_exit "multitool.jar not found"
    fi
  fi
}

mt_jar_usage ()
{
  if [ -z $mt_jar_usage_ ]
  then
    mt_hadoop
    mt_jar
    mt_jar_usage_=`$HADOOP_BIN jar $mt_jar_path | tail -n +3`
  fi

  echo "$mt_jar_usage_"
}
