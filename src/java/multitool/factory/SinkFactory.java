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

import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.SequenceFile;
import cascading.scheme.hadoop.TextDelimited;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

/**
 *
 */
public class SinkFactory extends TapFactory
  {
  public SinkFactory( String alias )
    {
    super( alias );
    }

  public Tap getTap( String value, Map<String, String> params )
    {
    SinkMode mode = SinkMode.KEEP;

    if( getBoolean( params, "replace" ) )
      mode = SinkMode.REPLACE;

    Fields sinkFields = asFields( getString( params, "select" ) );

    if( sinkFields == null )
      sinkFields = Fields.ALL;

    Scheme scheme;

    if( !containsKey( params, "seqfile" ) )
      {
      String compress = getString( params, "compress", TextLine.Compress.DEFAULT.toString() );
      String delim = getString( params, "delim", "\t" );
      TextLine.Compress compressEnum = TextLine.Compress.valueOf( compress.toUpperCase() );
      scheme = new TextDelimited( sinkFields, compressEnum, delim );
      }
    else
      {
      scheme = new SequenceFile( sinkFields );
      }

    return new Hfs( scheme, value, mode );
    }

  public Pipe addAssembly( String value, Map<String, String> subParams, Pipe pipe )
    {
    return pipe;
    }

  public String getUsage()
    {
    return "an url to output path";
    }

  public String[] getParameters()
    {
    return new String[]{"select", "replace", "compress", "delim", "seqfile"};
    }

  public String[] getParametersUsage()
    {
    return new String[]{"fields to sink", "set true of output should be overwritten",
                        "compression: enable, disable, or default",
                        "the delimiter to use to separate values",
                        "write to a sequence file instead of text, delim and compress are ignored"};
    }
  }