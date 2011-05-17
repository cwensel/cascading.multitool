# Copyright (c) 2011 Concurrent, Inc.

module_depends log

route_match "^(help|-h|--help)" mt_help

module_annotate help about "display this screen"
module_annotate_block help usage <<-USAGE
Prints usage information relating to Cascading.Multitool.

Usage:
  help [topic]

Topics:
USAGE

module_annotate_block help banner <<-USAGE
`basename $0` is a wrapper around Cascading.Multitool.
Copyright (c) `date +%Y` Concurrent, Inc. (http://concurrentinc.com)
USAGE

mt_help_module_list ()
{
  for module in `echo $MODULES | tr " " "\n" | sort | tr "\n" " "`; do
    about=`module_annotate $module about`
    if [ -n "$about" ]; then
      echo "  $module - $about"
    fi
  done
}

mt_help ()
{
  module_annotate help banner
  echo

  if [ $# -eq 0 ]; then
    module_annotate help usage | tail -n +3
    mt_help_module_list
  else
    while [ $# -gt 0 ]; do
      module=`module_annotate $1 usage`

      if [ -n "$module" ]; then
        echo "$1: $module"

        if [ "$1" = "help" ]; then
          mt_help_module_list
        fi
      else
        error "ERROR $1 is not a module"
      fi
      shift
    done
  fi
}
