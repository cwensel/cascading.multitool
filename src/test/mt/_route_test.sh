# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "_route.inc"

before () {
  module_depends _route
  route_match "^testing$" do_testing
}

ensure_foo_bar () {
  [ $# -eq 2 ] || exit 1
  [ "$1" = "foo" ] || exit 1
  [ "$2" = "bar" ] || exit 1
}

it_creates_a_matcher () {
  test "$ROUTE_do_testing" = "^testing$"
}

it_fetches_a_matcher () {
  test `route_match do_testing` = "^testing$"
}

it_sets_a_default_matcher () {
  route_default defaulted
  test "$ROUTE_DEFAULT" = "defaulted"
}

it_routes () {
  do_testing () {
    tested=true
  }
  route_perform testing
  test "$tested" = "true"
}

it_routes_with_arguments () {
  ROUTE_do_testing="^testing"
  do_testing () {
    ensure_foo_bar $@ && tested=true
  }
  route_perform "testing" foo bar
  test "$tested" = "true"
}

it_defaults_to_a_function () {
  do_default () {
    tested=true
  }
  route_default do_default
  route_perform not_routed
  test "$tested" = "true"
}

it_defaults_with_arguments () {
  do_default () {
    ensure_foo_bar $@ && tested=true
  }
  route_default do_default
  route_perform foo bar
  test "$tested" = "true"
}

it_runs_before_functions_and_then_routes () {
  do_pre () {
    did_pre=true
  }
  do_testing () {
    [ "$did_pre" = "true" ] && tested=true
  }
  route_before do_testing do_pre
  route_perform testing
  test "$tested" = "true"
}

it_runs_befores_with_args () {
  ROUTE_do_testing="^testing"
  do_pre () {
    ensure_foo_bar $@ && did_pre=true
  }
  do_testing () {
    ensure_foo_bar $@ && [ "$did_pre" = "true" ] && tested=true
  }
  route_before do_testing do_pre
  route_perform testing foo bar
  test "$tested" = "true"
}

it_defaults_a_before_with_args () {
  do_pre () {
    ensure_foo_bar $@ && did_pre=true
  }
  do_default () {
    ensure_foo_bar $@ && [ "$did_pre" = "true" ] && tested=true
  }
  route_default do_default
  route_before do_default do_pre
  route_perform foo bar
  test "$tested" = "true"
}

it_runs_multiple_befores () {
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

it_defaults_multiple_befores () {
  pre () {
    did_pre=true
  }
  pre2 () {
    did_pre2=true
  }
  do_default () {
    [ "$did_pre" = "true" ] && [ "$did_pre2" = "true" ] && tested=true
  }
  route_default do_default
  route_before do_default pre pre2
  route_perform
}

it_runs_multiple_befores_with_args () {
  ROUTE_do_testing="^testing"
  pre () {
    ensure_foo_bar $@ && did_pre=true
  }
  pre2 () {
    ensure_foo_bar $@ && did_pre2=true
  }
  do_testing () {
    ensure_foo_bar $@ && [ "$did_pre" = "true" ] && [ "$did_pre2" = "true" ] && tested=true
  }
  route_before do_testing pre pre2
  route_perform testing foo bar
}

it_defaults_multiple_befores_with_args () {
  pre () {
    ensure_foo_bar $@ && did_pre=true
  }
  pre2 () {
    ensure_foo_bar $@ && did_pre2=true
  }
  do_default () {
    ensure_foo_bar $@ && [ "$did_pre" = "true" ] && [ "$did_pre2" = "true" ] && tested=true
  }
  route_default do_default
  route_before do_default pre pre2
  route_perform foo bar
}