# Copyright (c) 2011 Concurrent, Inc.

module_dependencies log
module_about help "display this screen"
module_describe_block help <<-USAGE
Prints usage information relating to Cascading.Multitool.

Usage:
  help [topic]

Topics:
USAGE

mt_help_banner=$(cat <<ABOUT
mt is the wrapper around Cascading.Multitool.
Copyright (c) `date +%Y` Concurrent, Inc. (http://concurrentinc.com)
ABOUT
)

mt_help ()
{
  echo "$mt_help_banner"

  if [ $# -eq 0 ]; then
    module_describe help | tail -n +2
    module_list
  else
    while [ $# -gt 0 ]; do
      module=`module_describe $1`

      if [ -n "$module" ]; then
        echo ""
        echo "$1: $module"

        if [ "$1" = "help" ]; then
          module_list
        fi
      else
        error "ERROR $1 is not a module"
      fi
      shift
    done
  fi
}
