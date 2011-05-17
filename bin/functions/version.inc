# Copyright (c) 2011 Concurrent, Inc.

module_depends log hadoop jar
module_annotate version about "displays version information"
module_annotate_block version usage <<USAGE
Prints version information about Cascading and Cascading.Multitool.

Usage:
  version
USAGE

mt_version ()
{
  if [ -n `module_annotate version data` ]; then
    module_annotate_block version data <<VERSION
Cascading.Multitool: $mt_jar_path
`mt_jar_usage | head -n2`
VERSION
  fi
  
  module_annotate version data
}
