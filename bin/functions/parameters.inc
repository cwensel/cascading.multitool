# Copyright (c) 2011 Concurrent, Inc.

module_depends jar
module_annotate parameters about "prints a description of a Cascading.Multitool parameters"
module_annotate_block parameters usage <<EOF
The available operational parameters of Cascading.Multitool.

Usage:
  [parameter=argument] [parameter=argument] ...

Parameters:
`mt_jar_avoid_exit=1 mt_jar_usage | tail -n +5`
EOF
