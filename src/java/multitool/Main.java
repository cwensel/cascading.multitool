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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import cascading.flow.Flow;
import cascading.flow.FlowConnector;
import cascading.pipe.Pipe;
import cascading.tap.Tap;
import multitool.facctory.ConcatFactory;
import multitool.facctory.CountFactory;
import multitool.facctory.CutFactory;
import multitool.facctory.DebugFactory;
import multitool.facctory.ExpressionFactory;
import multitool.facctory.Factory;
import multitool.facctory.GenFactory;
import multitool.facctory.GroupByFactory;
import multitool.facctory.ParserFactory;
import multitool.facctory.ParserGenFactory;
import multitool.facctory.PipeFactory;
import multitool.facctory.RejectFactory;
import multitool.facctory.SelectExpressionFactory;
import multitool.facctory.SelectFactory;
import multitool.facctory.SinkFactory;
import multitool.facctory.SourceFactory;
import multitool.facctory.SumFactory;
import multitool.facctory.TapFactory;
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
    new CutFactory( "cut" ), new ParserFactory( "parse" ), new ParserGenFactory( "pgen" ),
    new GroupByFactory( "group" ), new ConcatFactory( "concat" ), new GenFactory( "gen" ), new CountFactory( "count" ),
    new SumFactory( "sum" ), new ExpressionFactory( "expr" ), new SelectExpressionFactory( "sexpr" ),
    new DebugFactory( "debug" )};

  static Map<String, Factory> factoryMap = new HashMap<String, Factory>();

  static
    {
    for( Factory factory : TAP_FACTORIES )
      factoryMap.put( factory.getAlias(), factory );

    for( Factory factory : PIPE_FACTORIES )
      factoryMap.put( factory.getAlias(), factory );
    }

  private List<String[]> params;

  public static void main( String[] args )
    {

    List<String[]> params = new LinkedList<String[]>();

    for( String arg : args )
      {
      int index = arg.indexOf( "=" );

      if( index != -1 )
        params.add( new String[]{arg.substring( 0, index ), arg.substring( index + 1 )} );
      else
        params.add( new String[]{arg, null} );
      }

    try
      {
      new Main( params ).execute();
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
    System.out.println( "" );
    System.out.println( "Usage:" );

    printFactoryUsage( TAP_FACTORIES );
    printFactoryUsage( PIPE_FACTORIES );

    System.exit( 1 );
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
    this.params = params;

    validateParams();
    }

  private void validateParams()
    {
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
    plan( getDefaultProperties() ).complete();
    }

  public Flow plan( Properties properties )
    {

    Map<String, Tap> sources = new HashMap<String, Tap>();
    Map<String, Tap> sinks = new HashMap<String, Tap>();
    String name = "multitool";
    Pipe pipe = new Pipe( name );

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
        sources.put( name, ( (TapFactory) factory ).getTap( value, subParams ) );
        pipe = ( (TapFactory) factory ).addAssembly( value, subParams, pipe );
        }
      else if( factory instanceof SinkFactory )
        {
        sinks.put( name, ( (TapFactory) factory ).getTap( value, subParams ) );
        pipe = ( (TapFactory) factory ).addAssembly( value, subParams, pipe );
        }
      else
        {
        pipe = ( (PipeFactory) factory ).addAssembly( value, subParams, pipe );
        }
      }

    if( sources.isEmpty() )
      throw new IllegalArgumentException( "error: must have one source" );

    if( sinks.isEmpty() )
      throw new IllegalArgumentException( "error: must have one sink" );

    return new FlowConnector( properties ).connect( sources, sinks, pipe );
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
