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

it_should_route_the_default_with_arguments () {
  tested=false
  defaulted () {
    [ "$1" = "foo" ] && [ "$2" = "bar" ] && tested=true
  }
  route_default defaulted
  route_perform foo bar
  test "$tested" = "true"
}

it_should_run_before_filters_before_routing () {
  did_pre=false
  tested=true
  pre () {
    did_pre=true
  }
  testing () {
    [ "$did_pre" = "true" ] && tested=true
  }
  route_before testing pre
  route_perform testing
}

it_should_run_multiple_before_filters () {
  did_pre=false
  did_pre2=false
  tested=true
  pre () {
    did_pre=true
  }
  pre2 () {
    did_pre2=true
  }
  testing () {
    [ "$did_pre" = "true" ] && [ "$did_pre2" = "true" ] && tested=true
  }
  route_before testing pre pre2
  route_perform testing
}
