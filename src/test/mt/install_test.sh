# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "install.inc"

it_displays_installation_usage () {
  alias read="cat" module_depends install

  test "$OUTPUT" = "" # uninteresting
}

it_sets_the_install_destination () {
  test "$mt_install_destination" = "$HOME/.multitool"
}