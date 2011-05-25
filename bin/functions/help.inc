# Copyright (c) 2011 Concurrent, Inc.

module_depends log

route_match '^-h|--help|(help(\s\w+)?)' mt_help

module_annotate help about "display this screen"
module_annotate_block help usage <<-USAGE
Runs the Cascading.Multitool jar with the provided parameters.

Usage:
  `basename $0` [flags] action [action-flags]
  `basename $0` [flags] command=argument [command=argument [...]]
  `basename $0` help [action]

Flags:
  -h|--help      - displays this message
  -q|--quiet     - silences INFO messages
  -nc|--no-color - disables colorization of messages

Commands:
  See \`help doc\` for more information.

Actions:
USAGE

module_annotate_block help banner <<-USAGE
`basename $0` is a wrapper around Cascading.Multitool.
Copyright (c) `date +%Y` Concurrent, Inc. (http://concurrentinc.com)
USAGE

mt_help_module_list ()
{
  local longest=0
  for module in $MODULES; do
    local length=`expr $module : '.*'`
    
    if [ $length -gt $longest ]; then
      longest=$length
    fi
  done
  
  for module in `echo $MODULES | tr " " "\n" | sort | tr "\n" " "`; do
    if [ "$module" != "run" ]; then
      about=`module_annotate $module about`
      if [ -n "$about" ]; then
        printf "  %-${longest}s - " "$module"
        echo "$about"
      fi
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

      if [ -n "$module" ] && [ "$1" != "run" ]; then
        echo "$1: $module"

        if [ "$1" = "help" ]; then
          mt_help_module_list
        fi
      else
        echo "Valid Topics:"
        mt_help_module_list
        echo
        module_exit "Invalid help topic: $1"
      fi
      shift
    done
  fi
}
