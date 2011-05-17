# Copyright (c) 2011 Concurrent, Inc.

module_dependencies log hadoop jar

module_about version "displays version information"
module_describe_block version <<USAGE
Prints version information about Cascading and Cascading.Multitool.

Usage:
  version
USAGE

mt_version ()
{
  cat <<VERSION
Cascading.Multitool: $mt_jar_path
`mt_jar_usage | head -n2`
VERSION
}
