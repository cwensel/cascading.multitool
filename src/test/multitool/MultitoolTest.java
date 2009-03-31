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
import java.util.regex.Pattern;

import cascading.CascadingTestCase;
import cascading.flow.Flow;
import cascading.flow.FlowConnector;
import cascading.pipe.Pipe;
import cascading.scheme.TextLine;
import cascading.tap.Hfs;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import multitool.assembly.ArtistParser;
import multitool.assembly.TrackParser;

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

  public void testParser() throws IOException
    {
    Tap source = new Hfs( new TextLine(), trackData );
    Tap sink = new Hfs( new TextLine(), outputPath + "/trackparser", SinkMode.REPLACE );

    Pipe pipe = new Pipe( "trackparser" );

//    pipe = new TrackParser( pipe );

    Flow flow = new FlowConnector().connect( "trackparser", source, sink, pipe );

    flow.complete();

    validateLength( flow, 99, 2, Pattern.compile( "[\\d]*(\\t[^\\t]*){11}" ) ); // we removed one line
    }

  }
