# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "update.inc"

before () {
  CURL_BIN="echo"
  module_depends _route update
}

it_routes () {
  tested=false
  mt_update () {
    tested=true
  }
  route_perform update
  test "$tested" = "true"
}

it_parses_the_multitool_location () {
  test "$mt_update_latest" != "latest"
  CURL_BIN="echo 'http://files.cascading.org/multitool/multitool-latest.tgz'"
  route_perform update
  test "$mt_update_latest" = "latest"
}

it_unpacks_a_tarball_into_position () {
  mt_update_parse_latest () {
    mt_update_latest="some_version"
  }
  mt_update_curl () {
    TEMP_DIR=`dirname $3`
    test `basename $3` = "latest.tgz"
  }
  route_perform update
  test "0" = "1"
}
