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

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;

/**
 *
 */
public class FileNameFactory extends PipeFactory
  {
  private class FileNameFunction extends BaseOperation implements Function
    {
    private FileNameFunction( Fields fieldDeclaration )
      {
      super( fieldDeclaration );
      }

    @Override
    public void operate( FlowProcess flowProcess, FunctionCall functionCall )
      {
      String filename = (String) flowProcess.getProperty( "cascading.source.path" );
      functionCall.getOutputCollector().add( new Tuple( filename ) );
      }
    }

  public FileNameFactory( String alias )
    {
    super( alias );
    }

  public String getUsage()
    {
    return "include the filename from which the current value was found";
    }

  public String[] getParameters()
    {
    return new String[]{"append", "only"};
    }

  public String[] getParametersUsage()
    {
    return new String[]{"append the filename to the record", "only return the filename"};
    }

  public Pipe addAssembly( String value, Map<String, String> subParams, Map<String, Pipe> pipes, Pipe pipe )
    {
    Fields fields = Fields.ALL;

    if( value != null && value.equalsIgnoreCase( "append" ) )
      fields = Fields.ALL;
    else if( value != null && value.equalsIgnoreCase( "only" ) )
      fields = Fields.RESULTS;

    return new Each( pipe, new FileNameFunction( new Fields( "filename" ) ), fields );
    }
  }