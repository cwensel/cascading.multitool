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

import java.io.Serializable;
import java.util.Map;

import cascading.tuple.Fields;

/**
 *
 */
public abstract class Factory implements Serializable
  {
  protected String alias;

  protected Factory( String alias )
    {
    this.alias = alias;
    }

  public String getAlias()
    {
    return alias;
    }

  public String[][] getParametersAndUsage()
    {
    String[][] results = new String[getParameters().length][2];

    for( int i = 0; i < getParameters().length; i++ )
      {
      results[ i ][ 0 ] = getAlias() + "." + getParameters()[ i ];
      results[ i ][ 1 ] = getParametersUsage()[ i ];
      }

    return results;
    }

  public abstract String getUsage();

  public abstract String[] getParameters();

  public abstract String[] getParametersUsage();

  protected Fields asFields( String fields )
    {
    if( fields == null || fields.isEmpty() )
      return null;

    String[] split = fields.split( "," );
    Comparable[] comparables = new Comparable[split.length];

    for( int i = 0; i < split.length; i++ )
      comparables[ i ] = Integer.parseInt( split[ i ] );

    return new Fields( comparables );
    }

  protected int[] getIntArray( String values )
    {
    if( values == null )
      return null;

    String[] split = values.split( "," );
    int[] ints = new int[split.length];

    for( int i = 0; i < split.length; i++ )
      ints[ i ] = Integer.parseInt( split[ i ] );

    return ints;
    }

  protected boolean containsKey( Map<String, String> params, String key )
    {
    return params != null && params.containsKey( key );
    }

  protected boolean getBoolean( Map<String, String> params, String key )
    {
    return getBoolean( params, key, false );
    }

  protected boolean getBoolean( Map<String, String> params, String key, boolean defaultValue )
    {
    String replace = params.get( key );

    return replace != null ? Boolean.parseBoolean( replace ) : defaultValue;
    }

  protected int getInteger( Map<String, String> params, String key )
    {
    return getInteger( params, key, 0 );
    }

  protected int getInteger( Map<String, String> params, String key, int defaultValue )
    {
    String replace = params.get( key );

    return replace != null ? Integer.parseInt( replace ) : defaultValue;
    }

  protected String getString( Map<String, String> params, String key )
    {
    return getString( params, key, null );
    }

  protected String getString( Map<String, String> params, String key, String defaultValue )
    {
    String replace = params.get( key );

    return replace != null ? replace : defaultValue;
    }
  }