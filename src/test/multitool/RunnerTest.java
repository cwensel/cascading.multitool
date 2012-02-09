/*
 * Copyright (c) 2007-2012 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package multitool;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import cascading.CascadingTestCase;
import cascading.flow.Flow;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.TupleEntryIterator;

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

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 99, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){11}$" ) ); // we removed one line
    iterator.close();
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

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 99, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){2}$" ) ); // we removed one line
    iterator.close();
    }

  public void testDiscard() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"cut", "1,2"} );
    params.add( new String[]{"discard", "1"} );

    params.add( new String[]{"sink", outputPath + "/discard"} );
    params.add( new String[]{"sink.replace", "true"} );

    Flow flow = new Main( params ).plan( new Properties() );

    flow.complete();

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 99, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){1}$" ) ); // we removed one line
    iterator.close();
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

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 2, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){11}$" ) ); // we removed one line
    iterator.close();
    }

  public void testSelectFilename() throws IOException
    {
    List<String[]> params = new LinkedList<String[]>();

    params.add( new String[]{"source", trackData} );
    params.add( new String[]{"source.skipheader", "true"} );

    params.add( new String[]{"select", "w"} );
    params.add( new String[]{"filename", "append"} );
    params.add( new String[]{"group", "0"} );
    params.add( new String[]{"unique", ""} );

    params.add( new String[]{"sink", outputPath + "/selectfilename"} );
    params.add( new String[]{"sink.replace", "true"} );
    params.add( new String[]{"sink.parts", "0"} );

    Flow flow = new Main( params ).plan( new Properties() );

//    flow.writeDOT( "selectreject.dot" );

    flow.complete();

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 16, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){12}file:.*/data/track.100.txt$" ) ); // we removed one line
    iterator.close();
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

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 99, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){11}$" ) ); // we removed one line
    iterator.close();
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

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 99, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*)$" ) ); // we removed one line
    iterator.close();
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

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 395, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){2}$" ) ); // we removed one line
    iterator.close();
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

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 4, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){2}$" ) ); // we removed one line
    iterator.close();
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

    TupleEntryIterator iterator = flow.openTapForRead( new Hfs( new TextLine(), flow.getSink().getIdentifier().toString() ) );
    validateLength( iterator, 5, 2, Pattern.compile( "^[0-9]+(\\t[^\\t]*){2}$" ) ); // we removed one line
    iterator.close();
    }
  }
