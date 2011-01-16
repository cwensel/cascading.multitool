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

import cascading.operation.Identity;
import cascading.operation.regex.RegexSplitter;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;

/**
 *
 */
public class CutFactory extends PipeFactory
  {
  public CutFactory( String alias )
    {
    super( alias );
    }

  public String getUsage()
    {
    return "split the first field, and return the given fields, or all fields. 0 for first, -1 for last";
    }

  public String[] getParameters()
    {
    return new String[]{"delim"};
    }

  public String[] getParametersUsage()
    {
    return new String[]{"regex delimiter, defaut: '\\t' (TAB)"};
    }

  public Pipe addAssembly( String value, Map<String, String> subParams, Map<String, Pipe> pipes, Pipe pipe )
    {
    Fields fields = asFields( value );
    String delim = getString( subParams, "delim", "\\t" );

    // cut parses the first field and returns fields out of the results
    pipe = new Each( pipe, Fields.FIRST, new RegexSplitter( delim ) );

    if( fields != null )
      pipe = new Each( pipe, fields, new Identity() );

    return pipe;
    }
  }