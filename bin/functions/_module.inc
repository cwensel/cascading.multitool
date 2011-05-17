# Copyright (c) 2011 Concurrent, Inc.

MT_MODULES=
MT_MODULES_LOADED=

mt_module_longest=1
mt_module_base_path=bin/functions

module_dependencies ()
{
  for dep in "$@"
  do
    for extant in $MT_MODULES; do
      if [ "$dep" = "$extant" ]; then
        break 1
      fi
    done

    if [ "$dep" != "$extant" ]; then
      MT_MODULES="$MT_MODULES $dep"
      local module=$MT_PATH/$mt_module_base_path/$dep.inc

      if [ -f $module ]; then
        . $module

        local length=`expr $dep : '.*'`

        if [ $length -gt $mt_module_longest ]; then
          mt_module_longest=$length
        fi
      fi
    fi
  done
}

module_about ()
{
  module=$1
  if [ -n "$module" ]
  then
    if [ $# -gt 1 ]
    then
      shift
      message=$@
      eval mt_about_$module=\$message
    else
      eval echo \"\$mt_about_$module\"
    fi
  fi
}

module_describe_block ()
{
  module=$1
  if [ -n "$module" ]
  then
    read -r -d '' description
    eval mt_describe_$module=\$description
  fi
}

module_describe ()
{
  module=$1
  if [ -n "$module" ]
  then
    if [ $# -gt 1 ]
    then
      shift
      message=$@
      eval mt_describe_$module=\$message
    else
      eval echo \"\$mt_describe_$module\"
    fi
  fi
}

module_list ()
{
  for module in `echo $MT_MODULES | tr " " "\n" | sort | tr "\n" " "`; do
    about=`module_about $module`
    if [ -n "$about" ]; then
      echo "  $module - $about"
    fi
  done
}
