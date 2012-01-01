/*
 * Copyright (c) 2007-2012 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package multitool.factory;

import java.util.Map;

import cascading.pipe.CoGroup;
import cascading.pipe.Pipe;
import cascading.pipe.joiner.InnerJoin;
import cascading.pipe.joiner.Joiner;
import cascading.pipe.joiner.LeftJoin;
import cascading.pipe.joiner.OuterJoin;
import cascading.pipe.joiner.RightJoin;
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