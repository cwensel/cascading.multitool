# Copyright (c) 2011 Concurrent, Inc.

module_dependencies jar

module_about parameters "prints a description of a Cascading.Multitool parameters"
module_describe_block parameters<<EOF
The available operational parameters of Cascading.Multitool.

Usage:
  [parameter=argument] [parameter=argument] ...

Parameters:
`mt_jar_usage | tail -n +5`
EOF
