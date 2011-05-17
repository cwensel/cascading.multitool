# Copyright (c) 2011 Concurrent, Inc.

. `dirname $(cd ${0%/*} && echo $PWD/${0##*/})`/include.sh

describe "log.inc"

before () {
  color=always
  module_depends log
}

it_logs_a_message() {
  LOG_INPUT='this has been logged'
  LOG_OUTPUT=`log $LOG_INPUT`
  test "$LOG_OUTPUT" = "$LOG_INPUT"
}

it_colorizes_an_info_message() {
  LOG_INPUT='this is information'
  LOG_OUTPUT=`info "INFO $LOG_INPUT"`

  LOG_TEST="INFO$mt_log_clear $LOG_INPUT$mt_log_clear"
  LOG_TEST=$mt_log_green$LOG_TEST

  test "$LOG_OUTPUT" = "`echo $LOG_TEST`"
}

it_colorizes_a_cascade_info_message() {
  LOG_INPUT='cascade'
  LOG_OUTPUT=`info "INFO $LOG_INPUT"`

  LOG_TEST="INFO$mt_log_blue $LOG_INPUT$mt_log_clear"
  LOG_TEST=$mt_log_green$LOG_TEST

  test "$LOG_OUTPUT" = "`echo $LOG_TEST`"
}

it_colorizes_a_multitool_info_message() {
  LOG_INPUT='multitool'
  LOG_OUTPUT=`info "INFO $LOG_INPUT"`

  LOG_TEST="INFO$mt_log_blue $LOG_INPUT$mt_log_clear"
  LOG_TEST=$mt_log_green$LOG_TEST

  test "$LOG_OUTPUT" = "`echo $LOG_TEST`"
}

it_colorizes_a_warning_message() {
  LOG_INPUT='deprecated'
  LOG_OUTPUT=`warn "WARN $LOG_INPUT"`

  LOG_TEST="WARN$mt_log_clear $LOG_INPUT$mt_log_clear"
  LOG_TEST=$mt_log_yellow$LOG_TEST

  test "$LOG_OUTPUT" = "`echo $LOG_TEST`"
}

it_colorizes_an_error_message() {
  LOG_INPUT='syntax error'
  LOG_OUTPUT=`error "ERROR $LOG_INPUT"`

  LOG_TEST="ERROR$mt_log_clear $LOG_INPUT$mt_log_clear"
  LOG_TEST=$mt_log_red$LOG_TEST

  test "$LOG_OUTPUT" = "`echo $LOG_TEST`"
}

it_indents_a_stacktrace() {
  LOG_INPUT='some stuff'
  
  LOG_OUTPUT=`stacktrace "$LOG_INPUT"`
  test "$LOG_OUTPUT" = "$LOG_INPUT"
  
  LOG_OUTPUT=`mt_log_stack_depth=2 stacktrace "$LOG_INPUT"`
  test "$LOG_OUTPUT" = "	$LOG_INPUT"
}