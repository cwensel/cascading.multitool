# Copyright (c) 2011 Concurrent, Inc.

module_depends log hadoop jar

route_match "^(version|-v|--version)$" mt_version

module_annotate version about "displays version information"
module_annotate_block version usage <<USAGE
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
