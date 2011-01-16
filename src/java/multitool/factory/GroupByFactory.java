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

import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;

/**
 *
 */
public class GroupByFactory extends PipeFactory
  {
  public GroupByFactory( String alias )
    {
    super( alias );
    }

  public String getUsage()
    {
    return "what fields to group/sort on, grouped fields are sorted";
    }

  public String[] getParameters()
    {
    return new String[]{"secondary", "secondary.reverse"};
    }

  public String[] getParametersUsage()
    {
    return new String[]{"fields to secondary sort on", "set true to reverse secondary sort"};
    }

  public Pipe addAssembly( String value, Map<String, String> subParams, Map<String, Pipe> pipes, Pipe pipe )
    {
    Fields groupFields = asFields( value );
    Fields secondaryFields = asFields( getString( subParams, "secondary", null ) );
    boolean isReverse = getBoolean( subParams, "secondary.reverse", false );

    if( secondaryFields == null )
      return new GroupBy( pipe, groupFields );

    return new GroupBy( pipe, groupFields, secondaryFields, isReverse );
    }
  }