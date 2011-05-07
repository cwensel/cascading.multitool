. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "log.inc"

before () {
  include_dependencies log
  ECHO_OUTPUT=
  alias echo="ECHO_OUTPUT="
}

it_logs_a_message() {
  usage=$(grep 2>&1 | head -n 1)
  test "$usage" = "Usage: grep [OPTION]... PATTERN [FILE]..."
}

it_will_fail_this_test() {
  echo foo | grep -q bar
}