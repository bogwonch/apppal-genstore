JC=javac
JFLAGS=-Xlint:unchecked
CLASSPATH=.:lib/AppPAL.jar:lib/hamcrest-core-1.3.jar:lib/junit-4.12.jar:lib/takari-cpsuite-1.2.7-SNAPSHOT.jar

MANIFEST=Manifest.txt
TARGET_JAR=genstore.jar

%.class: %.java
	$(JC) $(JFLAGS) -cp $(CLASSPATH) $<

# Java dependencies must be checked in to be built
SRC=$(shell git ls-files \*.java)

# WARNING: use GNU make or build each step in all in order ;-)
all: | classes jar test

jar: $(TARGET_JAR)

# Compile each Java source file
classes: $(SRC:.java=.class)

$(TARGET_JAR): classes $(MANIFEST)
	jar cfm $(@) $(MANIFEST) genstore lib

clean:
	$(RM) $(SRC:.java=.class) $(TARGET_JAR)

test: $(TARGET_JAR)
	 @for test in `find genstore/test -name \*Test.java`; do \
		java -cp ${TARGET_JAR}:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar \
			org.junit.runner.JUnitCore \
			`sed -e 's/.java$$//g;s/\//./g' <<< $$test`;  \
	done

