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
public class RunnerTest extends CascadingTestCase
  {
  public static final String trackData = "data/track.100.txt";
  public static final String topicData = "data/topic.100.txt";
  public static final String artistData = "data/artist.100.txt";

  public static final String outputPath = "build/test/output";

  public RunnerTest()
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

    params.add( new String[]{"cut", "1,2"} );

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

//    flow.writeDOT( "selectreject.dot" );

    flow.complete();

    validateLength( flow, 2, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){11}$" ) ); // we removed one line
    }

  public void testSelectFilename() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"select", "w"} );
    params.add( new String[]{"filename", ""} );

    params.add( new String[]{"sink", outputPath + "/selectfilename"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

//    flow.writeDOT( "selectreject.dot" );

    flow.complete();

    validateLength( flow, 16, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){12}file:.*/data/track.100.txt$" ) ); // we removed one line
    }

  public void testSort() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"cut", null} );
    params.add( new String[]{"group", "0"} );

    params.add( new String[]{"sink", outputPath + "/sort"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

    flow.complete();

    validateLength( flow, 99, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){11}$" ) ); // we removed one line
    }

  public void testConcat() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"cut", "2,3"} );
    params.add( new String[]{"concat", null} );
    params.add( new String[]{"concat.delim", "|"} );

    params.add( new String[]{"sink", outputPath + "/concat"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

    flow.complete();

    validateLength( flow, 99, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*)$" ) ); // we removed one line
    }

  public void testWordCount() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"expr", "$0.toLowerCase()"} );
    params.add( new String[]{"gen", "(?<!\\pL)(?=\\pL)[^\\s]*(?<=\\pL)(?!\\pL)"} );
    params.add( new String[]{"group", "0"} );
    params.add( new String[]{"count", null} );
    params.add( new String[]{"group", "1"} );

    params.add( new String[]{"sink", outputPath + "/wordcount"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

    flow.complete();

    validateLength( flow, 395, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){2}$" ) ); // we removed one line
    }

  public void testParseValues() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", topicData} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"cut", "0"} );
    params.add( new String[]{"pgen", "(\\b[12][09][0-9]{2}\\b)"} );
    params.add( new String[]{"group", "0"} );
    params.add( new String[]{"count", "0"} ); // adds count field
    params.add( new String[]{"group", "1"} );

    params.add( new String[]{"sink", outputPath + "/parsevalues"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

    flow.complete();

    validateLength( flow, 4, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){2}$" ) ); // we removed one line
    }

  public void testJoin() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.name", "lhs"} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"cut", "3"} );
    params.add( new String[]{"gen", ""} );
    params.add( new String[]{"gen.delim", " "} );

    params.add( new String[]{"debug", ""} );

    params.add( new String[]{"source", artistData} );
    params.add( new String[]{"source.name", "rhs"} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"cut", "0"} );
    params.add( new String[]{"gen", ""} );
    params.add( new String[]{"gen.delim", " "} );

    params.add( new String[]{"debug", ""} );

    params.add( new String[]{"join", ""} );
    params.add( new String[]{"join.lhs", "lhs"} );
    params.add( new String[]{"join.rhs", "rhs"} );

    params.add( new String[]{"count", ""} );

    params.add( new String[]{"sink", outputPath + "/join"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

//    flow.writeDOT( "join.dot" );

    flow.complete();

    validateLength( flow, 5, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){2}$" ) ); // we removed one line
    }
  }
