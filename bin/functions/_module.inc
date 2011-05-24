# Copyright (c) 2011 Concurrent, Inc.

if [ -z "$MODULES_ROOT" ]; then
  MODULES_ROOT=`cd $0 && pwd`
  MODULES_ROOT=`dirname $MODULES_ROOT`
else
  export MODULES_ROOT
fi

MODULES=

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
      fi
    fi
  done
}

module_annotate ()
{
  local module=$1
  local aspect=$2
  shift 2
  local message="$@"

  if [ -n "$module" ] && [ -n "$aspect" ]
  then
    if [ $# -gt 0 ]
    then
      eval _MODULE_$aspect$module=\"\$message\"
    else
      eval echo \"\$_MODULE_$aspect$module\"
    fi
  fi
}

module_annotate_block ()
{
  local module=$1
  local aspect=$2

  if [ -n "$module" ]
  then
    eval _MODULE_$aspect$module=\$\(cat\)
  fi
}
