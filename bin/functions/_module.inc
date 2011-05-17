# Copyright (c) 2011 Concurrent, Inc.

if [ -z "$MODULES_ROOT" ]; then
  MODULES_ROOT=`cd $0 && pwd`
  MODULES_ROOT=`dirname $MODULES_ROOT`
else
  export MODULES_ROOT
fi

MODULES=
MODULE_LONGEST_NAME=1

module_depends ()
{
  for dep in "$@"
  do
    for extant in $MODULES; do
      if [ "$dep" = "$extant" ]; then
        break 1
      fi
    done

    if [ "$dep" != "$extant" ]; then
      MODULES="$MODULES$dep "
      local module=$MODULES_ROOT/$dep.inc

      if [ -f $module ]; then
        . $module

        local length=`expr $dep : '.*'`

        if [ $length -gt $MODULE_LONGEST_NAME ]; then
          MODULE_LONGEST_NAME=$length
        fi
      fi
    fi
  done
}

module_annotate ()
{
  module=$1
  aspect=$2
  shift 2
  message=$@

  if [ -n "$module" ] && [ -n "$aspect" ]
  then
    if [ $# -gt 0 ]
    then
      eval _MODULE_$aspect$module=\$message
    else
      eval echo \"\$_MODULE_$aspect$module\"
    fi
  fi
}

module_annotate_block ()
{
  module=$1
  aspect=$2

  if [ -n "$module" ]
  then
    read -r -d '' description
    eval _MODULE_$aspect$module=\$description
  fi
}
