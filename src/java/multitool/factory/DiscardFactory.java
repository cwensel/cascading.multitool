/*
 * Copyright (c) 2007-2011 Concurrent, Inc. All Rights Reserved.
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

package multitool.factory;

import java.util.Map;

import cascading.pipe.Pipe;
import cascading.pipe.assembly.Discard;
import cascading.tuple.Fields;

/**
 *
 */
public class DiscardFactory extends PipeFactory
  {
  public DiscardFactory( String alias )
    {
    super( alias );
    }

  public String getUsage()
    {
    return "narrow the stream removing the given fields. 0 for first, -1 for last";
    }

  public String[] getParameters()
    {
    return new String[]{};
    }

  public String[] getParametersUsage()
    {
    return new String[]{};
    }

  public Pipe addAssembly( String value, Map<String, String> subParams, Map<String, Pipe> pipes, Pipe pipe )
    {
    Fields fields = asFields( value );

    pipe = new Discard( pipe, fields );

    return pipe;
    }
  }