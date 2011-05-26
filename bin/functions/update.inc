# Copyright (c) 2011 Concurrent, Inc.

module_depends log

route_match "^update|update (-v|--version(=|\s))\d+\$" mt_update
route_before mt_update mt_update_reject_git mt_update_parse_latest mt_update_create_temp

module_annotate update about "update Cascading.Multitool"
module_annotate_block update usage <<USAGE
Update Cascading.Multitool from the source.

Usage:
  update [flags]

Flags:
  -v|--version - specify the version of Cascading.Multitool to fetch
USAGE

mt_update_base_url="http://files.cascading.org/multitool"

mt_update_create_temp ()
{
  mt_update_temp=`mktemp -dt cascading_multitool-mt_update.XXXXX`

  if [ ! -d "$temp_dir" ]
  then
    module_exit "Could not create temporary directory"
  fi

  mt_update_temp_tgz=$mt_update_temp/current.tgz
  mt_update_temp_new=$mt_update_temp/new
  mt_update_temp_old=$mt_update_temp/old
}

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
        mt_update_latest=$mt_update_base_url/multitool-`echo $1 | sed 's/--version=\(.*\)/\1/g'`.tgz
        ;;
      -v|--version)
        mt_update_latest=$mt_update_base_url/multitool-$2.tgz
        break 1
        ;;
      *)
        module_exit "Invalid option $1"
        ;;
    esac
    shift
  done

  if [ -z "$mt_update_latest" ]
  then
    info "INFO Parsing version number from $mt_update_base_url/multitool-current.txt"
    mt_update_latest=`mt_update_curl $mt_update_base_url/multitool-current.txt | grep "$mt_update_base_url.*\.tgz" | sed 's|^\([^ ]*://[^ ]*\).*|\1|g' | head -n1`
  fi

  if [ -z "$mt_update_latest" ]
  then
    module_exit "Cannot get latest multitool from $mt_update_base_url/multitool-current.txt"
  fi
}

mt_update ()
{
  info "INFO Fetching $mt_update_latest"
  mt_update_curl -o $mt_update_temp_tgz $mt_update_latest

  if [ $? -ne 0 ]
  then
    rm -rf $mt_update_temp
    module_exit "Could not fetch $mt_update_latest"
  fi

  mkdir -p $mt_update_temp_new

  info "INFO Extracting $mt_update_temp_tgz"
  tar zxf $mt_update_temp_tgz -C $mt_update_temp_new

  info "INFO Backing $MT_PATH up to $mt_update_temp_old"
  mv $MT_PATH $mt_update_temp_old

  for listing in `ls $mt_update_temp_new`
  do
    listing=`cd $mt_update_temp_new && pwd`/$listing
    if [ -d "$listing" ]
    then
      info "INFO Copying $listing to $MT_PATH"
      cp -Rf $listing $MT_PATH
      break 1
    fi
  done

  warn "WARN Remember to remove $mt_update_temp"
}
