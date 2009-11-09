/*
 * Copyright (c) 2007-2009 Concurrent, Inc. All Rights Reserved.
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

import cascading.operation.Identity;
import cascading.operation.expression.ExpressionFilter;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.scheme.TextLine;
import cascading.tap.Hfs;
import cascading.tap.Tap;
import cascading.tuple.Fields;

/**
 *
 */
public class SourceFactory extends TapFactory
  {
  public SourceFactory( String alias )
    {
    super( alias );
    }

  public Tap getTap( String value, Map<String, String> params )
    {
    return new Hfs( new TextLine(), value );
    }

  public Pipe addAssembly( String value, Map<String, String> subParams, Pipe pipe )
    {
    String name = getString( subParams, "name" );

    if( name == null || name.isEmpty() )
      name = "multitool";

    pipe = new Pipe( name );

    if( getBoolean( subParams, "skipheader" ) )
      pipe = new Each( pipe, new Fields( 0 ), new ExpressionFilter( "$0 == 0", Long.class ) );

    pipe = new Each( pipe, new Fields( 1 ), new Identity() );

    return pipe;
    }

  public String getUsage()
    {
    return "an url to input data";
    }

  public String[] getParameters()
    {
    return new String[]{"name", "skipheader"};
    }

  public String[] getParametersUsage()
    {
    return new String[]{"name of this source, required if more than one",
                        "set true if the first line should be skipped"};
    }
  }
