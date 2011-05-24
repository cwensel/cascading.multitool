# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "update.inc"

before () {
  CURL_BIN="echo"
  module_depends _route update
}

it_routes () {
  MT_PATH=/does/not/exist
  
  mt_update () {
    tested=true
  }
  route_perform update
  test "$tested" = "true"
}

it_exits_if_a_git_repo_is_detected () {
  TMPDIR=`mktemp -d /tmp/mt_update-spec.XXXXXX`
  mkdir -p $TMPDIR/.git
  
  OUTPUT=`route_perform update`
  
  rm -rf $TMPDIR
  test "$OUTPUT" = ""
}

it_parses_the_multitool_location () {
  MT_PATH=/does/not/exist
  
  test "$mt_update_latest" != "latest"
  CURL_BIN="echo 'http://files.cascading.org/multitool/multitool-latest.tgz'"
  mt_update () {
    tested=true
  }
  route_perform update
  test "$mt_update_latest" = "latest"
}

it_allows_a_version_specifier () {
  MT_PATH=/does/not/exist
  
  test "$mt_update_latest" != "latest"
  mt_update () {
    tested=true
  }
  
  route_perform update -v latest
  test "$mt_update_latest" = "latest" && test "$tested" = "true"

  tested=
  route_perform update --version=some_other
  test "$mt_update_latest" = "some_other" && test "$tested" = "true"
}

it_unpacks_a_tarball_into_position () {
  MT_PATH=/does/not/exist

  mt_update_parse_latest () {
    mt_update_latest="some_version"
  }
  mt_update_curl () {
    TEMP_DIR=`dirname $3`
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
