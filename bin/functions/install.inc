# Copyright (c) 2011 Concurrent, Inc.

mt_install_dirs=(lib bin)
mt_install_binfiles=(bin/mt)
mt_install_files=(README.txt LICENSE.txt *.jar)

mt_install_destination=
mt_install_bin_destination=

function mt_install_usage () {
  echo $mt_install_usage_doc
}

function mt_install_set_destination () {
  if [ $UID -eq 0 ]; then
    mt_install_destination=/usr/local/lib/multitool
    mt_install_bin_destination=/usr/local/bin
  else
    mt_install_destination=$HOME/.multitool
  fi
}

function mt_install_remove_existing () {
  if [ -d $mt_install_destination ]; then
    echo "Removing $mt_install_destination"
    rm -rf $mt_install_destination
  fi

  if [ $UID -eq 0 ]; then
    for binfile in "${mt_install_binfiles[@]}" ; do
      binfile=$mt_install_bin_destination/`basename $binfile`

      if [[ -h $binfile ]]; then
        echo "Unlinking $binfile"
        rm -f $binfile
      fi
    done
  fi
}

function mt_install_all_files () {
  mkdir -p $mt_install_destination

  for dir in "${mt_install_dirs[@]}" ; do
    echo "Installing $mt_install_destination/$dir"
    cp -Rf $MT_PATH/$dir $mt_install_destination
  done

  for file in "${mt_install_files[@]}" ; do
    echo "Installing $mt_install_destination/$file"
    cp -f $MT_PATH/$file $mt_install_destination
  done

  for binfile in "${mt_install_binfiles[@]}" ; do
    echo "Setting multitool base location to $mt_install_destination"
    cat $mt_install_destination/$binfile | sed s,MT_PATH=.,MT_PATH=\"$mt_install_destination\",g | tee $mt_install_destination/$binfile > /dev/null
  done

  if [[ $UID -eq 0 ]]; then
    mkdir -p $mt_install_bin_destination

    for binfile in "${mt_install_binfiles[@]}" ; do
      echo "Linking $mt_install_bin_destination/$binfile"
      chmod +x $mt_install_destination/$binfile
      ln -sf $mt_install_destination/$binfile $mt_install_bin_destination/`basename $binfile`
    done
  elif ! echo $PATH | grep "$mt_install_destination/bin" > /dev/null; then
    echo "NOTE: You should add $mt_install_destination/bin to your \$PATH."
  fi
}

mt_install_usage_doc=$(cat <<-USAGE
install-mt writes Cascading.Multitool to its final destination.
Copyright (c) 2011 Concurrent, Inc.

Usage:
  install-mt [flags] [options]

Flags:
  -h|--help         - displays this message
  -u|--uninstall    - uninstall related files

Options:
  -d|--destination     - specifies the install destination
                         defaults to /usr/local/lib/multitool (as root/sudo)
                         defaults to $HOME/.multitool (as user)
  -b|--bin-destination - specifies the destination for executable files
                         defaults to /usr/local/bin
                         (only applies when running as root/sudo)
USAGE)
