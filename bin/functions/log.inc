# Copyright (c) 2011 Concurrent, Inc.

mt_log_use_color=1
mt_log_verbose=1

mt_log_stack_depth=0
mt_log_red="\033[0;31m"
mt_log_green="\033[0;32m"
mt_log_yellow="\033[0;33m"
mt_log_blue="\033[0;34m"
mt_log_reset_code="\033[0m"

mt_log_info_highlight_strings="cascade flow multitool"

function mt_log_reset () {
  if [[ $mt_log_use_color -ne 0 ]]; then
    echo -ne $mt_log_reset_code
  fi
}

function log () {
  if [[ $mt_log_stack_depth -gt 0 ]]; then
    echo -e `printf '\\\t%.0s' {1..$mt_log_stack_depth}`$*
  else
    echo -e $indent$@
  fi
}

function info () {
  mt_log_stack_depth=0
  if [[ $mt_log_verbose -ne 0 ]]; then
    if [[ $mt_log_use_color -ne 0 ]]; then
      for hilite in $mt_log_info_highlight_strings; do
        if echo $@ | grep "INFO $hilite" > /dev/null; then
          echo -e ${@/INFO/$mt_log_green'INFO'$mt_log_blue}$mt_log_reset_code
          return
        fi
      done

      echo -e ${@/INFO/$mt_log_green'INFO'$mt_log_reset_code}$mt_log_reset_code
    else
      echo $@
    fi
  fi
}

function warn () {
  mt_log_stack_depth=0
  if [[ $mt_log_use_color -ne 0 ]]; then
    echo -e ${@/WARN/$mt_log_yellow'WARN'$mt_log_reset_code}$mt_log_reset_code
  else
    echo $@
  fi
}

function error () {
  mt_log_stack_depth=0
  if [[ $mt_log_use_color -ne 0 ]]; then
    echo -e ${@/ERROR/$mt_log_red'ERROR'$mt_log_reset_code}$mt_log_reset_code
  else
    echo $@
  fi
}

function stacktrace () {
  log $@
  mt_log_stack_depth=$(($mt_log_stack_depth + 1))
}
