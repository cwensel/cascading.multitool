if [[ "$MT_PATH" == "" ]]; then
  MT_PATH=`dirname $(cd ${0%/*}/../../../.. && echo $PWD/${0##*/})`
fi

function include_dependencies () {
  if [[ $# -gt 0 ]]; then
    for dep in "$@"; do
      . $MT_PATH/bin/functions/$dep.inc
    done
  fi
}
