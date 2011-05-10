# Copyright (c) 2011 Concurrent, Inc.

if [[ "$mt_jar_path" == "" ]]; then
  mt_jar_path=`find $MT_PATH -name multitool*.jar | head -n1`
  
  if [[ "$mt_jar_path" == "" ]]; then
    echo "multitool.jar not found"
    
    if [[ -z $mt_jar_avoid_exit ]]; then
      exit 1
    fi
  fi
fi
