# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "update.inc"

before () {
  CURL_BIN="echo"
  module_depends _route update
}

it_routes () {
  mt_update_reject_git () {
    tested=1
  }
  mt_update_parse_latest () {
    [ "$tested" = "1" ] && tested=2
  }
  mt_update () {
    [ "$tested" = "2" ] && tested=3
  }
  route_perform update
  test "$tested" = "3"
}

it_exits_if_a_git_repo_is_detected () {
  TMPDIR=`mktemp -d /tmp/mt_update-spec.XXXXXX`
  mkdir -p $TMPDIR/.git
  MT_PATH=$TMPDIR

  module_exit () {
    [ "$*" = "$MT_PATH is a git repository.  Use git pull to update." ] && tested=1
  }
  mt_update_parse_latest () {
    [ "$tested" = "1" ] && tested=2
  }
  mt_update () {
    [ "$tested" = "2" ] && tested=3
  }

  route_perform update
  
  rm -rf $TMPDIR
  
  test "$tested" = "3"
}

it_parses_the_multitool_location () {
  mt_update_reject_git () {
    MT_PATH=/does/not/exist
  }
  
  test "$mt_update_latest" != "latest"
  testing_url="http://files.cascading.org/multitool/multitool-latest.tgz"
  CURL_BIN="echo $testing_url"
  mt_update () {
    tested=true
  }
  route_perform update
  test "$mt_update_latest" = "$testing_url"
}

it_allows_a_version_specifier () {
  mt_update_reject_git () {
    MT_PATH=/does/not/exist
  }
  
  test "$mt_update_latest" != "latest"
  mt_update () {
    tested=true
  }
  
  route_perform update -v latest
  test "$mt_update_latest" = "http://files.cascading.org/multitool/multitool-latest.tgz"
  
  route_perform update --version=some_other
  test "$mt_update_latest" = "http://files.cascading.org/multitool/multitool-some_other.tgz"
}

it_unpacks_a_tarball_into_position () {
  mt_update_reject_git () {
    MT_PATH=/does/not/exist
  }
  mt_update_parse_latest () {
    mt_update_latest="http://files.cascading.org/multitool/multitool-some_version.tgz"
  }
  mt_update_curl () {
    TEMP_DIR=`dirname $2`
  }

  mkdir () {
    [ "$1" = "$TEMP_DIR/extracted" ] && mkdir_called=true
  }
  tar () {
    [ "$2" = "$TEMP_DIR/latest.tgz" ] && [ "$4" = "$TEMP_DIR/extracted" ] && tar_called=true
  }
  rm () {
    [ "$2" = "$MT_PATH" ] || [ "$2" = "$TEMP_DIR" ] && rm_called=true
  }
  cp () {
    [ "$2" = "$TEMP_DIR/extracted//" ] && [ "$3" = "$MT_PATH" ] && cp_called=true
  }

  mt_update

  test "$mkdir_called" = "true"
  test "$tar_called" = "true"
  test "$rm_called" = "true"
  test "$cp_called" = "true"
}
