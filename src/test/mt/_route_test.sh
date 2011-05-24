# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "_route.inc"

before () {
  module_depends _route
  route_match "^testing$" do_testing
}

it_should_create_a_matcher () {
  test "$ROUTE_do_testing" = "^testing$"
}

it_should_fetch_a_matcher () {
  test `route_match do_testing` = "^testing$"
}

it_should_set_a_default_matcher () {
  route_default defaulted
  test "$ROUTE_DEFAULT" = "defaulted"
}

it_should_route_a_string () {
  tested=false
  do_testing () {
    tested=true
  }
  route_perform testing
  test "$tested" = "true"
}

it_should_route_a_string_with_arguments () {
  ROUTE_do_testing="^testing"
  tested=false
  do_testing () {
    [ "$1" = "foo" ] && [ "$2" = "bar" ] && [ "$*" = "foo bar" ] && tested=true
  }
  route_perform "testing" foo bar
  test "$tested" = "true"
}

it_should_route_the_default_matcher () {
  tested=false
  do_default () {
    tested=true
  }
  route_default do_default
  route_perform not_routed
  test "$tested" = "true"
}

it_should_route_the_default_with_arguments () {
  tested=false
  do_default () {
    [ "$*" = "foo bar" ] && tested=true
  }
  route_default do_default
  route_perform foo bar
  test "$tested" = "true"
}

it_should_run_before_filters_before_routing () {
  did_pre=false
  tested=true
  do_pre () {
    did_pre=true
  }
  do_testing () {
    [ "$did_pre" = "true" ] && tested=true
  }
  route_before do_testing do_pre
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
  do_testing () {
    [ "$did_pre" = "true" ] && [ "$did_pre2" = "true" ] && tested=true
  }
  route_before do_testing pre pre2
  route_perform testing
}
