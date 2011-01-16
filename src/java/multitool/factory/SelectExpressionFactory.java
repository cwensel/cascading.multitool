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

import cascading.operation.expression.ExpressionFilter;
import cascading.operation.filter.Not;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;

/**
 *
 */
public class SelectExpressionFactory extends PipeFactory
  {
  public SelectExpressionFactory( String alias )
    {
    super( alias );
    }

  public String getUsage()
    {
    return "use java expression as filter, e.g. $0 != null";
    }

  public String[] getParameters()
    {
    return new String[]{"args"};
    }

  public String[] getParametersUsage()
    {
    return new String[]{"the fields to use as arguments"};
    }

  public Pipe addAssembly( String value, Map<String, String> subParams, Map<String, Pipe> pipes, Pipe pipe )
    {
    Fields fields = asFields( getString( subParams, "args", null ) );

    if( fields == null )
      fields = Fields.FIRST;

    return new Each( pipe, fields, new Not( new ExpressionFilter( value, String.class ) ) );
    }
  }