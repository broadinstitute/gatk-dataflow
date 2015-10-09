[![Build Status](https://travis-ci.org/broadinstitute/gatk-dataflow.svg?branch=master)](https://travis-ci.org/broadinstitute/gatk-dataflow)
[![Coverage Status](https://coveralls.io/repos/broadinstitute/gatk-dataflow/badge.svg?branch=master)](https://coveralls.io/r/broadinstitute/gatk-dataflow?branch=master)

**Do not use this code for production work, it has not been validated for correctness and it's results are not trustworthy.**

GATK 4 Dataflow Exploration
============================

**This is not an actively developed project!**

**It is a record of project Hellbender's exploratory work into running GATK tools in [Google's Dataflow cloud framework](https://cloud.google.com/dataflow/).** 

**GATK 4 development is continuing in a different direction at (https://github.com/broadinstitute/gatk) and we welcome contributions!**

**Feel free to make use of this code if it is useful to you, but expect no support or updates.**

If you are looking for the current version of GATK to use in production work, please see the [GATK website](http://www.broadinstitute.org/gatk), where you can download a precompiled executable, read documentation, ask questions and receive technical support.

If you are looking for the codebase of the current production version of GATK, please see either the [GATK development framework repository](https://github.com/broadgsa/gatk/) or the [full GATK tools repository](https://github.com/broadgsa/gatk-protected/).


Requirements
------------
* Java 8

* Gradle 2.7

* Git 2.5+

Installation
------------
To build and run all tests, run `gradle check`. Test report is in `build/reports/tests/index.html`.

To only build, run `gradle installDist`.

To run all tests, run `gradle test`. 
What will happen depends on the value of the `CLOUD` environment variable: if it's `false` or unset then only local tests are run. If it's `mandatory` then it'll run only the cloud tests. 

To run a single test class, run something like this, `gradle test -Dtest.single=ReadUtilsUnitTest`.

To run tests and compute coverage reports, run `gradle jacocoTestReport`. The report is then in `build/reports/jacoco/test/html/index.html`. (IntelliJ 14 has a good coverage tool that is preferable for development).

To run the main program, run `build/install/gatk-dataflow/bin/gatk-dataflow`.

Note: for faster gradle operations, add `org.gradle.daemon=true` to your `~/.gradle/gradle.properties` file.  This will keep a gradle daemon running in the background and avoid the ~6s gradle start up time on every command.  

Tests
----------------
We use [Travis-CI](https://travis-ci.org/broadinstitute/hellbender) as our continuous integration provider.

* Before merging any branch make sure that all required tests pass on travis.
* Every travis build will upload the test results to our hellbender-dataflow google bucket.  A link to the uploaded report will appear at the very bottom of the travis log.  Look for the line that says `See the test report at`.
If TestNG itself crashes there will be no report generated.

Creating a GATK-Dataflow project in the IntelliJ IDE:
----------------

* Ensure that you have `gradle` and the Java 8 JDK installed

* Install the TestNG plugin (in preferences)

* Clone the gatk-dataflow repository using git

* In IntelliJ, go to File -> "Import Project"

* Select the root directory of your gatk-dataflow clone, then "Ok"

* Select "Import project from external model", then "Gradle", then "Next"

* Ensure that "Gradle project" points to the build.gradle file in the root of your gatk-dataflow clone

* Select "Use local gradle distribution", and enter your Gradle home directory in the "Gradle home" box. This will be the directory that contains the `bin` directory where the **actual** gradle executable (**not** merely a symlink to it!) lives. For example, if the actual gradle executable is `/usr/local/Cellar/gradle/2.2.1/libexec/bin/gradle`, you would enter `/usr/local/Cellar/gradle/2.2.1/libexec/` as your gradle home directory.

* Click "Finish"

* After downloading project dependencies, IntelliJ should open a new window with your project

* In File -> "Project Structure" -> "Project", set the "Project SDK" to your Java 1.8 JDK, and "Project language level" to 8 (you may need to add your Java 8 JDK under "Platform Settings" -> SDKs if it isn't there already). Then click "Apply"/"Ok".


Setting up debugging in IntelliJ
----------------

* Follow the instructions above for creating an IntelliJ project for gatk-dataflow

* Go to Run -> "Edit Configurations", then click "+" and add a new "Application" configuration

* Set the name of the new configuration to something like "gatk-dataflow debug"

* For "Main class", enter `org.broadinstitute.hellbender.Main`

* Ensure that "Use classpath of module:" is set to use the "gatk-dataflow" module's classpath

* Enter the gatk-dataflow arguments for the command you want to debug in "Program Arguments"

* Click "Apply"/"Ok"

* Set breakpoints, etc., as desired, then select "Run" -> "Debug" -> "gatk-dataflow debug" to start your debugging session

* In future debugging sessions, you can simply adjust the "Program Arguments" in the "gatk-dataflow debug" configuration as needed

Updating the Intellij project when dependencies change
-------------------
If there are dependency changes in `build.gradle` it is necessary to refresh the gradle project. This is easily done with the following steps.

* Open the gradle tool window  ( "View" -> "Tool Windows" -> "Gradle" )
* Click the refresh button in the Gradle tool window.  It is in the top left of the gradle view and is represented by two blue arrows.

