# Copyright (c) 2011 Concurrent, Inc.

mt_log_use_color=1
mt_log_verbose=1

mt_log_stack_depth=0

if (test -t 1) || (test $color = always); then
  mt_log_red=$(printf "\033[31m")
  mt_log_green=$(printf "\033[32m")
  mt_log_yellow=$(printf "\033[33m")
  mt_log_blue=$(printf "\033[34m")
  mt_log_reset_code=$(printf "\033[m")
fi

mt_log_info_highlight_strings="cascade flow multitool"

function mt_log_reset () {
  if [[ $mt_log_use_color -ne 0 ]]; then
    printf $mt_log_reset_code
  fi
}

function log () {
  if [[ $mt_log_stack_depth -gt 0 ]]; then
    printf '\t%.0s' {1..$mt_log_stack_depth}
  fi
  echo $@
}

function info () {
  mt_log_stack_depth=0
  if [[ $mt_log_verbose -ne 0 ]]; then
    if [[ $mt_log_use_color -ne 0 ]]; then
      for hilite in $mt_log_info_highlight_strings; do
        if echo $@ | grep "INFO $hilite" > /dev/null; then
          local err=`echo $@ | sed s/INFO/${mt_log_green}INFO${mt_log_blue}/`
          echo $err
          mt_log_reset
          return
        fi
      done

      local err=`echo $@ | sed s/INFO/${mt_log_green}INFO${mt_log_reset}/`
      echo $err
      mt_log_reset
    else
      echo $@
    fi
  fi
}

function warn () {
  mt_log_stack_depth=0
  if [[ $mt_log_use_color -ne 0 ]]; then
    local err=`echo $@ | sed s/WARN/${mt_log_yellow}WARN${mt_log_reset_code}/`
    echo $err
    mt_log_reset
  else
    echo $@
  fi
}

function error () {
  mt_log_stack_depth=0
  if [[ $mt_log_use_color -ne 0 ]]; then
    local err=`echo $@ | sed s/ERROR/${mt_log_red}ERROR${mt_log_reset_code}/`
    echo $err
    mt_log_reset
  else
    echo $@
  fi
}

function stacktrace () {
  log $@
  mt_log_stack_depth=$(($mt_log_stack_depth + 1))
}
