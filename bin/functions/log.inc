# Copyright (c) 2011 Concurrent, Inc.

mt_log_use_color=true
mt_log_verbose=true
mt_log_stack_depth=0

if  [ "$color" = "always" ] || (test -t 1); then
  mt_log_red=$(printf "\033[31m")
  mt_log_green=$(printf "\033[32m")
  mt_log_yellow=$(printf "\033[33m")
  mt_log_blue=$(printf "\033[34m")
  mt_log_clear=$(printf "\033[m")
fi

mt_log_hilite_pattern="s/INFO/${mt_log_green}INFO${mt_log_blue}/"
mt_log_info_pattern="s/INFO/${mt_log_green}INFO${mt_log_clear}/"
mt_log_warn_pattern="s/WARN/${mt_log_yellow}WARN${mt_log_clear}/"
mt_log_error_pattern="s/ERROR/${mt_log_red}ERROR${mt_log_clear}/"
mt_log_info_highlight_strings="cascade flow multitool"

log ()
{
  [ "$mt_log_stack_depth" -gt 0 ] && printf '\t%.0s' {1..$mt_log_stack_depth}
  echo "$@"
}

info ()
{
  mt_log_stack_depth=0
  if [ -n "$mt_log_verbose" ]
  then
    if [ -n "$mt_log_use_color" ]
    then
      for hilite in $mt_log_info_highlight_strings
      do
        if echo "$@" | grep "INFO $hilite" > /dev/null
        then
          echo `echo "$@" | sed $mt_log_hilite_pattern`$mt_log_clear
          return
        fi
      done

      echo `echo "$@" | sed $mt_log_info_pattern`$mt_log_clear
    else
      echo "$@"
    fi
  fi
}

warn ()
{
  mt_log_stack_depth=0
  if [ -n "$mt_log_use_color" ]
  then
    echo `echo "$@" | sed $mt_log_warn_pattern`$mt_log_clear
  else
    echo "$@"
  fi
}

error ()
{
  mt_log_stack_depth=0
  if [ -n "$mt_log_use_color" ]
  then
    echo `echo "$@" | sed $mt_log_error_pattern`$mt_log_clear
  else
    echo "$@"
  fi
}

stacktrace ()
{
  log $@
  mt_log_stack_depth=$(($mt_log_stack_depth + 1))
}
