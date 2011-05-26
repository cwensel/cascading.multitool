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
  mt_update_create_temp () {
    [ "$tested" = "2" ] && tested=3
  }
  mt_update () {
    [ "$tested" = "3" ] && tested=4
  }
  
  route_perform update
  test "$tested" = "4"
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
  mt_update_create_temp () {
    [ "$tested" = "2" ] && tested=3
  }
  mt_update () {
    [ "$tested" = "3" ] && tested=4
  }

  route_perform update
  rm -rf $TMPDIR

  test "$tested" = "4"
}

it_parses_the_latest_multitool_location () {
  testing_url="http://files.cascading.org/multitool/multitool-latest.tgz"
  CURL_BIN="echo $testing_url"
  mt_update_reject_git () {
    MT_PATH=/does/not/exist
  }
  mt_update_create_temp () {
    tested=1
  }
  mt_update () {
    [ "$mt_update_latest" = "$testing_url" ] && [ "$tested" = "1" ] && tested=2
  }

  route_perform update
  test "$tested" = "2"
}

it_complains_if_curl_fails_to_fetch_latest () {
  CURL_BIN=echo
  mt_update_reject_git () {
    MT_PATH=/does/not/exist
  }
  module_exit () {
    [ "$*" = "Cannot get latest multitool from http://files.cascading.org/multitool/multitool-current.txt" ] && tested=1
  }
  mt_update_create_temp () {
    [ "$tested" = "1" ] && tested=2
  }
  mt_update () {
    [ "$tested" = "2" ] && tested=3
  }

  route_perform update
  test "$mt_update_latest" = "$testing_url" -a "$tested" = "3"
}

it_allows_a_version_specifier () {
  mt_update_reject_git () {
    MT_PATH=/does/not/exist
  }
  mt_update_create_temp () {
    temped=true
  }
  mt_update () {
    tested=true
  }

  route_perform update -v latest
  test "$mt_update_latest" = "http://files.cascading.org/multitool/multitool-latest.tgz"

  route_perform update --version more_latest
  test "$mt_update_latest" = "http://files.cascading.org/multitool/multitool-more_latest.tgz"

  route_perform update --version=super_latest
  test "$mt_update_latest" = "http://files.cascading.org/multitool/multitool-super_latest.tgz"
}

it_updates_an_existing_installation () {
  TMPDIR=`mktemp -d /tmp/mt_update-spec.XXXXXX`
  mkdir $TMPDIR/mt
  MT_PATH=$TMPDIR/mt
  touch $MT_PATH/foo

  mt_update_temp=$TMPDIR
  mt_update_temp_tgz=$mt_update_temp/current.tgz
  mt_update_temp_new=$mt_update_temp/new
  mt_update_temp_old=$mt_update_temp/old

  mt_update_curl () {
    touch $mt_update_temp_tgz
  }
  tar () {
    mkdir $4/stuff
    touch $4/stuff/bar
  }

  test -e "$MT_PATH/foo"
  test ! -e "$MT_PATH/bar"

  mt_update

  test ! -e "$MT_PATH/foo"
  test -e "$MT_PATH/bar"
  test -e "$mt_update_temp_old/foo"

  rm -rf $TMPDIR
}
