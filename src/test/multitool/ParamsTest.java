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

package multitool;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import cascading.CascadingTestCase;

/**
 *
 */
public class ParamsTest extends CascadingTestCase
  {
  public ParamsTest()
    {
    super( "params tests" );
    }

  public void testBadCommand() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", "path"} );
    params.add( new String[]{"fudge", "path"} );
    params.add( new String[]{"sink", "path"} );

    try
      {
      new Main( params ).plan( new Properties() );
      fail( "did not catch out of order params" );
      }
    catch( IllegalArgumentException exception )
      {
      // ignore
      }
    }

  public void testBadSource() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"fudge", "path"} );
    params.add( new String[]{"sink", "path"} );

    try
      {
      new Main( params ).plan( new Properties() );
      fail( "did not catch out of order params" );
      }
    catch( IllegalArgumentException exception )
      {
      // ignore
      }
    }

  public void testBadSink() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", "path"} );
    params.add( new String[]{"fudge", "path"} );

    try
      {
      new Main( params ).plan( new Properties() );
      fail( "did not catch out of order params" );
      }
    catch( IllegalArgumentException exception )
      {
      // ignore
      }
    }

  public void testSubsWrongOrder() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", "path"} );
    params.add( new String[]{"sink", "path"} );
    params.add( new String[]{"source.skipheader", "true"} );
    params.add( new String[]{"sink.replace", "true"} );

    try
      {
      new Main( params ).plan( new Properties() );
      fail( "did not catch out of order params" );
      }
    catch( IllegalArgumentException exception )
      {
      // ignore
      }
    }
  }