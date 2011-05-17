# Copyright (c) 2011 Concurrent, Inc.

module_depends log install

route_match "^uninstall" mt_uninstall

module_annotate uninstall about "remove all installed files"
module_annotate_block uninstall usage <<-USAGE
Removes all the installed files.

Usage:
  uninstall [options]

Options:
  -d|--destination     - specifies the install destination
                         defaults to /usr/local/lib/multitool (as root/sudo)
                         defaults to $HOME/.multitool (as user)
  -b|--bin-destination - specifies the destination for executable files
                         defaults to /usr/local/bin
                         (only applies when running as root/sudo)
USAGE

mt_uninstall ()
{
  if [ -d $mt_install_destination ]; then
    info "INFO Removing $mt_install_destination"
    rm -rf $mt_install_destination
  fi

  if [ "$mt_install_bin_destination" != "" ]; then
    for binfile in "${mt_install_binfiles[@]}" ; do
      binfile=$mt_install_bin_destination/`basename $binfile`

      if [ -h $binfile ]; then
        info "INFO Unlinking $binfile"
        rm -f $binfile
      fi
    done
  fi
}
