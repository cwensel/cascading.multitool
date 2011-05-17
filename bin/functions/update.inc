# Copyright (c) 2011 Concurrent, Inc.

module_depends log

route_match "^update$" mt_run

module_annotate update about "update Cascading.Multitool"
module_annotate_block run usage <<USAGE
Update Cascading.Multitool from the source.

Usage:
  update [flags]

Flags:
  -v|--version - specify the version of Cascading.Multitool to fetch
USAGE

mt_update ()
{
  echo "Hi folks"
}