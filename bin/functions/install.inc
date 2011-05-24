# Copyright (c) 2011 Concurrent, Inc.

module_depends log jar

route_match "^install" mt_install

module_annotate install about "copy all files into place"
module_annotate_block install usage <<-USAGE
Copies all necessary files into place.

Usage:
  `basename $0` install [options]

Options:
  -d|--destination     - specifies the install destination
                         defaults to /usr/local/lib/multitool (as root/sudo)
                         defaults to $HOME/.multitool (as user)
  -b|--bin-destination - specifies the destination for executable files
                         defaults to /usr/local/bin
                         (only applies when running as root/sudo)
USAGE

mt_install_dirs="bin"
mt_install_bin="bin/multitool"
mt_install_bin_links="multitool mtl"
mt_install_files="README.txt LICENSE.txt"

if [ "$UID" = "0" ]
then
  mt_install_destination=/usr/local/lib/multitool
  mt_install_bin_destination=/usr/local/bin
else
  mt_install_destination=$HOME/.multitool
  mt_install_bin_destination=
fi

mt_install ()
{
  mkdir -p $mt_install_destination
  
  for dir in $mt_install_dirs
  do
    info "INFO Installing $mt_install_destination/$dir/*"
    cp -Rf $MT_PATH/$dir $mt_install_destination
  done

  for file in $mt_install_files
  do
    info "INFO Installing $mt_install_destination/$file"
    cp -f $MT_PATH/$file $mt_install_destination
  done

  mt_hadoop_avoid_exit=1 mt_hadoop
  mt_jar_avoid_exit=1 mt_jar

  if [ -n "$mt_jar_path" ]
  then
    info "INFO Installing $mt_install_destination/`basename $mt_jar_path`"
    cp -f $mt_jar_path $mt_install_destination/`basename $mt_jar_path`
  fi

  if [ "$UID" = "0" ]
  then
    chmod +x $mt_install_destination/$mt_install_bin
    mkdir -p $mt_install_bin_destination

    for link in $mt_install_bin_links
    do
      info "INFO Linking $mt_install_destination/$mt_install_bin -> $mt_install_bin_destination/$link"
      ln -sf $mt_install_destination/$mt_install_bin $mt_install_bin_destination/$link
    done
  elif ! echo $PATH | grep "$mt_install_destination/bin" > /dev/null
  then
    warn "WARN You should add $mt_install_destination/bin to your \$PATH."
  fi
}
