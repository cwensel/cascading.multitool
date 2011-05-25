# Copyright (c) 2011 Concurrent, Inc.

module_depends log

route_match "^update|update (-v|--version(=|\s))\d+\$" mt_update
route_before mt_update mt_update_reject_git mt_update_parse_latest

module_annotate update about "update Cascading.Multitool"
module_annotate_block update usage <<USAGE
Update Cascading.Multitool from the source.

Usage:
  update [flags]

Flags:
  -v|--version - specify the version of Cascading.Multitool to fetch
USAGE

mt_update_latest=
mt_update_base="http://files.cascading.org/multitool"
mt_update_latest_location="http://www.cascading.org/downloads.html"
mt_update_latest_extractor='s/.*multitool-\([^\.]*\)\.tgz.*/\1/g'

mt_update_curl ()
{
  if [ -z "$CURL_BIN" ]
  then
    CURL_BIN=`which curl 2>/dev/null || echo`

    if [ -z "$CURL_BIN" ]
    then
      module_exit "curl was not found in PATH"
    fi
  fi
  
  if [ -n "$mt_log_verbose" ]
  then
    $CURL_BIN --progress-bar $@
  else
    $CURL_BIN -s $@
  fi
}

mt_update_reject_git ()
{
  if [ -d "$MT_PATH/.git" ]
  then
    module_exit "$MT_PATH is a git repository.  Use git pull to update."
  fi
}

mt_update_parse_latest ()
{
  while [ $# -gt 0 ]
  do
    case "$1" in
      --version=*)
        mt_update_latest=`echo $1 | sed 's/--version=\(.*\)/\1/g'`
        ;;
      -v|--version)
        mt_update_latest=$2
        break 1
        ;;
      *)
        module_exit "Invalid option $1"
        ;;
    esac
    shift
  done
  
  for location in $mt_update_base/multitool-current.txt $mt_update_latest_location
  do
    if [ -n "$mt_update_latest" ]
    then
      break 1
    fi
    info "INFO Parsing version number from $location"
    mt_update_latest=`mt_update_curl $location | grep "$mt_update_base.*\.tgz" | sed $mt_update_latest_extractor`
  done
  
  if [ -z "$mt_update_latest" ]
  then
    module_exit "Cannot get latest multitool."
  fi
}

mt_update ()
{
  TMPDIR=`mktemp -d /tmp/cascading_multitool-mt_update.XXXXXX`

  info "INFO Fetching $mt_update_base/multitool-$mt_update_latest.tgz"
  mt_update_curl -o $TMPDIR/latest.tgz $mt_update_base/multitool-$mt_update_latest.tgz

  if [ $? -ne 0 ]
  then
    rm -rf $TMPDIR
    module_exit "Could not fetch $mt_update_base/multitool-$mt_update_latest.tgz"
  fi

  info "INFO Extracting $TMPDIR/latest.tgz"
  mkdir $TMPDIR/extracted
  tar zxf $TMPDIR/latest.tgz -C $TMPDIR/extracted

  info "INFO Installing to $MT_PATH"
  rm -rf $MT_PATH
  cp -Rf $TMPDIR/extracted/`ls $TMPDIR/extracted`/ $MT_PATH

  info "INFO Cleaning up $TMPDIR"
  rm -rf $TMPDIR
}
