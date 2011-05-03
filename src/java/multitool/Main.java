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
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import cascading.cascade.Cascade;
import cascading.flow.Flow;
import cascading.flow.FlowConnector;
import cascading.flow.PlannerException;
import cascading.pipe.Pipe;
import cascading.tap.Tap;
import multitool.factory.CoGroupFactory;
import multitool.factory.ConcatFactory;
import multitool.factory.CountFactory;
import multitool.factory.CutFactory;
import multitool.factory.DebugFactory;
import multitool.factory.ExpressionFactory;
import multitool.factory.Factory;
import multitool.factory.FileNameFactory;
import multitool.factory.GenFactory;
import multitool.factory.GroupByFactory;
import multitool.factory.ParserFactory;
import multitool.factory.ParserGenFactory;
import multitool.factory.PipeFactory;
import multitool.factory.RejectFactory;
import multitool.factory.ReplaceFactory;
import multitool.factory.SelectExpressionFactory;
import multitool.factory.SelectFactory;
import multitool.factory.ShapeFactory;
import multitool.factory.SinkFactory;
import multitool.factory.SourceFactory;
import multitool.factory.SumFactory;
import multitool.factory.TapFactory;
import multitool.factory.UniqueFactory;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Main
  {
  private static final Logger LOG = LoggerFactory.getLogger( Main.class );

  static TapFactory[] TAP_FACTORIES = new TapFactory[]{new SourceFactory( "source" ), new SinkFactory( "sink" )};

  static PipeFactory[] PIPE_FACTORIES = new PipeFactory[]{new RejectFactory( "reject" ), new SelectFactory( "select" ),
                                                          new CutFactory( "cut" ), new ParserFactory( "parse" ),
                                                          new ShapeFactory( "shape" ),
                                                          new ParserGenFactory( "pgen" ),
                                                          new ReplaceFactory( "replace" ),
                                                          new GroupByFactory( "group" ), new CoGroupFactory( "join" ),
                                                          new ConcatFactory( "concat" ),
                                                          new GenFactory( "gen" ), new CountFactory( "count" ),
                                                          new SumFactory( "sum" ), new ExpressionFactory( "expr" ),
                                                          new SelectExpressionFactory( "sexpr" ),
                                                          new DebugFactory( "debug" ),
                                                          new FileNameFactory( "filename" ),
                                                          new UniqueFactory( "unique" )};

  static Map<String, Factory> factoryMap = new HashMap<String, Factory>();

  static
    {
    for( Factory factory : TAP_FACTORIES )
      factoryMap.put( factory.getAlias(), factory );

    for( Factory factory : PIPE_FACTORIES )
      factoryMap.put( factory.getAlias(), factory );
    }

  private Map<String, String> options;
  private List<String[]> params;

  public static void main( String[] args )
    {

    Map<String, String> options = new LinkedHashMap<String, String>();
    List<String[]> params = new LinkedList<String[]>();

    for( String arg : args )
      {
      int index = arg.indexOf( "=" );

      if( arg.startsWith( "-" ) )
        {
        if( index != -1 )
          options.put( arg.substring( 0, index ), arg.substring( index + 1 ) );
        else
          options.put( arg, null );
        }
      else
        {
        if( index != -1 )
          params.add( new String[]{arg.substring( 0, index ), arg.substring( index + 1 )} );
        else
          params.add( new String[]{arg, null} );
        }
      }

    try
      {
      new Main( options, params ).execute();
      }
    catch( IllegalArgumentException exception )
      {
      System.out.println( exception.getMessage() );
      printUsage();
      }

    }

  private static void printUsage()
    {
    System.out.println( "multitool [param] [param] ..." );
    printCascadingVersion();

    System.out.println( "" );
    printLicense();

    System.out.println( "" );
    System.out.println( "Usage:" );

    printFactoryUsage( TAP_FACTORIES );
    printFactoryUsage( PIPE_FACTORIES );

    System.exit( 1 );
    }

  private static void printLicense()
    {
    try
      {
      InputStream stream = Main.class.getResourceAsStream( "/LICENSE.txt" );
      BufferedReader reader = new BufferedReader( new InputStreamReader( stream ) );
      String line = null;

      System.out.print( "Licensed under " );

      while ( ( line = reader.readLine() ) != null && line.length() > 0 )
        System.out.println( line );

      reader.close();
      }
    catch( IOException exception )
      {
      System.out.println( "Unspecified License" );
      }
    }

  private static void printCascadingVersion()
   {
     try
       {
       Properties versionProperties = new Properties();

       InputStream stream = Cascade.class.getClassLoader().getResourceAsStream( "cascading/version.properties" );
       versionProperties.load( stream );

       stream = Cascade.class.getClassLoader().getResourceAsStream( "cascading/build.number.properties" );
       versionProperties.load( stream );

       String releaseMajor = versionProperties.getProperty( "cascading.release.major" );
       String releaseMinor = versionProperties.getProperty( "cascading.release.minor", null );
       String releaseBuild = versionProperties.getProperty( "build.number", null );
       String hadoopVersion = versionProperties.getProperty( "cascading.hadoop.compatible.version" );
       String releaseFull = null;

       if( releaseMinor == null )
         releaseFull = releaseMajor;
       else
         if( releaseBuild == null )
           releaseFull = String.format( "%s.%s", releaseMajor, releaseMinor );
         else
           releaseFull = String.format( "%s.%s%s", releaseMajor, releaseMinor, releaseBuild );


       System.out.println( String.format( "Built against Cascading %s on %s", releaseFull, hadoopVersion ) );
       }
     catch( IOException exception )
       {
       System.out.println( "Unknown Cascading Version" );
       }
   }

  private static void printFactoryUsage( Factory[] factories )
    {
    for( Factory factory : factories )
      {
      System.out.println( String.format( "  %-10s  %s", factory.getAlias(), factory.getUsage() ) );

      for( String[] strings : factory.getParametersAndUsage() )
        System.out.println( String.format( "  %-10s  %s", strings[ 0 ], strings[ 1 ] ) );
      }
    }

  public Main( List<String[]> params )
    {
    this( new LinkedHashMap<String, String>(), params );
    }

  public Main( Map<String, String> options, List<String[]> params )
    {
    if( options != null )
      this.options = options;

    this.params = params;

    validateParams();
    }

  private void validateParams()
    {
    if( params.size() == 0 )
      throw new IllegalArgumentException( "error: no args given" );

    for( String[] param : params )
      {
      String alias = param[ 0 ].replaceFirst( "^([^.]+).*$", "$1" );

      if( !factoryMap.keySet().contains( alias ) )
        throw new IllegalArgumentException( "error: invalid argument: " + param[ 0 ] );
      }

    if( !params.get( 0 )[ 0 ].equals( "source" ) )
      throw new IllegalArgumentException( "error: first command must be source: " + params.get( 0 )[ 0 ] );

    if( !params.get( params.size() - 1 )[ 0 ].startsWith( "sink" ) )
      throw new IllegalArgumentException( "error: last command must be sink: " + params.get( params.size() - 1 ) );
    }

  private Properties getDefaultProperties()
    {
    Properties properties = new Properties();

    FlowConnector.setApplicationJarClass( properties, Main.class );

    properties.setProperty( "mapred.output.compression.codec", GzipCodec.class.getName() );
    properties.setProperty( "mapred.child.java.opts", "-server -Xmx512m" );
    properties.setProperty( "mapred.reduce.tasks.speculative.execution", "false" );
    properties.setProperty( "mapred.map.tasks.speculative.execution", "false" );

//    int trackers = getNumTaskTrackers();
//    properties.setProperty( "mapred.map.tasks", "" );
//    properties.setProperty( "mapred.reduce.tasks", "" );

    return properties;
    }

  private int getNumTaskTrackers()
    {
    ClusterStatus status = null;

    try
      {
      status = new JobClient( new JobConf() ).getClusterStatus();
      }
    catch( IOException exception )
      {
      LOG.warn( "failed getting cluster status", exception );

      return 1;
      }

    return status.getTaskTrackers();
    }

  public void execute()
    {
    try
      {
      Flow flow = plan( getDefaultProperties() );

      if( options.containsKey( "-dot" ) )
        flow.writeDOT( options.get( "-dot" ) );

      flow.complete();
      }
    catch( PlannerException exception )
      {
      if( options.containsKey( "-dot" ) )
        exception.writeDOT( options.get( "-dot" ) );

      throw exception;
      }
    }

  public Flow plan( Properties properties )
    {

    Map<String, Pipe> pipes = new HashMap<String, Pipe>();
    Map<String, Tap> sources = new HashMap<String, Tap>();
    Map<String, Tap> sinks = new HashMap<String, Tap>();
    Pipe currentPipe = null;

    ListIterator<String[]> iterator = params.listIterator();

    while( iterator.hasNext() )
      {
      String[] pair = iterator.next();
      String key = pair[ 0 ];
      String value = pair[ 1 ];
      LOG.info( "key: {}", key );
      Map<String, String> subParams = getSubParams( key, iterator );

      Factory factory = factoryMap.get( key );

      if( factory instanceof SourceFactory )
        {
        Tap tap = ( (TapFactory) factory ).getTap( value, subParams );
        currentPipe = ( (TapFactory) factory ).addAssembly( value, subParams, currentPipe );
        sources.put( currentPipe.getName(), tap );
        }
      else if( factory instanceof SinkFactory )
        {
        sinks.put( currentPipe.getName(), ( (TapFactory) factory ).getTap( value, subParams ) );
        currentPipe = ( (TapFactory) factory ).addAssembly( value, subParams, currentPipe );
        }
      else
        {
        currentPipe = ( (PipeFactory) factory ).addAssembly( value, subParams, pipes, currentPipe );
        }

      pipes.put( currentPipe.getName(), currentPipe );
      }

    if( sources.isEmpty() )
      throw new IllegalArgumentException( "error: must have atleast one source" );

    if( sinks.isEmpty() )
      throw new IllegalArgumentException( "error: must have one sink" );

    return new FlowConnector( properties ).connect( "multitool", sources, sinks, currentPipe );
    }

  private Map<String, String> getSubParams( String key, ListIterator<String[]> iterator )
    {
    Map<String, String> subParams = new LinkedHashMap<String, String>();

    int index = iterator.nextIndex();
    for( int i = index; i < params.size(); i++ )
      {
      String current = params.get( i )[ 0 ];
      int dotIndex = current.indexOf( '.' );

      if( dotIndex == -1 )
        break;

      if( !current.startsWith( key + "." ) )
        throw new IllegalArgumentException( "error: param out of order: " + current + ", should follow: " + key );

      subParams.put( current.substring( dotIndex + 1 ), params.get( i )[ 1 ] );
      iterator.next();
      }

    return subParams;
    }
  }
