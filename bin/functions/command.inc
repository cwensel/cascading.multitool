# Copyright (c) 2011 Concurrent, Inc.

module_depends jar help

route_match "^help command \w+\$" mt_command
route_before mt_command mt_hadoop mt_jar

module_annotate command about "describes a Cascading.Multitool parameter"


mt_command_table ()
{
  names=`mt_jar_avoid_exit=1 mt_jar_usage | tail -n +5 | sed -e 's/^[ \t]*//' | cut -d " " -f1 | sort`
  set -- $names
  while [ $# -gt 0 ]
  do
    printf "  %-25s%-25s%-25s" "$1" "$2" "$3"
    echo
    shift 3
  done
}


module_annotate_block command usage <<EOF
Examine the available operational parameters of Cascading.Multitool.

Usage:
  help command [parameter]

Parameters:
`mt_command_table`
EOF



mt_command ()
{
  module_annotate help banner
  echo
  
  request=$2
  set -- `mt_jar_usage | egrep "^  $request "`
  if [ $# -gt 0 ]
  then
    name=$1
    shift
    description=$@
    cat <<-USAGE
$name: $description

Usage:
  multitool $name=[value]
USAGE
  else
    module_annotate command usage
    echo
    
    error "ERROR Not a command: $request"
  fi
}
