/*
 * Copyright (c) 2009-2011 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Cascading is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cascading is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cascading.  If not, see <http://www.gnu.org/licenses/>.
 */

package multitool.factory;

import java.util.Map;

import cascading.pipe.CoGroup;
import cascading.pipe.Pipe;
import cascading.pipe.cogroup.InnerJoin;
import cascading.pipe.cogroup.Joiner;
import cascading.pipe.cogroup.LeftJoin;
import cascading.pipe.cogroup.OuterJoin;
import cascading.pipe.cogroup.RightJoin;
import cascading.tuple.Fields;

/**
 *
 */
public class CoGroupFactory extends PipeFactory
  {
  public CoGroupFactory( String alias )
    {
    super( alias );
    }

  public String getUsage()
    {
    return "what fields to join and group on, grouped fields are sorted";
    }

  public String[] getParameters()
    {
    return new String[]{"lhs", "lhs.group", "rhs", "rhs.group", "joiner", "name"};
    }

  public String[] getParametersUsage()
    {
    return new String[]{"source name of the lhs of the join", "lhs fields to group on, default FIRST",
                        "source name of the rhs of the join", "rhs fields to group on, default FIRST",
                        "join type: inner, outer, left, right", "branch name"};
    }

  public Pipe addAssembly( String value, Map<String, String> subParams, Map<String, Pipe> pipes, Pipe pipe )
    {
    String lhsName = getString( subParams, "lhs" );
    String rhsName = getString( subParams, "rhs" );

    Pipe lhsPipe = pipes.get( lhsName );
    Pipe rhsPipe = pipes.get( rhsName );

    if( lhsPipe == null )
      throw new IllegalArgumentException( "no source found with name: " + lhsName );

    if( rhsPipe == null )
      throw new IllegalArgumentException( "no source found with name: " + rhsName );

    Fields lhsFields = asFields( getString( subParams, "lhs.group" ) );
    Fields rhsFields = asFields( getString( subParams, "rhs.group" ) );

    if( lhsFields == null )
      lhsFields = Fields.FIRST;

    if( rhsFields == null )
      rhsFields = Fields.FIRST;

    Joiner joiner = null;
    String join = getString( subParams, "join" );

    if( join == null || join.isEmpty() || join.equalsIgnoreCase( "inner" ) )
      joiner = new InnerJoin();
    else if( join.equalsIgnoreCase( "outer" ) )
      joiner = new OuterJoin();
    else if( join.equalsIgnoreCase( "left" ) )
        joiner = new LeftJoin();
      else if( join.equalsIgnoreCase( "right" ) )
          joiner = new RightJoin();
        else
          throw new IllegalArgumentException( "unknown join type: " + join );


    return new CoGroup( lhsPipe, lhsFields, rhsPipe, rhsFields, joiner );
    }
  }