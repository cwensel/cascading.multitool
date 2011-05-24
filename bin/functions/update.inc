# Copyright (c) 2011 Concurrent, Inc.

module_depends log

route_match "^update$" mt_update
route_before mt_update mt_update_fetch_latest

module_annotate update about "update Cascading.Multitool"
module_annotate_block run usage <<USAGE
Update Cascading.Multitool from the source.

Usage:
  update [flags]

Flags:
  -v|--version - specify the version of Cascading.Multitool to fetch
USAGE

mt_update_latest="20110518"
mt_update_base="http://files.cascading.org/multitool/"
mt_update_latest_location="http://www.cascading.org/downloads.html"
mt_update_latest_extractor="s/.*multitool-\([0-9]*\)\.tgz.*/\1/g"

mt_update_fetch_latest ()
{
  curl_cmd=`which curl 2>/dev/null || echo`
  
  if [ -z "$curl_cmd" ]
  then
    error "ERROR curl was not found in PATH"
    exit 1
  fi
  
  mt_update_latest=`$curl_cmd -s $mt_update_latest_location | grep "$mt_update_base" | sed "'$mt_update_latest_extractor'"`
}

mt_update ()
{
  echo "Going to get $mt_upate_base/multitool-$mt_update_latest.tgz"
}