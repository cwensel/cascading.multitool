# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "_route.inc"

before () {
  module_depends _route
  route_match "^testing$" testing
}

it_should_create_a_matcher () {
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
  testing () {
    tested=true
  }
  route_perform testing
  test "$tested" = "true"
}

it_should_route_a_string_with_arguments () {
  ROUTE_testing="^testing"
  tested=false
  testing () {
    [ "$1" = "foo" ] && tested=true
  }
  route_perform testing foo
  test "$tested" = "true"
}

it_should_route_the_default_matcher () {
  tested=false
  defaulted() {
    tested=true
  }
  route_default defaulted
  route_perform foo
  test "$tested" = "true"
}

it_should_route_the_default_matcher_with_arguments () {
  tested=false
  defaulted() {
    [ "$1" = "bar" ] && tested=true
  }
  route_default defaulted
  route_perform foo bar
  test "$tested" = "true"
}
