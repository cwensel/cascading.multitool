# Copyright (c) 2011 Concurrent, Inc.

module_depends log
module_annotate help about "display this screen"
module_annotate_block help usage <<-USAGE
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
  echo "$mt_help_banner"

  if [ $# -eq 0 ]; then
    module_annotate help usage | tail -n +2
    mt_help_module_list
  else
    while [ $# -gt 0 ]; do
      module=`module_annotate $1 usage`

      if [ -n "$module" ]; then
        echo ""
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
