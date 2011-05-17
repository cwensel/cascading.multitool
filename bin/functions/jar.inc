# Copyright (c) 2011 Concurrent, Inc.

module_dependencies log hadoop

if [ -z "$mt_jar_path" ]; then
  mt_jar_path=`find $MT_PATH -name multitool*.jar | head -n1`
  
  if [ -z "$mt_jar_path" ]; then
    error "ERROR multitool.jar not found"
    
    [ -z $mt_jar_avoid_exit ] && exit 1
  fi
fi

mt_jar_usage ()
{
  [ -z $mt_jar_usage_ ] && mt_jar_usage_=`$HADOOP_BIN jar $mt_jar_path | tail -n +3`
  echo "$mt_jar_usage_"
}
