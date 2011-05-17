# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "_route.inc"

before () {
  module_depends _route
  route_match "^testing$" testing
}

it_should_create_a_matcher () {
  echo `set`
  test "$ROUTE_testing" = "^testing$"
}

it_should_fetch_a_matcher () {
  test `route_match testing` = "^testing$"
}

it_should_set_a_default_matcher () {
  route_default defaulted
  test "$ROUTE_DEFAULT" = "defaulted"
}

it_should_route_a_string () {
  tested=false
  testing() {
    tested=true
  }
  route_perform testing
  test "$tested" = "true"
}
