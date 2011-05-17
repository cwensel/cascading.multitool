# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "_module.inc"

it_imports_a_module () {
  test "$MODULES" = ""
  module_depends _route
  test "$MODULES" = "_route "
}

it_does_not_import_a_module_twice () {
  test "$MODULES" = ""
  module_depends _route
  test "$MODULES" = "_route "
  module_depends _route
  test "$MODULES" = "_route "
}

it_quietly_ignores_invalid_modules () {
  OUTPUT=`module_depends _bad_`
  test "$OUTPUT" = ""
}

it_annotates_modules () {
  input="to be annotated"
  module_annotate _to_annotate_ longer_name "$input"
  test "$_MODULE_longer_name_to_annotate_" = "$input"

  OUTPUT=`module_annotate _to_annotate_ longer_name`
  test "$OUTPUT" = "$input"
}

it_annotates_modules_with_a_heredoc () {
  input="to be annotated"
  module_annotate_block _to_annotate_ longer_name <<USAGE
`echo $input`
USAGE

  test "$_MODULE_longer_name_to_annotate_" = "$input"
}
