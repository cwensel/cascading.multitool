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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class Main
  {
  static String[][] PARAMETERS = new String[][]{{"src", "url to input dataset"},
    {"src.header", "set true if should skip first line"}, {"reject", "regex, matches are discarded"},
    {"select", "regex, matches are kept"}};

  static Set<String> paramSet = new HashSet<String>();

  static
    {
    for( String[] strings : PARAMETERS )
      paramSet.add( strings[ 0 ] );
    }

  private Map<String, String> params;

  public static void main( String[] args )
    {

    Map<String, String> params = new LinkedHashMap<String, String>();

    for( String arg : args )
      {
      String[] split = arg.split( "=" );
      params.put( split[ 0 ], split[ 1 ] );
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

    for( String[] strings : PARAMETERS )
      System.out.println( String.format( "  %-10s  %s", strings[ 0 ], strings[ 1 ] ) );

    System.exit( 1 );
    }

  public Main( Map<String, String> params )
    {
    this.params = params;

    validateParams();
    }

  private void validateParams()
    {
    for( String param : params.keySet() )
      {
      if( !paramSet.contains( param ) )
        throw new IllegalArgumentException( "error: invalid argument: " + param );
      }
    }

  private void execute()
    {

    }
  }
