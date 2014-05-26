
/*
 * Copyright 2014 Madhu Siddalingaiah
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

MRViewer an educational tool that displays MapReduce steps through a graphical user interface.
All jobs are executed in memory on a single machine. The API is similar, but not the same
as Hadoop MapReduce. There are no external dependencies, only the Java runtime is required.
 
To execute the application, run the following command:

java -jar mrviewer.jar

MRViewer includes several demonstration MapReduce jobs as well as sample data. Selecting a
job will display usage information. Additional jobs can be added to the UI by modifying the
props.txt properties file.
