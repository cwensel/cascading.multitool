# Copyright (c) 2011 Concurrent, Inc.

route_match ()
{
  if [ $# -eq 1 ]
  then
    endpoint=$1

    eval echo \"\$ROUTE_$endpoint\"
  else
    matcher=$1
    endpoint=$2

    for existing in $ROUTE_ENDPOINTS_
    do
      [ "$endpoint" = "$existing" ] && break 1
    done
    
    if [ "$endpoint" != "$existing" ]
    then
      ROUTE_ENDPOINTS_="$ROUTE_ENDPOINTS_$endpoint "
      eval ROUTE_$endpoint=\"\$matcher\"
    fi
  fi
}

route_default ()
{
  ROUTE_DEFAULT=$1
}

route_perform ()
{
  full=$@

  if [ $# -gt 0 ]
  then
    shift
    args=$@
  fi

  for endpoint in $ROUTE_ENDPOINTS_
  do
    matcher=`route_match $endpoint`

    if echo $full | egrep "$matcher" > /dev/null
    then
      for step in `route_before $endpoint`
      do
        eval $step \$args
      done

      eval $endpoint \$args
      return
    fi
  done

  if [ -n $ROUTE_DEFAULT ]
  then
    for step in `route_before $ROUTE_DEFAULT`
    do
      eval $step \$full
    done

    eval $ROUTE_DEFAULT \$full
  fi
}

route_before ()
{
  endpoint=$1

  if [ $# -eq 1 ]
  then
    eval echo \"\$ROUTE_BEFORE$endpoint\"
  else
    if [ $# -gt 1 ]
    then
      shift
      args=$@
    fi
    
    eval ROUTE_BEFORE$endpoint=\"\$ROUTE_BEFORE$endpoint\$args \"
  fi
}
