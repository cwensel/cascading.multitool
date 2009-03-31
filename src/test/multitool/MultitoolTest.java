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

package multitool;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import cascading.CascadingTestCase;
import cascading.flow.Flow;

/**
 *
 */
public class MultitoolTest extends CascadingTestCase
  {
  public static final String artistData = "build/data/artist.100.txt";
  public static final String trackData = "build/data/track.100.txt";

  public static final String outputPath = "build/test/output";

  public MultitoolTest()
    {
    super( "basic tests" );
    }

  public void testCopy() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.skipheader", "true"} );
    params.add( new String[]{"sink", outputPath + "/simple"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

    flow.complete();

    validateLength( flow, 99, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){11}$" ) ); // we removed one line
    }

  public void testCut() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"cut", "2,3"} );

    params.add( new String[]{"sink", outputPath + "/cut"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

    flow.complete();

    validateLength( flow, 99, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){2}$" ) ); // we removed one line
    }

  public void testSelectReject() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"select", "w"} );
    params.add( new String[]{"reject", "o"} );

    params.add( new String[]{"sink", outputPath + "/selectreject"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

    flow.complete();

    validateLength( flow, 2, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){11}$" ) ); // we removed one line
    }

  }
