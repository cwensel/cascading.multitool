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
  local full=$@
  if [ $# -gt 0 ]
  then
    shift
    local args=$@
  fi
  
  for endpoint in $ROUTE_ENDPOINTS_
  do
    local matcher=`route_match $endpoint`

    if echo "$full" | egrep $matcher > /dev/null
    then
      for step in `route_before $endpoint`
      do
        eval $step
      done

      eval $endpoint $args
      return
    fi
  done

  if [ -n $ROUTE_DEFAULT ]
  then
    for step in `route_before $ROUTE_DEFAULT`
    do
      eval $step
    done

    eval $ROUTE_DEFAULT $args
  fi
}

route_before ()
{
  if [ $# -eq 1 ]
  then
    local endpoint=$1

    eval echo \"\$ROUTE_BEFORE$endpoint\"
  else
    local endpoint=$1
    shift
    local args=$@

    eval ROUTE_BEFORE$endpoint=\"\$ROUTE_BEFORE$endpoint\$args \"
  fi
}
