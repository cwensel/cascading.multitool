# Copyright (c) 2011 Concurrent, Inc.

route_match ()
{
  if [ $# -eq 1 ]
  then
    local endpoint=$1

    eval echo \"\$ROUTE_$endpoint\"
  else
    local matcher=$1
    local endpoint=$2

    for existing in $ROUTE_ENDPOINTS_
    do
      [ "$endpoint" = "$existing" ] && break 1
    done
    
    if [ "$endpoint" != "$existing" ]
    then
      ROUTE_ENDPOINTS_="$ROUTE_ENDPOINTS_$endpoint "
      eval ROUTE_$endpoint=\$matcher
    fi
  fi
}

route_default ()
{
  ROUTE_DEFAULT=$1
}

route_perform ()
{
  for endpoint in $ROUTE_ENDPOINTS_
  do
    local matcher=`route_match $endpoint`

    if echo "$@" | egrep $matcher > /dev/null
    then
      shift
      local args=$@
      eval $endpoint $args
      return
    fi
  done
  
  [ -n $ROUTE_DEFAULT ] && eval $ROUTE_DEFAULT $args
}
